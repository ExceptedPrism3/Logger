package me.prism3.loggercore.database.datasource;

import me.prism3.loggercore.database.DataSourceInterface;
import me.prism3.loggercore.database.data.Options;
import me.prism3.loggercore.database.entity.PlayerChat;
import me.prism3.loggercore.database.queue.QueueManager;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public final class SQLite implements DataSourceInterface {

    private final File databaseFile;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final Options options;
    private Connection connection;

    private QueueManager queueManager;

    public SQLite(Options options, File dataFolder) {
        this.options = options;
        this.databaseFile = new File(dataFolder, "data.db");
        try {
            this.databaseFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.createTables();
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public void setQueueManager(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
    }

    private String getJdbcUrl() {
        return ("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
    }

    private void createTables() {

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
                    "x INT, y INT, z INT, player_name TEXT, line VARCHAR(80), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_death"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, player_level INT, x INT, y INT, z INT," +
                    " cause VARCHAR(40), by_who TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_teleport"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, from_x INT, from_y INT, from_z INT, to_x INT," +
                    " to_y INT, to_z INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_connection"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, x INT, y INT, z INT, ip INT, is_staff BOOLEAN, player_connection_type VARCHAR(20) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS block_interaction"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, block VARCHAR(40), x INT, y INT, z INT, is_staff BOOLEAN, interaction_type VARCHAR(30) NOT NULL)");


            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_kick "
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, x INT, y INT, z INT, reason TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_level"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "player_name TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_action"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, bucket VARCHAR(40), x INT, y INT, z INT, is_staff BOOLEAN, bucket_action VARCHAR(40) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS anvil"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, player_name TEXT," +
                    " new_name TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS item_action"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item TEXT, amount INT, x INT, y INT, z INT, enchantment TEXT," +
                    " changed_name TEXT, is_staff BOOLEAN, item_action_type VARCHAR(40) NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS enchanting"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " x INT, y INT, z INT, enchantment TEXT, enchantment_level INT, item TEXT," +
                    " cost INT(5), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS book_editing"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " page_count INT, page_content VARCHAR(250), signed_by VARCHAR(25), is_staff BOOLEAN)");


            statement.executeUpdate("CREATE TABLE IF NOT EXISTS furnace"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item VARCHAR(250), amount INT, x INT, y INT, z INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS game_mode"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " game_mode VARCHAR(15), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS crafting"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item TEXT, amount INT, x INT, y INT, z INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS registration"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, player_name TEXT," +
                    " player_uuid VARCHAR(80), join_date TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS primed_tnt"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_uuid VARCHAR(80)," +
                    " player_name TEXT, x INT, y INT, z INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS chest_interaction"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_uuid VARCHAR(80)," +
                    " player_name TEXT, x INT, y INT, z INT, items TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS entity_death"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_uuid VARCHAR(80)," +
                    " player_name TEXT, mob TEXT, x INT, y INT, z INT, is_staff BOOLEAN)");

            // Server Side Part
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
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, total_memory INT, used_memory INT, free_memory INT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tps"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, tps INT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS portal_creation"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, caused_by TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS rcon"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, command TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS command_block"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, command VARCHAR(256))");

            // Extras Side Part
            if (this.options.isEssentialsEnabled()) {

                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS afk(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                                " player_name TEXT, x INT, y INT, z INT, is_staff BOOLEAN)");

            }

            if (this.options.isAuthMeEnabled()) {

                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS wrong_password(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                                "player_name TEXT, is_staff BOOLEAN)");

            }

            if (this.options.isVaultEnabled()) {

                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS vault(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                                "player_name TEXT, old_balance DOUBLE, new_balance DOUBLE, is_staff BOOLEAN)");

            }

            if (this.options.isLiteBansEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS litebans "
                        + "(server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        " sender TEXT, command TEXT, onwho TEXT, reason TEXT," +
                        " duration TEXT, is_silent BOOLEAN)");

            }

            if (this.options.isAdvancedBanEnabled()) {

                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS advanced_ban (server_name TEXT," +
                                " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, type TEXT,executor TEXT," +
                                " executed_on TEXT, reason TEXT, expiration_date TEXT)");

            }

            // Version Exceptions Part
            if (this.options.isViaVersion()) {

                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS wood_stripping (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, " +
                                "uuid TEXT, player_name TEXT, log_name TEXT, x INT, y INT, z INT, is_staff BOOLEAN)");

            }

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public PreparedStatement getPlayerChatStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO player_chat" +
                        " (server_name, world, player_name, message, is_staff, date) VALUES(?,?,?,?,?,?)");


    }

    @Override
    public PreparedStatement getPlayerCommandStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO player_commands" +
                        " (server_name, world, player_name, command, is_staff, date) VALUES(?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getPlayerSignTextStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO player_sign_text" +
                        " (server_name, world, x, y, z, player_name, line, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getPlayerDeathStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO player_death" +
                        " (server_name, world, player_name, player_level, x, y, z, cause, by_who, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?,?)");


    }

    @Override
    public PreparedStatement getPlayerTeleportStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO player_teleport" +
                        " (server_name, world, player_name, from_x, from_y, from_z, to_x, to_y, to_z, is_staff, date)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getPlayerConnectionStsm(Connection connection) throws SQLException {
