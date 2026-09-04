package me.prism3.logger_core.database.providers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.prism3.logger_core.utils.Log;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteDatabaseProvider implements DatabaseProvider {

    private final File dataFolder;
    private HikariDataSource dataSource;

    public SQLiteDatabaseProvider(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    @Override
    public void initialize() {
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }

        final File dbFile = new File(this.dataFolder, "logs.db");
        final HikariConfig config = new HikariConfig();

        config.setDriverClassName("org.sqlite.JDBC");
        config.setPoolName("LoggerSQLitePool");
        config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(30000);

        this.dataSource = new HikariDataSource(config);

        Log.info("Connected to SQLite database at: " + dbFile.getAbsolutePath());
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
