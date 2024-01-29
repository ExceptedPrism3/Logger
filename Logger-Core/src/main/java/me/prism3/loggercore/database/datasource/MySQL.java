package me.prism3.loggercore.database.datasource;

import me.prism3.loggercore.database.AbstractDataSource;
import me.prism3.loggercore.database.data.Options;
import me.prism3.loggercore.database.data.Settings;
import me.prism3.loggercore.database.queue.DatabaseQueue;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public final class MySQL extends AbstractDataSource {

    private final Options options;
    private final DatabaseQueue databaseQueue;

    private final Settings settings;
    
    public MySQL(@NotNull Settings settings, @NotNull Options options, String prefix) {

        super(options, MySQL.class.getName(), prefix);
        this.options = options;
        this.settings = settings;
        this.createTables();
        this.databaseQueue = new DatabaseQueue(this);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(settings.getJdbcUrl(), settings.getDbUsername(), settings.getDbPassword());
    }

    @Override
    protected String getJdbcUrl() {
        return settings.getJdbcUrl();
    }

    @Override
    public void createTables() {

        try (final Connection connection = this.getConnection();
             final Statement statement = connection.createStatement()) {

            // Player Side Part
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_player_chat"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, message TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_player_commands"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, command VARCHAR(256), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_player_sign_text"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "x INTEGER, y INTEGER, z INTEGER, player_name TEXT, line VARCHAR(80), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_player_death"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, player_level INTEGER, x INTEGER, y INTEGER, z INTEGER," +
                    " cause VARCHAR(40), by_who TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_player_teleport"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, from_x INTEGER, from_y INTEGER, from_z INTEGER, to_x INTEGER," +
                    " to_y INTEGER, to_z INTEGER, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_player_connection"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, x INTEGER, y INTEGER, z INTEGER, ip INTEGER, is_staff BOOLEAN, player_connection_type VARCHAR(20) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_block_interaction"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, block VARCHAR(40), x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN, interaction_type VARCHAR(30) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_player_kick "
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, x INTEGER, y INTEGER, z INTEGER, reason TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_player_level"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "player_name TEXT,player_level INTEGER,  is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_bucket_action"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, bucket VARCHAR(40), x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN, bucket_action VARCHAR(40) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_anvil"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, player_name TEXT," +
                    " new_name TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_item_action"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item TEXT, amount INTEGER, x INTEGER, y INTEGER, z INTEGER, enchantment TEXT," +
                    " changed_name TEXT, is_staff BOOLEAN, item_action_type VARCHAR(40) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_enchanting"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " x INTEGER, y INTEGER, z INTEGER, enchantment TEXT, enchantment_level INTEGER, item TEXT," +
                    " cost INTEGER(5), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_book_editing"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " page_count INTEGER, page_content VARCHAR(250), signed_by VARCHAR(25), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_furnace"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item VARCHAR(250), amount INTEGER, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_game_mode"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " game_mode VARCHAR(15), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_crafting"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item TEXT, amount INTEGER, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_registration"
                    + "(player_uuid VARCHAR(80) PRIMARY KEY, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, player_name TEXT," +
                    " join_date TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_primed_tnt"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_uuid VARCHAR(80)," +
                    " player_name TEXT, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_chest_interaction"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_uuid VARCHAR(80)," +
                    " player_name TEXT, x INTEGER, y INTEGER, z INTEGER, items TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_entity_death"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_uuid VARCHAR(80)," +
                    " player_name TEXT, mob TEXT, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            // Server Side Part
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_player_count"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_server_start"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_server_stop"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_console_commands"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, command VARCHAR(256))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_ram"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, total_memory INTEGER, used_memory INTEGER, free_memory INTEGER)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_tps"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, tps INTEGER)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_portal_creation"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, caused_by TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_rcon"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, command TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_command_block"
                    + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, command VARCHAR(256))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_server_address(id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "server_name TEXT, player_name TEXT NOT NULL, player_uuid TEXT NOT NULL, date DATETIME NOT NULL," +
                    " domain TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_armorstand_interaction(id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    " server_name TEXT," +
                    " player_name TEXT NOT NULL," +
                    " player_uuid TEXT NOT NULL," +
                    " date DATETIME NOT NULL," +
                    " x int NOT NULL, y int NOT NULL, z int NOT NULL, world TEXT NOT NULL, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_armorstand_endcrystal(id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    " server_name TEXT," +
                    " player_name TEXT NOT NULL," +
                    " player_uuid TEXT NOT NULL," +
                    " date DATETIME NOT NULL," +
                    " x int NOT NULL, y int NOT NULL, z int NOT NULL, world TEXT NOT NULL," +
                    " block TEXT NOT NULL," +
                    " interaction_type TEXT NOT NULL, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_lever_interaction (id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    " date DATETIME NOT NULL," +
                    " server_name TEXT," +
                    " world TEXT NOT NULL," +
                    " player_uuid TEXT NOT NULL," +
                    " player_name TEXT NOT NULL," +
                    " x int NOT NULL, y int NOT NULL, z int NOT NULL, is_staff BOOLEAN)");

            // Extras Side Part

            if (this.options.isEssentialsEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_afk(id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                                " player_name TEXT, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            if (this.options.isAuthMeEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_wrong_password(id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                                "player_name TEXT, is_staff BOOLEAN)");

            if (this.options.isVaultEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_vault(id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                "player_name TEXT, old_balance DOUBLE, new_balance DOUBLE, is_staff BOOLEAN)");

            if (this.options.isLiteBansEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_litebans "
                        + "(server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        " sender TEXT, command TEXT, onwho TEXT, reason TEXT," +
                        " duration TEXT, is_silent BOOLEAN)");

            if (this.options.isAdvancedBanEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_advanced_ban (server_name TEXT, " +
                        "date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, type TEXT, executor_uuid TEXT NOT NULL, " +
                        "executor TEXT, executed_on TEXT, reason TEXT, expiration_date TEXT)");

            // Version Exceptions Part
            if (this.options.isViaVersion())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+this.prefix+"_wood_stripping (id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, " +
                                "uuid TEXT, player_name TEXT, log_name TEXT, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");
        } catch (final SQLException e) { e.printStackTrace(); }

    }

    @Override
    public void disconnect() {
        this.databaseQueue.flushQueue();
    }

    @Override
    public DatabaseQueue getDatabaseQueue() {
        return this.databaseQueue;
    }
}