//TODO convert ip in java instead of database
        return connection.prepareStatement(
                "INSERT INTO player_connection" +
                        " (server_name, world, player_name, x, y, z, ip, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getTpsStsm(Connection connection) throws SQLException {
//TODO check if it has date

        return connection.prepareStatement(
                "INSERT INTO tps (server_name, tps) VALUES(?,?)");


    }

    @Override
    public PreparedStatement getRAMStsm(Connection connection) throws SQLException {
//TODO check if it has date
        return connection.prepareStatement("INSERT INTO ram" +
                " (server_name, total_memory, used_memory, free_memory) VALUES(?,?,?,?)");
    }

    @Override
    public PreparedStatement getPlayerKickStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO player_kick" +
                        " (server_name, world, player_name, x, y, z, reason, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?)");


    }

    @Override
    public PreparedStatement getPortalCreateStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO portal_creation" +
                        " (server_name, world, caused_by) VALUES(?,?,?)");
    }

    @Override
    public PreparedStatement getPlayerLevelStsm(Connection connection) throws SQLException {
        return null;
    }


    @Override
    public PreparedStatement getBucketActionStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO bucket_action" +
                        " (server_name, world, player_name, bucket, x, y, z, is_staff, bucket_action, date ) VALUES(?,?,?,?,?,?,?,?,?,?)");


    }

    @Override
    public PreparedStatement getAnvilStsm(Connection connection) throws SQLException {
        return connection.prepareStatement("INSERT INTO `anvil` (server_name, new_name, player_name, is_staff) VALUES(?,?,?,?)");
    }

    @Override
    public void insertServerStart(String serverName) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement serverStart = connection.prepareStatement(
                     "INSERT INTO server_start (server_name) VALUES(?)")) {

            serverStart.setString(1, serverName);

            serverStart.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertServerStop(String serverName) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement serverStop = connection.prepareStatement(
                     "INSERT INTO server_stop (server_name) VALUES(?)")) {

            serverStop.setString(1, serverName);

            serverStop.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public PreparedStatement getEnchantStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO enchanting" +
                        " (server_name, world, player_name, x, y, z, enchantment, enchantment_level, item, cost, is_staff)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?,?)");
    }

    @Override
    public PreparedStatement getBookEditingStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO book_editing" +
                        " (server_name, world, player_name, page_count, page_content, signed_by, is_staff) VALUES(?,?,?,?,?,?,?)");


    }

    @Override
    public PreparedStatement getAfkStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO afk" +
                " (server_name, world, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?)");
    }

    @Override
    public PreparedStatement getWrongPasswordStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO wrong_password" +
                        " (server_name, world, player_name, is_staff) VALUES(?,?,?,?)");
    }

    @Override
    public PreparedStatement getItemActionStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO item_action" +
                        " (server_name, world, player_name, item, amount, x, y, z ,changed_name, is_staff, item_action_type, date, enchantment) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getFurnaceStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO furnace" +
                " (server_name, world, player_name, item, amount, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)");


    }

    @Override
    public void insertRCON(String serverName, String command) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement rCon = connection.prepareStatement("INSERT INTO rcon" +
                     " (server_name, ip, command) VALUES(?,?)")) {

            rCon.setString(1, serverName);
            rCon.setString(2, command);

            rCon.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PreparedStatement getGamemodeStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO game_mode" +
                        " (server_name, world, player_name, game_mode, is_staff, date) VALUES(?,?,?,?,?,?)");
    }

    @Override
    public PreparedStatement getPlayerCraftStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO crafting" +
                " (server_name, world, player_name, item, amount, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getVaultStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO vault" +
                " (server_name, player_name, old_balance, new_balance, is_staff) VALUES(?,?,?,?,?)");
    }

    @Override
    public void insertPlayerRegistration(String serverName, String playerName, String playerUUID,
                                         String joinDate) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement register = connection.prepareStatement(
                     "INSERT INTO registration" +
                             " (server_name, player_name, player_uuid, join_date) VALUES(?,?,?,?)")) {

            register.setString(1, serverName);
            register.setString(2, playerName);
            register.setString(3, playerUUID);
            register.setString(4, joinDate);

            register.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PreparedStatement getPrimedTntStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO primed_tnt" +
                        " (server_name, world, player_uuid, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getLiteBansStsm(Connection connection) throws SQLException {

        return connection.prepareStatement("INSERT INTO litebans" +
                " (server_name, sender, command, onwho, reason, duration, is_silent) VALUES(?,?,?,?,?,?,?)");
    }

    @Override
    public PreparedStatement getAdvancedDataStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO advanced_ban" +
                        " (server_name, type, executor, executed_on, reason, expiration_date) VALUES(?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getCommandBlockStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO command_block" +
                        " (server_name, command) VALUES(?,?)");
    }

    @Override
    public PreparedStatement getWoodStrippingStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO wood_stripping" +
                        " (server_name, world, uuid, player_name, log_name, x, y, z, is_staff, date) VALUES(?,?,?,?,?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getChestInteractionStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO chest_interaction" +
                        " (server_name, world, player_uuid, player_name, x, y, z, items, is_staff) VALUES(?,?,?,?,?,?,?,?,?)");

    }

    @Override
    public PreparedStatement getEntityDeathStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO entity_death" +
                        " (server_name, world, player_uuid, player_name, mob, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)");


    }

    @Override
    public PreparedStatement getConsoleCommandStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO console_commands" +
                        " (server_name, command, date) VALUES(?,?,?)");

    }

    @Override
    public void insertServerReload(String serverName, String playerName, boolean isStaff) {

    }

    @Override
    public PreparedStatement getPlayerLoginStsm(Connection connection) {
        return null;
    }


    @Override
    public PreparedStatement getServerSwitchStsm(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement getPAFPartyMessageStsm(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement getPAFFriendMessageStsm(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement getLeverInteractionStsm(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement getSpawnEggStsm(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement getWorldGuardStsm(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement getPlayerCountStsm(Connection connection) throws SQLException {
        return null;
    }


    @Override
    public void disconnect() { this.queueManager.flushQueue(); }

    @Override
    public List<PlayerChat> getPlayerChatByPlayerName(String playerName, int offset, int limit) {
        return Collections.emptyList();
    }

    @Override
    public Long countByTable(String action) {
        return 0L;
    }

    @Override
    public PreparedStatement getBlockInteractionStsm(Connection connection) throws SQLException {
        return connection.prepareStatement(
                "INSERT INTO block_interaction" +
                        " (server_name, world, player_name, block, x, y, z, is_staff, interaction_type, date) VALUES(?,?,?,?,?,?,?,?,?,?)");
    }

    @Override
    public PreparedStatement getLevelChangeStsm(Connection connection) throws SQLException {

        return connection.prepareStatement(
                "INSERT INTO player_level" +
                        " (server_name, player_name, is_staff) VALUES(?,?,?)");
    }

    public void insertAnvil(String serverName, String playerName, String newName, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement anvil = connection.prepareStatement("INSERT INTO anvil" +
                     " (server_name, player_name, new_name, is_staff) VALUES(?,?,?,?)")) {

            anvil.setString(1, serverName);
            anvil.setString(2, playerName);
            anvil.setString(3, newName);
            anvil.setBoolean(4, isStaff);

            anvil.executeUpdate();

        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

}
