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
    protected final Options options;
    protected HikariDataSource dataSource;

    protected AbstractDataSource(DatabaseCredentials databaseCredentials, Options options,
                                 String className) throws SQLException {
        this.className = className;
        this.databaseCredentials = databaseCredentials;
        this.options = options;
        this.initializeDataSource();

        if (!DatabaseUtils.checkConnectionIfAlright(this.dataSource))
            throw new SQLException("Failed to connect to database!");


    }
    public void createTable() {

        try (final Connection connection = this.dataSource.getConnection();
             final Statement statement = connection.createStatement()) {

            // Player Side Part
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_chat "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), message VARCHAR(200), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_commands "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), command VARCHAR(256), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_sign_text "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "x INT, y INT, z INT, player_name VARCHAR(100), line VARCHAR(60), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_death "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), player_level INT, x INT, y INT, z INT," +
                    " cause VARCHAR(40), by_who VARCHAR(30), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_teleport "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), from_x INT, from_y INT, from_z INT, to_x INT," +
                    " to_y INT, to_z INT, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_join "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), x INT, y INT, z INT, ip INT UNSIGNED, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_leave "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), x INT, y INT, z INT, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS block_place "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), block VARCHAR(40), x INT, y INT, z INT, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS block_break "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), block VARCHAR(40), x INT, y INT, z INT, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_kick "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), x INT, y INT, z INT, reason VARCHAR(50), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_level "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                    "player_name VARCHAR(100), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_fill "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), bucket VARCHAR(40), x INT, y INT, z INT, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_empty "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                    "player_name VARCHAR(100), bucket VARCHAR(40), x INT, y INT, z INT, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS anvil "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100)," +
                    " new_name VARCHAR(100), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS item_drop "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_name VARCHAR(100)," +
                    " item VARCHAR(50), amount INT, x INT, y INT, z INT, enchantment VARCHAR(50)," +
                    " changed_name VARCHAR(50), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS enchanting "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_name VARCHAR(100)," +
                    " x INT, y INT, z INT, enchantment VARCHAR(50), enchantment_level INT, item VARCHAR(50)," +
                    " cost INT(5), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS book_editing "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_name VARCHAR(100)," +
                    " page_count INT, page_content VARCHAR(250), signed_by VARCHAR(25), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS item_pickup "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_name VARCHAR(100)," +
                    " item VARCHAR(250), amount INT, x INT, y INT, z INT,  changed_name VARCHAR(250), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS furnace "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_name VARCHAR(100)," +
                    " item VARCHAR(250), amount INT, x INT, y INT, z INT, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS game_mode "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_name VARCHAR(100)," +
                    " game_mode VARCHAR(15), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS crafting "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_name VARCHAR(100)," +
                    " item VARCHAR(50), amount INT, x INT, y INT, z INT, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS registration "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(30)," +
                    " player_uuid VARCHAR(80), join_date VARCHAR(30))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS primed_tnt "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_uuid VARCHAR(80)," +
                    " player_name VARCHAR(100), x INT, y INT, z INT, is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS chest_interaction "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_uuid VARCHAR(80)," +
                    " player_name VARCHAR(100), x INT, y INT, z INT, items VARCHAR(255), is_staff TINYINT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS entity_death "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), player_uuid VARCHAR(80)," +
                    " player_name VARCHAR(100), mob VARCHAR(50), x INT, y INT, z INT, is_staff TINYINT)");

            // Server Side Part
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS server_start "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS server_stop "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS console_commands "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), command VARCHAR(256))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ram "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), total_memory INT, used_memory INT, free_memory INT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tps "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), tps INT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS portal_creation "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), caused_by VARCHAR(50))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS rcon "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), ip INT UNSIGNED, command VARCHAR(50))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS command_block "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, server_name VARCHAR(30)," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), command VARCHAR(256))");

            // Extras Side Part
            if (this.options.isEssentialsEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS afk (id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT," +
                        " server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                        " player_name VARCHAR(100), x INT, y INT, z INT, is_staff TINYINT)");

                tablesNames.add("afk");
            }

            if (this.options.isAuthMeEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS wrong_password (id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT," +
                        " server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100)," +
                        "player_name VARCHAR(100), is_staff TINYINT)");

                tablesNames.add("wrong_password");
            }

            if (this.options.isVaultEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS vault (id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT," +
                        " server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), " +
                        "player_name VARCHAR(100), old_balance DOUBLE, new_balance DOUBLE, is_staff TINYINT)");

                tablesNames.add("vault");
            }

            if (this.options.isLiteBansEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS litebans "
                        + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                        " sender VARCHAR(100), command VARCHAR(20), onwho VARCHAR(100), reason VARCHAR(200)," +
                        " duration VARCHAR(30), is_silent TINYINT)");

                tablesNames.add("litebans");
            }

            if (this.options.isAdvancedBanEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS advanced_ban (server_name VARCHAR(30)," +
                        " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), type VARCHAR(30),executor VARCHAR(30)," +
                        " executed_on VARCHAR(30), reason VARCHAR(100), expiration_date VARCHAR(50))");

                tablesNames.add("advanced_ban");
            }

            // Version Exceptions Part
            if (this.options.isViaVersion()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS wood_stripping (id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT," +
                        " server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), world VARCHAR(100), " +
                        "uuid VARCHAR(100), player_name VARCHAR(100), log_name VARCHAR(30), x INT, y INT, z INT, is_staff TINYINT)");

                tablesNames.add("wood_stripping");
            }

        } catch (final SQLException e) { e.printStackTrace(); }
    }



    protected void initializeDataSource() {

        this.dataSource = new HikariDataSource();
        this.dataSource.setMaxLifetime(1800 * 1000L);
        this.dataSource.setMaximumPoolSize(100);
        this.dataSource.setIdleTimeout(600000);
        this.dataSource.setJdbcUrl(this.getJdbcUrl());
        this.dataSource.setUsername(this.databaseCredentials.getDbUsername());
        this.dataSource.setPassword(this.databaseCredentials.getDbPassword());
        this.dataSource.setDriverClassName(this.className);
        this.dataSource.addDataSourceProperty("characterEncoding", "utf8");
        this.dataSource.addDataSourceProperty("encoding", "UTF-8");
        this.dataSource.addDataSourceProperty("useUnicode", "true");
        this.dataSource.setPoolName("Logger-HikariPool");
    }

    protected abstract String getJdbcUrl();



}
