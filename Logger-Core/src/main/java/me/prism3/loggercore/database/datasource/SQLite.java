package me.prism3.loggercore.database.datasource;

import me.prism3.loggercore.database.AbstractDataSource;
import me.prism3.loggercore.database.data.Options;
import me.prism3.loggercore.database.queue.DatabaseQueue;

import java.io.File;
import java.sql.*;


public final class SQLite extends AbstractDataSource {

    private final File databaseFile;
    private final Options options;
    private final DatabaseQueue databaseQueue;
    public SQLite(Options options, File dataFolder) {

        super(options, SQLite.class.getName());

        this.options = options;
        this.databaseFile = new File(dataFolder, "data.db");

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.createTables();
        this.databaseQueue = new DatabaseQueue(this);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
    }

    @Override
    protected String getJdbcUrl() {
        return ("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
    }

    @Override
    protected void createTables() {

        try (final Connection connection = this.getConnection();
             final Statement statement = connection.createStatement()) {

            // Player Side Part
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_chat"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, message TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_commands"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, command VARCHAR(256), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_sign_text"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "x INTEGER, y INTEGER, z INTEGER, player_name TEXT, line VARCHAR(80), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_death"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, player_level INTEGER, x INTEGER, y INTEGER, z INTEGER," +
                    " cause VARCHAR(40), by_who TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_teleport"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, from_x INTEGER, from_y INTEGER, from_z INTEGER, to_x INTEGER," +
                    " to_y INTEGER, to_z INTEGER, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_connection"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, x INTEGER, y INTEGER, z INTEGER, ip INTEGER, is_staff BOOLEAN, player_connection_type VARCHAR(20) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS block_interaction"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, block VARCHAR(40), x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN, interaction_type VARCHAR(30) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_kick "
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, x INTEGER, y INTEGER, z INTEGER, reason TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_level"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "player_name TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_action"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, bucket VARCHAR(40), x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN, bucket_action VARCHAR(40) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS anvil"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, player_name TEXT," +
                    " new_name TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS item_action"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item TEXT, amount INTEGER, x INTEGER, y INTEGER, z INTEGER, enchantment TEXT," +
                    " changed_name TEXT, is_staff BOOLEAN, item_action_type VARCHAR(40) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS enchanting"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " x INTEGER, y INTEGER, z INTEGER, enchantment TEXT, enchantment_level INTEGER, item TEXT," +
                    " cost INTEGER(5), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS book_editing"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " page_count INTEGER, page_content VARCHAR(250), signed_by VARCHAR(25), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS furnace"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item VARCHAR(250), amount INTEGER, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS game_mode"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " game_mode VARCHAR(15), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS crafting"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item TEXT, amount INTEGER, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS registration"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, player_name TEXT," +
                    " player_uuid VARCHAR(80), join_date TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS primed_tnt"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_uuid VARCHAR(80)," +
                    " player_name TEXT, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS chest_interaction"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_uuid VARCHAR(80)," +
                    " player_name TEXT, x INTEGER, y INTEGER, z INTEGER, items TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS entity_death"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_uuid VARCHAR(80)," +
                    " player_name TEXT, mob TEXT, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            // Server Side Part
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_count"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS server_start"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS server_stop"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS console_commands"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, command VARCHAR(256))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ram"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, total_memory INTEGER, used_memory INTEGER, free_memory INTEGER)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tps"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, tps INTEGER)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS portal_creation"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, caused_by TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS rcon"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, command TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS command_block"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, command VARCHAR(256))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS server_address(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "server_name TEXT, player_name TEXT NOT NULL, player_uuid TEXT NOT NULL, date DATETIME NOT NULL," +
                    " domain TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS armorstand_interaction(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " server_name TEXT," +
                    " player_name TEXT NOT NULL," +
                    " player_uuid TEXT NOT NULL," +
                    " date DATETIME NOT NULL," +
                    " x int NOT NULL, y int NOT NULL, z int NOT NULL, world TEXT NOT NULL, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS armorstand_endcrystal(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " server_name TEXT," +
                    " player_name TEXT NOT NULL," +
                    " player_uuid TEXT NOT NULL," +
                    " date DATETIME NOT NULL," +
                    " x int NOT NULL, y int NOT NULL, z int NOT NULL, world TEXT NOT NULL," +
                    " block TEXT NOT NULL," +
                    " interaction_type TEXT NOT NULL, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS lever_interaction (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " date DATETIME NOT NULL," +
                    " server_name TEXT," +
                    " world TEXT NOT NULL," +
                    " player_uuid TEXT NOT NULL," +
                    " player_name TEXT NOT NULL," +
                    " x int NOT NULL, y int NOT NULL, z int NOT NULL, is_staff BOOLEAN)");

            // Extras Side Part

            if (this.options.isEssentialsEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS afk(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                                " player_name TEXT, x INTEGER, y INTEGER, z INTEGER, is_staff BOOLEAN)");

            if (this.options.isAuthMeEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS wrong_password(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                                "player_name TEXT, is_staff BOOLEAN)");

            if (this.options.isVaultEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS vault(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                "player_name TEXT, old_balance DOUBLE, new_balance DOUBLE, is_staff BOOLEAN)");

            if (this.options.isLiteBansEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS litebans "
                        + "(server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        " sender TEXT, command TEXT, onwho TEXT, reason TEXT," +
                        " duration TEXT, is_silent BOOLEAN)");

            if (this.options.isAdvancedBanEnabled())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS advanced_ban (server_name TEXT," +
                                " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, type TEXT,executor TEXT," +
                                " executed_on TEXT, reason TEXT, expiration_date TEXT)");

            // Version Exceptions Part
            if (this.options.isViaVersion())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS wood_stripping (id INTEGER PRIMARY KEY AUTOINCREMENT," +
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
