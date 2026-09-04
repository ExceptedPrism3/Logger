package me.prism3.logger_core.database;

import me.prism3.logger_core.database.providers.DatabaseProvider;
import me.prism3.logger_core.database.providers.MySQLDatabaseProvider;
import me.prism3.logger_core.database.providers.SQLiteDatabaseProvider;
import me.prism3.logger_core.platform.LoggerPlatform;
import me.prism3.logger_core.utils.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DatabaseManager {

    private final LoggerPlatform platform;
    private final DatabaseConfig config;
    private DatabaseProvider provider;
    
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final ThreadLocal<Connection> currentTransaction = new ThreadLocal<>();
    private volatile boolean isShutdown = false;
    private Thread workerThread;

    // Table definitions
    private final Map<String, String> tables;

    public DatabaseManager(LoggerPlatform platform, DatabaseConfig config) {
        this.platform = platform;
        this.config = config;
        
        boolean isSqlite = "sqlite".equalsIgnoreCase(config.type);
        String autoInc = isSqlite ? "id INTEGER PRIMARY KEY AUTOINCREMENT" : "id INT AUTO_INCREMENT PRIMARY KEY";
        String dateType = isSqlite ? "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" : "DATETIME DEFAULT CURRENT_TIMESTAMP";

        // Initialize Schema definitions
        Map<String, String> tempTables = new HashMap<>();
        tempTables.put("player_chat", getPlayerSideFields() + "message TEXT, is_staff TINYINT(1)");
        tempTables.put("player_command", getPlayerSideFields() + "command TEXT, is_staff TINYINT(1)");
        tempTables.put("player_kick", getPlayerSideFields() + "reason TEXT, is_staff TINYINT(1)");
        tempTables.put("player_login", getPlayerSideFields() + "ip_address VARCHAR(45), is_staff TINYINT(1)");
        tempTables.put("player_quit", getPlayerSideFields() + "is_staff TINYINT(1)");
        tempTables.put("player_server_switch", getPlayerSideFields() + "from_server VARCHAR(50), to_server VARCHAR(50), is_staff TINYINT(1)");
        
        // Server tables
        tempTables.put("server_start", getCommonFields());
        tempTables.put("server_stop", getCommonFields());
        
        // Proxy tables (Unified Bungee & Velocity)
        tempTables.put("player_events", autoInc + ", server_name VARCHAR(100), player_name VARCHAR(100), event_type VARCHAR(50), message TEXT, date " + dateType);
        tempTables.put("server_events", autoInc + ", server_name VARCHAR(100), event_type VARCHAR(50), message TEXT, date " + dateType);
        
        this.tables = Collections.unmodifiableMap(tempTables);
    }
    
    public void initialize() {
        if (!config.enabled) return;

        try {
            this.provider = createProvider();
            this.provider.initialize();
            try (Connection conn = this.provider.getConnection()) {
                SchemaMigrator.migrate(conn, this.config, this.tables);
            }
            this.createTables();
            this.startWorker();
            this.purgeOldLogs();
            Log.info("Database initialized successfully (" + config.type.toUpperCase() + ").");
        } catch (Exception e) {
            Log.severe("Failed to initialize database: " + e.getMessage(), e);
        }
    }

    private DatabaseProvider createProvider() {
        String type = config.type.toLowerCase();
        switch (type) {
            case "sqlite":
                return new SQLiteDatabaseProvider(platform.getDataFolder());
            case "mysql":
            case "mariadb":
                return new MySQLDatabaseProvider(config);
            default:
                throw new IllegalArgumentException("Unsupported database type: " + type);
        }
    }

    private void createTables() {
        try (Connection conn = provider.getConnection();
             Statement stmt = conn.createStatement()) {
            
            for (Map.Entry<String, String> entry : tables.entrySet()) {
                String sql = "CREATE TABLE IF NOT EXISTS " + config.tablePrefix + entry.getKey() + " (" + entry.getValue() + ");";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            Log.severe("Failed to create tables", e);
        }
    }

    private String getCommonFields() {
        boolean isSqlite = "sqlite".equalsIgnoreCase(config.type);
        if (isSqlite) {
            return "id INTEGER PRIMARY KEY AUTOINCREMENT, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, server_name VARCHAR(100)";
        } else {
            return "id INTEGER PRIMARY KEY AUTO_INCREMENT, date DATETIME DEFAULT CURRENT_TIMESTAMP, server_name VARCHAR(100)";
        }
    }

    private String getPlayerSideFields() {
        return getCommonFields() + ", player_uuid VARCHAR(36), player_name VARCHAR(100), world_name VARCHAR(100), " +
               "location_x INT, location_y INT, location_z INT, ";
    }

    public void submit(Runnable job) {
        if (isShutdown) return;
        taskQueue.offer(job);
    }
    
    public Connection getConnection() throws SQLException {
        Connection tx = currentTransaction.get();
        if (tx != null) {
             return (Connection) java.lang.reflect.Proxy.newProxyInstance(
                    DatabaseManager.class.getClassLoader(),
                    new Class<?>[]{Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("close")) return null;
                        return method.invoke(tx, args);
                    });
        }
        return provider.getConnection();
    }

    private void startWorker() {
        workerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable first = taskQueue.take();
                    List<Runnable> batch = new ArrayList<>();
                    batch.add(first);
                    taskQueue.drainTo(batch, 99);

                    try (Connection conn = provider.getConnection()) {
                        conn.setAutoCommit(false);
                        currentTransaction.set(conn);
                        for (Runnable task : batch) {
                            try {
                                task.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        conn.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        currentTransaction.remove();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "LoggerCore-DB-Worker");
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public void purgeOldLogs() {
        if (!config.enabled || config.dataDeletion <= 0) return;
        submit(() -> {
            boolean isSqlite = "sqlite".equalsIgnoreCase(config.type);
            String sql;
            if (isSqlite) {
                sql = "DELETE FROM %s WHERE date < datetime('now', '-%d days')";
            } else {
                sql = "DELETE FROM %s WHERE date < DATE_SUB(NOW(), INTERVAL %d DAY)";
            }
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                for (String table : tables.keySet()) {
                    String fullTableName = config.tablePrefix + table;
                    try {
                        stmt.executeUpdate(String.format(sql, fullTableName, config.dataDeletion));
                    } catch (SQLException ignored) {}
                }
            } catch (SQLException e) {
                Log.severe("Failed to purge old logs from database: " + e.getMessage());
            }
        });
    }

    public void shutdown() {
        isShutdown = true;
        if (workerThread != null) {
            workerThread.interrupt();
            try {
                workerThread.join(1000);
            } catch (InterruptedException ignored) {}
        }
        if (provider != null) {
            try {
                provider.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    public boolean isEnabled() {
        return config.enabled && provider != null;
    }

    public String getTableName(String table) {
        return config.tablePrefix + table;
    }

    public java.util.Set<String> getPlayerTableNames() {
        return tables.keySet();
    }
}
