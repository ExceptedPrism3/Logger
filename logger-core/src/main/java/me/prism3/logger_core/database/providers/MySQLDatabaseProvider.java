package me.prism3.logger_core.database.providers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.prism3.logger_core.database.DatabaseConfig;
import me.prism3.logger_core.utils.Log;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLDatabaseProvider implements DatabaseProvider {

    private final DatabaseConfig config;
    private HikariDataSource dataSource;

    public MySQLDatabaseProvider(DatabaseConfig config) {
        this.config = config;
    }

    @Override
    public void initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver", true, Thread.currentThread().getContextClassLoader());
        } catch (final ClassNotFoundException e) {
            Log.warn("MySQL JDBC Driver not found via explicit load: " + e.getMessage());
        }

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("LoggerPool");
        hikariConfig.setJdbcUrl(String.format(
                "jdbc:mysql://%s:%d/%s?autoReconnect=true&useSSL=false&serverTimezone=UTC",
                config.host, config.port, config.database));

        hikariConfig.setUsername(config.username);
        hikariConfig.setPassword(config.password);

        hikariConfig.setConnectionTimeout(30000);
        hikariConfig.setIdleTimeout(600000);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setLeakDetectionThreshold(30000);

        // Standard optimizations
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(hikariConfig);

        Log.info(String.format("Connected to MySQL/MariaDB database at: %s:%d/%s using HikariCP.", 
                config.host, config.port, config.database));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void close() {
        if (this.dataSource != null)
            this.dataSource.close();
    }
}
