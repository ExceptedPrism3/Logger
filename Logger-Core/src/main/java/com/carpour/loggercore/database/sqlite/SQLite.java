package com.carpour.loggercore.database.sqlite;

import com.carpour.loggercore.database.AbstractDataSource;
import com.carpour.loggercore.database.DataSourceInterface;
import com.carpour.loggercore.database.data.Options;
import com.carpour.loggercore.database.entity.Coordinates;
import com.carpour.loggercore.database.entity.PlayerChat;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SQLite implements DataSourceInterface {

    private static final List<String> tablesNames = Stream.of("player_chat", "player_commands", "player_sign_text",
            "player_death", "player_teleport", "player_join", "player_leave", "block_place", "block_break",
            "player_kick", "player_level", "Bucket_fill", "bucket_empty", "anvil", "item_drop", "enchanting",
            "book_editing", "item_pickup", "furnace", "game_mode", "crafting", "registration", "server_start",
            "server_stop", "console_commands", "ram", "tps", "portal_creation", "rcon", "primed_tnt", "command_block",
            "chest_interaction", "entity_death", "logger_playertime").collect(Collectors.toCollection(ArrayList::new));

    private final File databaseFile;
    private final Options options;
    private final Logger logger = Logger.getLogger(AbstractDataSource.class.getName());

    public SQLite(Options options, File databaseFile) throws SQLException {

        this.options = options;
        this.databaseFile = databaseFile;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) { throw new RuntimeException(e); }

        this.createDatabaseFile();
        this.createTable();

    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
    }

    private void createDatabaseFile() {
        try {
            if (!this.databaseFile.exists())
                this.databaseFile.createNewFile();

        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private String getJdbcUrl() {
        return ("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
    }

    private void createTable() {

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
                    "x INT, y INT, z INT, player_name TEXT, line VARCHAR(60), is_staff BOOLEAN)");

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

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_join"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, x INT, y INT, z INT, ip INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_leave"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, x INT, y INT, z INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS block_place"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, block VARCHAR(40), x INT, y INT, z INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS block_break "
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, block VARCHAR(40), x INT, y INT, z INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_kick "
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, x INT, y INT, z INT, reason TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_level"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "player_name TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_fill"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, bucket VARCHAR(40), x INT, y INT, z INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_empty"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                    "player_name TEXT, bucket VARCHAR(40), x INT, y INT, z INT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS anvil"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, player_name TEXT," +
                    " new_name TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS item_drop"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item TEXT, amount INT, x INT, y INT, z INT, enchantment TEXT," +
                    " changed_name TEXT, is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS enchanting"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " x INT, y INT, z INT, enchantment TEXT, enchantment_level INT, item TEXT," +
                    " cost INT(5), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS book_editing"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " page_count INT, page_content VARCHAR(250), signed_by VARCHAR(25), is_staff BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS item_pickup"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, player_name TEXT," +
                    " item VARCHAR(250), amount INT, x INT, y INT, z INT,  changed_name VARCHAR(250), is_staff BOOLEAN)");

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
                    " player_name TEXT, x INT, y INT, z INT, items VARCHAR(255), is_staff BOOLEAN)");

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
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, ip INT, command TEXT)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS command_block"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, server_name TEXT," +
                    " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, command VARCHAR(256))");

            // Extras Side Part
            if (this.options.isEssentialsEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS afk(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                        " player_name TEXT, x INT, y INT, z INT, is_staff BOOLEAN)");

                tablesNames.add("afk");
            }

            if (this.options.isAuthMeEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS wrong_password(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT," +
                        "player_name TEXT, is_staff BOOLEAN)");

                tablesNames.add("wrong_password");
            }

            if (this.options.isVaultEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS vault(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        "player_name TEXT, old_balance DOUBLE, new_balance DOUBLE, is_staff BOOLEAN)");

                tablesNames.add("vault");
            }

            if (this.options.isLiteBansEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS litebans "
                        + "(server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        " sender TEXT, command TEXT, onwho TEXT, reason TEXT," +
                        " duration TEXT, is_silent BOOLEAN)");

                tablesNames.add("litebans");
            }

            if (this.options.isAdvancedBanEnabled()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS advanced_ban (server_name TEXT," +
                        " date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, type TEXT,executor TEXT," +
                        " executed_on TEXT, reason TEXT, expiration_date TEXT)");

                tablesNames.add("advanced_ban");
            }

            // Version Exceptions Part
            if (this.options.isViaVersion()) {

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS wood_stripping (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " server_name TEXT, date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, world TEXT, " +
                        "uuid TEXT, player_name TEXT, log_name TEXT, x INT, y INT, z INT, is_staff BOOLEAN)");

                tablesNames.add("wood_stripping");
            }

        } catch (final SQLException e) { e.printStackTrace(); }
    }


    @Override
    public void insertPlayerChat(String serverName, String playerName, String playerUUID, String worldName, String msg, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement playerChat = connection.prepareStatement("INSERT INTO player_chat" +
                     " (server_name, world, player_name, message, is_staff) VALUES(?,?,?,?,?)")) {

            playerChat.setString(1, serverName);
            playerChat.setString(2, worldName);
            playerChat.setString(3, playerName);
            playerChat.setString(4, msg);
            playerChat.setBoolean(5, isStaff);

            playerChat.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerCommands(String serverName, String playerName, String playerUUID, String worldName, String command, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement playerCommands = connection.prepareStatement("INSERT INTO player_commands" +
                     " (server_name, world, player_name, command, is_staff) VALUES(?,?,?,?,?)")) {

            playerCommands.setString(1, serverName);
            playerCommands.setString(2, worldName);
            playerCommands.setString(3, playerName);
            playerCommands.setString(4, command);
            playerCommands.setBoolean(5, isStaff);

            playerCommands.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerSignText(String serverName, String playerName, String playerUUID, Coordinates coords, String lines, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement playerSignText = connection.prepareStatement("INSERT INTO player_sign_text" +
                     " (server_name, world, x, y, z, player_name, line, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            playerSignText.setString(1, serverName);
            playerSignText.setString(2, coords.getWorldName());
            playerSignText.setInt(3, coords.getX());
            playerSignText.setInt(4, coords.getY());
            playerSignText.setInt(5, coords.getZ());
            playerSignText.setString(6, playerName);
            playerSignText.setString(7, lines);
            playerSignText.setBoolean(8, isStaff);

            playerSignText.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerDeath(String serverName, String playerName, String playerUUID, int level, String cause, String who, Coordinates coordinates, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement playerDeath = connection.prepareStatement("INSERT INTO player_death" +
                     " (server_name, world, player_name, player_level, x, y, z, cause, by_who, is_staff) VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            playerDeath.setString(1, serverName);
            playerDeath.setString(2, coordinates.getWorldName());
            playerDeath.setString(3, playerName);
            playerDeath.setInt(4, level);
            playerDeath.setInt(5, coordinates.getX());
            playerDeath.setInt(6, coordinates.getY());
            playerDeath.setInt(7, coordinates.getZ());
            playerDeath.setString(8, cause);
            playerDeath.setString(9, who);
            playerDeath.setBoolean(10, isStaff);

            playerDeath.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerTeleport(String serverName, String playerName, String playerUUID, Coordinates oldCoords, Coordinates newCoords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement playerTeleport = connection.prepareStatement("INSERT INTO player_teleport" +
                     " (server_name, world, player_name, from_x, from_y, from_z, to_x, to_y, to_z, is_staff)" +
                     " VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            playerTeleport.setString(1, serverName);
            playerTeleport.setString(2, oldCoords.getWorldName());
            playerTeleport.setString(3, playerName);
            playerTeleport.setInt(4, oldCoords.getX());
            playerTeleport.setInt(5, oldCoords.getY());
            playerTeleport.setInt(6, oldCoords.getZ());
            playerTeleport.setInt(7, newCoords.getX());
            playerTeleport.setInt(8, newCoords.getY());
            playerTeleport.setInt(9, newCoords.getZ());
            playerTeleport.setBoolean(10, isStaff);

            playerTeleport.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerJoin(String serverName, String playerName, String playerUUID, Coordinates coords, InetSocketAddress ip, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement playerJoin = connection.prepareStatement("INSERT INTO player_join" +
                     " (server_name, world, player_name, x, y, z, ip, is_staff) VALUES(?,?,?,?,?,?,INET_ATON(?),?)")) {

            playerJoin.setString(1, serverName);
            playerJoin.setString(2, coords.getWorldName());
            playerJoin.setString(3, playerName);
            playerJoin.setInt(4, coords.getX());
            playerJoin.setInt(5, coords.getY());
            playerJoin.setInt(6, coords.getZ());
            if (this.options.isPlayerIPEnabled())
                playerJoin.setString(7, ip.getHostString());
            else
                playerJoin.setString(7, null);
            playerJoin.setBoolean(8, isStaff);

            playerJoin.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerLeave(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement playerLeave = connection.prepareStatement("INSERT INTO player_leave" +
                     " (server_name, world, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?)")) {

            playerLeave.setString(1, serverName);
            playerLeave.setString(2, coords.getWorldName());
            playerLeave.setString(3, playerName);
            playerLeave.setInt(4, coords.getX());
            playerLeave.setInt(5, coords.getY());
            playerLeave.setInt(6, coords.getZ());
            playerLeave.setBoolean(7, isStaff);

            playerLeave.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBlockPlace(String serverName, String playerName, String playerUUID, String block, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement blockPlace = connection.prepareStatement("INSERT INTO block_place" +
                     " (server_name, world, player_name, block, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            blockPlace.setString(1, serverName);
            blockPlace.setString(2, coords.getWorldName());
            blockPlace.setString(3, playerName);
            blockPlace.setString(4, block);
            blockPlace.setInt(5, coords.getX());
            blockPlace.setInt(6, coords.getY());
            blockPlace.setInt(7, coords.getZ());
            blockPlace.setBoolean(8, isStaff);

            blockPlace.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBlockBreak(String serverName, String playerName, String playerUUID, String blockName, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement blockBreak = connection.prepareStatement("INSERT INTO block_break" +
                     " (server_name, world, player_name, block, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            blockBreak.setString(1, serverName);
            blockBreak.setString(2, coords.getWorldName());
            blockBreak.setString(3, playerName);
            blockBreak.setString(4, blockName);
            blockBreak.setInt(5, coords.getX());
            blockBreak.setInt(6, coords.getY());
            blockBreak.setInt(7, coords.getZ());
            blockBreak.setBoolean(8, isStaff);

            blockBreak.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertTps(String serverName, double tpss) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement tps = connection.prepareStatement("INSERT INTO tps (server_name, tps) VALUES(?,?)")) {

            tps.setString(1, serverName);
            tps.setDouble(2, tpss);

            tps.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertRam(String serverName, long tm, long um, long fm) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement ram = connection.prepareStatement("INSERT INTO ram" +
                     " (server_name, total_memory, used_memory, free_memory) VALUES(?,?,?,?)")) {

            ram.setString(1, serverName);
            ram.setLong(2, tm);
            ram.setLong(3, um);
            ram.setLong(4, fm);

            ram.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerKick(String serverName, String playerName, String playerUUID, Coordinates coords, String reason, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement playerKick = connection.prepareStatement("INSERT INTO player_kick" +
                     " (server_name, world, player_name, x, y, z, reason, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            playerKick.setString(1, serverName);
            playerKick.setString(2, coords.getWorldName());
            playerKick.setString(3, playerName);
            playerKick.setInt(4, coords.getX());
            playerKick.setInt(5, coords.getY());
            playerKick.setInt(6, coords.getZ());
            playerKick.setString(7, reason);
            playerKick.setBoolean(8, isStaff);

            playerKick.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPortalCreate(String serverName, String worldName, String by) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement portalCreation = connection.prepareStatement("INSERT INTO portal_creation" +
                     " (server_name, world, caused_by) VALUES(?,?,?)")) {

            portalCreation.setString(1, serverName);
            portalCreation.setString(2, worldName);
            portalCreation.setString(3, by);

            portalCreation.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertLevelChange(String serverName, String playerName, String playerUUID, boolean isStaff) {

    }

    public void insertLevelChange(String serverName, String playerName, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement playerLevel = connection.prepareStatement("INSERT INTO player_level" +
                     " (server_name, player_name, is_staff) VALUES(?,?,?)")) {

            playerLevel.setString(1, serverName);
            playerLevel.setString(2, playerName);
            playerLevel.setBoolean(3, isStaff);

            playerLevel.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBucketFill(String serverName, String playerName, String playerUUID, String bucket, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement bucketPlace = connection.prepareStatement("INSERT INTO bucket_fill" +
                     " (server_name, world, player_name, bucket, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            bucketPlace.setString(1, serverName);
            bucketPlace.setString(2, coords.getWorldName());
            bucketPlace.setString(3, playerName);
            bucketPlace.setString(4, bucket);
            bucketPlace.setInt(5, coords.getX());
            bucketPlace.setInt(6, coords.getY());
            bucketPlace.setInt(7, coords.getZ());
            bucketPlace.setBoolean(8, isStaff);

            bucketPlace.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBucketEmpty(String serverName, String playerName, String playerUUID, String bucket, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement bucketPlace = connection.prepareStatement("INSERT INTO bucket_empty" +
                     " (server_name, world, player_name, bucket, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {


            bucketPlace.setString(1, serverName);
            bucketPlace.setString(2, coords.getWorldName());
            bucketPlace.setString(3, playerName);
            bucketPlace.setString(4, bucket);
            bucketPlace.setInt(5, coords.getX());
            bucketPlace.setInt(6, coords.getY());
            bucketPlace.setInt(7, coords.getZ());
            bucketPlace.setBoolean(8, isStaff);

            bucketPlace.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertAnvil(String serverName, String playerName, String playerUUID, String newName, boolean isStaff) {

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

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertServerStart(String serverName) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement serverStart = connection.prepareStatement("INSERT INTO server_start (server_name) VALUES(?)")) {

            serverStart.setString(1, serverName);

            serverStart.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertServerStop(String serverName) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement serverStop = connection.prepareStatement("INSERT INTO server_stop (server_name) VALUES(?)")) {

            serverStop.setString(1, serverName);

            serverStop.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertItemDrop(String serverName, String playerName, String playerUUID, String item, int amount, Coordinates coords, List<String> enchantment, String changedName, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement itemDrop = connection.prepareStatement("INSERT INTO item_drop" +
                     " (server_name, world, player_name, item, amount, x, y, z, enchantment, changed_name, is_staff)" +
                     " VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {

            itemDrop.setString(1, serverName);
            itemDrop.setString(2, coords.getWorldName());
            itemDrop.setString(3, playerName);
            itemDrop.setString(4, item);
            itemDrop.setInt(5, amount);
            itemDrop.setInt(6, coords.getX());
            itemDrop.setInt(7, coords.getY());
            itemDrop.setInt(8, coords.getZ());
            itemDrop.setString(9, enchantment.toString());
            itemDrop.setString(10, changedName);
            itemDrop.setBoolean(11, isStaff);

            itemDrop.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertEnchant(String serverName, String playerName, String playerUUID, List<String> enchantment, int enchantmentLevel,
                              String item, int cost, Coordinates coordinates, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement enchanting = connection.prepareStatement("INSERT INTO enchanting" +
                     " (server_name, world, player_name, x, y, z, enchantment, enchantment_level, item, cost, is_staff)" +
                     " VALUES(?,?,?,?,?,?,?,?,?,?,?)")) {

            enchanting.setString(1, serverName);
            enchanting.setString(2, coordinates.getWorldName());
            enchanting.setString(3, playerName);
            enchanting.setInt(4, coordinates.getX());
            enchanting.setInt(5, coordinates.getY());
            enchanting.setInt(6, coordinates.getZ());
            enchanting.setString(7, enchantment.toString());
            enchanting.setInt(8, enchantmentLevel);
            enchanting.setString(9, item);
            enchanting.setInt(10, cost);
            enchanting.setBoolean(11, isStaff);

            enchanting.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertBookEditing(String serverName, String playerName, String playerUUID, String worldName, int pages, List<String> content, String signedBy, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement enchanting = connection.prepareStatement("INSERT INTO book_editing" +
                     " (server_name, world, player_name, page_count, page_content, signed_by, is_staff) VALUES(?,?,?,?,?,?,?)")) {

            enchanting.setString(1, serverName);
            enchanting.setString(2, worldName);
            enchanting.setString(3, playerName);
            enchanting.setInt(4, pages);
            enchanting.setString(5, content.toString());
            enchanting.setString(6, signedBy);
            enchanting.setBoolean(7, isStaff);

            enchanting.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertAfk(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement afk = connection.prepareStatement("INSERT INTO afk" +
                     " (server_name, world, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?)")) {

            afk.setString(1, serverName);
            afk.setString(2, coords.getWorldName());
            afk.setString(3, playerName);
            afk.setInt(4, coords.getX());
            afk.setInt(5, coords.getY());
            afk.setInt(6, coords.getZ());
            afk.setBoolean(7, isStaff);

            afk.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertWrongPassword(String serverName, String playerName, String playerUUID, String worldName, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement wrongPassword = connection.prepareStatement("INSERT INTO wrong_password" +
                     " (server_name, world, player_name, is_staff) VALUES(?,?,?,?)")) {

            wrongPassword.setString(1, serverName);
            wrongPassword.setString(2, worldName);
            wrongPassword.setString(3, playerName);
            wrongPassword.setBoolean(4, isStaff);

            wrongPassword.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertItemPickup(String serverName, String playerName, String playerUUID, String item, int amount, Coordinates coords, String changedName, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement itemPickup = connection.prepareStatement("INSERT INTO item_pickup" +
                     " (server_name, world, player_name, item, amount, x, y, z, changed_name, is_staff) VALUES(?,?,?,?,?,?,?,?,?,?)")) {

            itemPickup.setString(1, serverName);
            itemPickup.setString(2, coords.getWorldName());
            itemPickup.setString(3, playerName);
            itemPickup.setString(4, item);
            itemPickup.setInt(5, amount);
            itemPickup.setInt(6, coords.getX());
            itemPickup.setInt(7, coords.getY());
            itemPickup.setInt(8, coords.getZ());
            itemPickup.setString(9, changedName);
            itemPickup.setBoolean(10, isStaff);

            itemPickup.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertFurnace(String serverName, String playerName, String playerUUID, String item, int amount, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement furnace = connection.prepareStatement("INSERT INTO furnace" +
                     " (server_name, world, player_name, item, amount, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            furnace.setString(1, serverName);
            furnace.setString(2, coords.getWorldName());
            furnace.setString(3, playerName);
            furnace.setString(4, item);
            furnace.setInt(5, amount);
            furnace.setInt(6, coords.getX());
            furnace.setInt(7, coords.getY());
            furnace.setInt(8, coords.getZ());
            furnace.setBoolean(9, isStaff);

            furnace.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertRCON(String serverName, String ip, String command) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement rCon = connection.prepareStatement("INSERT INTO rcon" +
                     " (server_name, ip, command) VALUES(?,?,?)")) {

            rCon.setString(1, serverName);
            rCon.setString(2, ip);
            rCon.setString(3, command);

            rCon.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertGameMode(String serverName, String playerName, String playerUUID, String theGameMode, String worldName, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement gameMode = connection.prepareStatement("INSERT INTO game_mode" +
                     " (server_name, world, player_name, game_mode, is_staff) VALUES(?,?,?,?,?)")) {

            gameMode.setString(1, serverName);
            gameMode.setString(2, worldName);
            gameMode.setString(3, playerName);
            gameMode.setString(4, theGameMode);
            gameMode.setBoolean(5, isStaff);

            gameMode.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerCraft(String serverName, String playerName, String playerUUID, String item, int amount, Coordinates coordinates, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement craft = connection.prepareStatement("INSERT INTO crafting" +
                     " (server_name, world, player_name, item, amount, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            craft.setString(1, serverName);
            craft.setString(2, coordinates.getWorldName());
            craft.setString(3, playerName);
            craft.setString(4, item);
            craft.setInt(5, amount);
            craft.setInt(6, coordinates.getX());
            craft.setInt(7, coordinates.getY());
            craft.setInt(8, coordinates.getZ());
            craft.setBoolean(9, isStaff);

            craft.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertVault(String serverName, String playerName, String playerUUID, double oldBal, double newBal, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement vault = connection.prepareStatement("INSERT INTO vault" +
                     " (server_name, player_name, old_balance, new_balance, is_staff) VALUES(?,?,?,?,?)")) {

            vault.setString(1, serverName);
            vault.setString(2, playerName);
            vault.setDouble(3, oldBal);
            vault.setDouble(4, newBal);
            vault.setBoolean(5, isStaff);

            vault.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPlayerRegistration(String serverName, String playerName, String playerUUID, String joinDate) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement register = connection.prepareStatement("INSERT INTO registration" +
                     " (server_name, player_name, player_uuid, join_date) VALUES(?,?,?,?)")) {

            register.setString(1, serverName);
            register.setString(2, playerName);
            register.setString(3, playerUUID);
            register.setString(4, joinDate);

            register.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertPrimedTnt(String serverName, String playerName, String playerUUID, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement primedTNT = connection.prepareStatement("INSERT INTO primed_tnt" +
                     " (server_name, world, player_uuid, player_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?)")) {

            primedTNT.setString(1, serverName);
            primedTNT.setString(2, coords.getWorldName());
            primedTNT.setString(3, playerUUID);
            primedTNT.setString(4, playerName);
            primedTNT.setInt(5, coords.getX());
            primedTNT.setInt(6, coords.getY());
            primedTNT.setInt(7, coords.getZ());
            primedTNT.setBoolean(8, isStaff);

            primedTNT.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertLiteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement liteBans = connection.prepareStatement("INSERT INTO litebans" +
                     " (server_name, sender, command, onwho, reason, duration, is_silent) VALUES(?,?,?,?,?,?,?)")) {

            liteBans.setString(1, serverName);
            liteBans.setString(2, executor);
            liteBans.setString(3, command);
            liteBans.setString(4, onWho);
            liteBans.setString(5, duration);
            liteBans.setString(6, reason);
            liteBans.setBoolean(7, isSilent);

            liteBans.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertAdvanceBanData(String serverName, String type, String executor, String executedOn, String reason, long expirationDate) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement advancedBan = connection.prepareStatement("INSERT INTO advanced_ban" +
                     " (server_name, type, executor, executed_on, reason, expiration_date) VALUES(?,?,?,?,?,?)")) {

            advancedBan.setString(1, serverName);
            advancedBan.setString(2, type);
            advancedBan.setString(3, executor);
            advancedBan.setString(4, executedOn);
            advancedBan.setString(5, reason);
            advancedBan.setLong(6, expirationDate);

            advancedBan.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertCommandBlock(String serverName, String msg) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement commandBlock = connection.prepareStatement("INSERT INTO command_block" +
                     " (server_name, command) VALUES(?,?)")) {

            commandBlock.setString(1, serverName);
            commandBlock.setString(2, msg);

            commandBlock.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertWoodStripping(String serverName, String playerName, String playerUUID, String logName, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement woodStripping = connection.prepareStatement("INSERT INTO wood_stripping" +
                     " (server_name, world, uuid, player_name, log_name, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            woodStripping.setString(1, serverName);
            woodStripping.setString(2, coords.getWorldName());
            woodStripping.setString(3, playerUUID);
            woodStripping.setString(4, playerName);
            woodStripping.setString(5, logName);
            woodStripping.setInt(6, coords.getX());
            woodStripping.setInt(7, coords.getY());
            woodStripping.setInt(8, coords.getZ());
            woodStripping.setBoolean(9, isStaff);

            woodStripping.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertChestInteraction(String serverName, String playerName, String playerUUID, Coordinates coords, String[] items, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement chestInteraction = connection.prepareStatement("INSERT INTO chest_interaction" +
                     " (server_name, world, player_uuid, player_name, x, y, z, items, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            chestInteraction.setString(1, serverName);
            chestInteraction.setString(2, coords.getWorldName());
            chestInteraction.setString(3, playerUUID);
            chestInteraction.setString(4, playerName);
            chestInteraction.setInt(5, coords.getX());
            chestInteraction.setInt(6, coords.getY());
            chestInteraction.setInt(7, coords.getZ());
            chestInteraction.setString(8, Arrays.toString(items));
            chestInteraction.setBoolean(9, isStaff);

            chestInteraction.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertEntityDeath(String serverName, String playerName, String playerUUID, String mob, Coordinates coords, boolean isStaff) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement entityDeath = connection.prepareStatement("INSERT INTO entity_death" +
                     " (server_name, world, player_uuid, player_name, mob, x, y, z, is_staff) VALUES(?,?,?,?,?,?,?,?,?)")) {

            entityDeath.setString(1, serverName);
            entityDeath.setString(2, coords.getWorldName());
            entityDeath.setString(3, playerUUID);
            entityDeath.setString(4, playerName);
            entityDeath.setString(5, mob);
            entityDeath.setInt(6, coords.getX());
            entityDeath.setInt(7, coords.getY());
            entityDeath.setInt(8, coords.getZ());
            entityDeath.setBoolean(9, isStaff);

            entityDeath.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }


    @Override
    public void insertConsoleCommand(String serverName, String msg) {

        try (final Connection connection = this.getConnection();
             final PreparedStatement consoleCommands = connection.prepareStatement("INSERT INTO console_commands" +
                     " (server_name, command) VALUES(?,?)")) {

            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, msg);

            consoleCommands.executeUpdate();

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void insertServerReload(String serverName, String playerName, boolean isStaff) {

    }

    @Override
    public void insertPlayerLogin(String serverName, String playerName, String toString,
                                  InetSocketAddress playerIP,
                                  boolean hasPermission) {

    }

    @Override
    public void disconnect() {
        //TODO implement this method
    }

    @Override
    public List<PlayerChat> getPlayerChatByPlayerName(String playerName, int offset, int limit) {
        return null;
    }

    @Override
    public Long getPlayerChatCount(String playerName) {
        return 0L;
    }

}
