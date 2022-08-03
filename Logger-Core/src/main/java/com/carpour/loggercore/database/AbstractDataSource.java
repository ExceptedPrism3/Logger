package com.carpour.loggercore.database;

import com.carpour.loggercore.database.data.DatabaseCredentials;
import com.carpour.loggercore.database.data.Options;
import com.carpour.loggercore.database.utils.DatabaseUtils;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractDataSource implements DataSourceInterface {
    protected static final List<String> tablesNames = Stream.of("player_chat", "player_commands", "player_sign_text",
            "player_death", "player_teleport", "player_join", "player_leave", "block_place", "block_break",
            "player_kick", "player_level", "Bucket_fill", "bucket_empty", "anvil", "item_drop", "enchanting",
            "book_editing", "item_pickup", "furnace", "game_mode", "crafting", "registration", "server_start",
            "server_stop", "console_commands", "ram", "tps", "portal_creation", "rcon", "primed_tnt", "command_block",
            "chest_interaction", "entity_death", "logger_playertime").collect(Collectors.toCollection(ArrayList::new));
    protected final DatabaseCredentials databaseCredentials;
    private final String className;
    protected final Logger logger = Logger.getLogger(AbstractDataSource.class.getName());
    protected HikariDataSource dataSource;

    protected final Options options;

    protected AbstractDataSource(DatabaseCredentials databaseCredentials, Options options,
                                  String className) throws SQLException {

        this.className = className;
        this.databaseCredentials = databaseCredentials;
        this.options = options;
        this.initializeDataSource();


        if (!DatabaseUtils.checkConnectionIfAlright(this.dataSource))
        {

            throw new SQLException("Failed to connect to database!");

        }
        this.createTables();

    }



    public void createTables() {
    this.createPlayerTime();

    }


    private void createPlayerTime() {
        try (final Connection connection = this.dataSource.getConnection();
             final Statement statement = connection.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS logger_playertime(`player_name` VARCHAR(30)  NOT NULL, player_time int UNSIGNED DEFAULT 0, PRIMARY KEY(player_name)) CHARACTER SET = utf8;";
            statement.execute(sql);
        } catch (SQLException e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }


    private void initializeDataSource() {

        this.dataSource = new HikariDataSource();
        this.dataSource.setMaxLifetime(1800 * 1000L);
        this.dataSource.setMaximumPoolSize(100);
        this.dataSource.setIdleTimeout(600000);
        this.dataSource.setUsername(this.databaseCredentials.getDbUsername());
        this.dataSource.setPassword(this.databaseCredentials.getDbPassword());
        this.dataSource.setJdbcUrl(this.getJdbcUrl());
        this.dataSource.setDriverClassName(this.className);
        this.dataSource.addDataSourceProperty("characterEncoding", "utf8");
        this.dataSource.addDataSourceProperty("encoding", "UTF-8");
        this.dataSource.addDataSourceProperty("useUnicode", "true");
        this.dataSource.setPoolName("Logger-HikariPool");
    }

    protected abstract String getJdbcUrl();

    public static List<String> getTableNames() { return tablesNames; }

}
