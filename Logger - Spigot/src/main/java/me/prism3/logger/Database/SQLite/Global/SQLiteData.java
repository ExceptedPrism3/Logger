package me.prism3.logger.Database.SQLite.Global;

import me.prism3.logger.API.AuthMeUtil;
import me.prism3.logger.API.EssentialsUtil;
import me.prism3.logger.API.VaultUtil;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.Data;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.world.PortalCreateEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class SQLiteData {

    private static Main plugin;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSSXXX");

    public SQLiteData(Main plugin) {
        SQLiteData.plugin = plugin;
    }

    public void createTable() {

        final PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin, playerLeave,
                playerDeath, playerTeleport, blockPlace, blockBreak, TPS, RAM, playerKick, portalCreation, playerLevel,
                bucketFill, bucketEmpty, anvil, serverStart, serverStop, itemDrop, enchant, bookEditing, afk,
                wrongPassword, itemPickup, furnace, rcon, gameMode, craft, vault, registration;

        try {

            // Player Side Part
            playerChat = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat " +
                    "(Server_Name TEXT(30),Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY , World TEXT(100), Player_Name TEXT(20)," +
                    "Message TEXT(256), Is_Staff INTEGER)");

            playerCommands = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100),Player_Name TEXT(40)," +
                    "Command TEXT(100),Is_Staff INTEGER)");

            playerSignText = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Sign_Text" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(50)," +
                    " X INTEGER, Y INTEGER, Z INTEGER," +
                    "Player_Name TEXT(40), Line TEXT(100), Is_Staff INTEGER)");

            playerJoin = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Join" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'now'))  PRIMARY KEY , World TEXT(50)," +
                    "X INTEGER, Y INTEGER, Z INTEGER, IP TEXT, Is_Staff INTEGER)");

            playerLeave = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS  Player_Leave " +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(50), X INTEGER, Y INTEGER, Z INTEGER , Player_Name TEXT(40), Is_Staff INTEGER)");

            playerDeath = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Death" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "Player_Level INTEGER, X INTEGER, Y INTEGER, Z INTEGER, Cause TEXT(100), By_Who TEXT(100), Is_Staff INTEGER)");

            playerTeleport = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Teleport" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "From_X INTEGER, From_Y INTEGER, From_Z INTEGER, To_X INTEGER, To_Y INTEGER, To_Z INTEGER, Is_Staff INTEGER)");

            blockPlace = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Place" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "Block TEXT(40),X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            blockBreak = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Break" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY , World TEXT(100), Player_Name TEXT(40)," +
                    "Block TEXT(40), X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            playerKick = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Kick" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY,World TEXT(100), Player_Name TEXT(40)," +
                    " X INTEGER, Y INTEGER, Z INTEGER, Reason TEXT(50), Is_Staff INTEGER )");

            playerLevel = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE  IF NOT EXISTS Player_Level" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, Player_Name TEXT(40), Is_Staff INTEGER )");

            bucketFill = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Bucket_Fill" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(100), Player_Name TEXT(40)," +
                    "Bucket TEXT(40), X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            bucketEmpty = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Bucket_Empty" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(100), Player_Name TEXT(40)," +
                    "Bucket TEXT(40), X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            anvil = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Anvil" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,Player_Name TEXT(40), New_Name TEXT(100), Is_Staff INTEGER)");

            itemDrop = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Item_Drop" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "Item TEXT(50), Amount INTEGER, X INTEGER, Y INTEGER, Z INTEGER, Enchantment TEXT(50), Changed_Name TEXT(50), Is_Staff INTEGER)");

            enchant = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Enchanting" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "X INTEGER, Y INTEGER, Z INTEGER, Enchantment TEXT(50), Enchantment_Level INTEGER, Item TEXT(50)," +
                    " Cost INTEGER, Is_Staff INTEGER)");

            bookEditing = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Book_Editing" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "Page_Count INTEGER, Page_Content TEXT(100), Signed_by TEXT(25), Is_Staff INTEGER)");

            itemPickup = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Item_Pickup" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "Item TEXT(50), Amount INTEGER, X INTEGER, Y INTEGER, Z INTEGER, Changed_Name TEXT(100), Is_Staff INTEGER)");

            furnace = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Furnace" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "Item TEXT(50), Amount INTEGER, X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            gameMode = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Game_Mode" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "Game_Mode TEXT(15), Is_Staff INTEGER)");

            craft = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Crafting" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                    "Item TEXT(50), Amount INTEGER, X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            registration = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Registration" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, Player_Name TEXT(40)," +
                    "Player_UUID INTEGER, Join_Date TEXT(30))");

            // Server Side Part
            serverStart = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY)");

            serverStop = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE  IF NOT EXISTS Server_Stop" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY )");

            consoleCommands = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Console_Commands" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY,Command TEXT(256))");

            RAM = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY," +
                    " Total_Memory INTEGER, Used_Memory INTEGER, Free_Memory INTEGER)");

            TPS = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS TPS" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, TPS INTEGER )");

            portalCreation = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Portal_Creation" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(50), Caused_By TEXT(50))");

            rcon = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RCON" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, IP TEXT, Command TEXT(100))");

            // Extras Side Part
            if (EssentialsUtil.getEssentialsAPI() != null) {

                afk = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS AFK" +
                        "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                        "X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

                afk.executeUpdate();
            }

            if (AuthMeUtil.getAuthMeAPI() != null) {

                wrongPassword = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Wrong_Password" +
                        "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(40)," +
                        "Is_Staff INTEGER)");

                wrongPassword.executeUpdate();
            }

            if (VaultUtil.getVaultAPI() && VaultUtil.getVault() != null) {

                vault = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Vault" +
                        "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, Player_Name TEXT(30), Old_Balance REAL(200)," +
                        " New_Balance REAL(200), Is_Staff INTEGER)");

                vault.executeUpdate();
            }

            playerChat.executeUpdate();
            playerChat.close();
            playerCommands.executeUpdate();
            playerCommands.close();
            playerSignText.executeUpdate();
            playerSignText.close();
            playerDeath.executeUpdate();
            playerDeath.close();
            playerTeleport.executeUpdate();
            playerTeleport.close();
            playerJoin.executeUpdate();
            playerJoin.close();
            playerLeave.executeUpdate();
            playerLeave.close();
            blockPlace.executeUpdate();
            blockPlace.close();
            blockBreak.executeUpdate();
            blockBreak.close();
            playerKick.executeUpdate();
            playerKick.close();
            playerLevel.executeUpdate();
            playerLevel.close();
            bucketFill.executeUpdate();
            bucketFill.close();
            bucketEmpty.executeUpdate();
            bucketEmpty.close();
            anvil.executeUpdate();
            anvil.close();
            itemDrop.executeUpdate();
            itemDrop.close();
            enchant.executeUpdate();
            enchant.close();
            bookEditing.executeUpdate();
            bookEditing.close();
            itemPickup.executeUpdate();
            itemPickup.close();
            furnace.executeUpdate();
            furnace.close();
            gameMode.executeUpdate();
            gameMode.close();
            craft.executeUpdate();
            craft.close();
            registration.executeUpdate();
            registration.close();

            serverStart.executeUpdate();
            serverStart.close();
            serverStop.executeUpdate();
            serverStop.close();
            consoleCommands.executeUpdate();
            consoleCommands.close();
            RAM.executeUpdate();
            RAM.close();
            TPS.executeUpdate();
            TPS.close();
            portalCreation.executeUpdate();
            portalCreation.close();
            rcon.executeUpdate();
            rcon.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerChat(String serverName, Player player, String message, boolean staff) {

        try {
            final PreparedStatement playerChat = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Chat (Server_Name, Date, World, Player_Name, Message, Is_Staff) VALUES (?,?,?,?,?,?)");
            playerChat.setString(1, serverName);
            playerChat.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerChat.setString(3, player.getWorld().getName());
            playerChat.setString(4, player.getName());
            playerChat.setString(5, message);
            playerChat.setBoolean(6, staff);

            playerChat.executeUpdate();
            playerChat.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerCommands(String serverName, Player player, String command, boolean staff) {
        try {
            final PreparedStatement playerCommands = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Commands (Server_Name, Date, World, Player_Name, Command, Is_Staff) VALUES (?,?,?,?,?,?)");
            playerCommands.setString(1, serverName);
            playerCommands.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerCommands.setString(3, player.getWorld().getName());
            playerCommands.setString(4, player.getName());
            playerCommands.setString(5, command);
            playerCommands.setBoolean(6, staff);

            playerCommands.executeUpdate();
            playerCommands.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }


    public static void insertConsoleCommands(String serverName, String command) {
        try {
            final PreparedStatement consoleCommands = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Console_Commands (Server_Name, Date, Command) VALUES (?,?,?)");
            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            consoleCommands.setString(3, command);

            consoleCommands.executeUpdate();
            consoleCommands.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerSignText(String serverName, Player player, String lines, boolean isStaff) {
        try {
            final PreparedStatement playerSignText = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Sign_Text (Server_Name, Date, World, X, Y, Z, Player_Name, Line, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
            playerSignText.setString(1, serverName);
            playerSignText.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerSignText.setString(3, player.getWorld().getName());
            playerSignText.setInt(4, player.getLocation().getBlockX());
            playerSignText.setInt(5, player.getLocation().getBlockY());
            playerSignText.setInt(6, player.getLocation().getBlockZ());
            playerSignText.setString(7, player.getName());
            playerSignText.setString(8, lines);
            playerSignText.setBoolean(9, isStaff);

            playerSignText.executeUpdate();
            playerSignText.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerDeath(String serverName, Player player, String cause, String by_who, boolean isStaff) {
        try {
            final PreparedStatement playerDeath = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Death (Server_Name, Date, World, Player_Name, Player_Level, X, Y, Z, Cause, By_Who, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            playerDeath.setString(1, serverName);
            playerDeath.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerDeath.setString(3, player.getWorld().getName());
            playerDeath.setString(4, player.getName());
            playerDeath.setInt(5, player.getLevel());
            playerDeath.setInt(6, player.getLocation().getBlockX());
            playerDeath.setInt(7, player.getLocation().getBlockY());
            playerDeath.setInt(8, player.getLocation().getBlockZ());
            playerDeath.setString(9, cause);
            playerDeath.setString(10, by_who);
            playerDeath.setBoolean(11, isStaff);

            playerDeath.executeUpdate();
            playerDeath.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerTeleport(String serverName, Player player, Location from, Location to, boolean isStaff) {
        try {
            final PreparedStatement playerTeleport = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Teleport (Server_Name, Date, World, Player_Name, From_X, From_Y, From_Z, To_X, To_Y, To_Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            playerTeleport.setString(1, serverName);
            playerTeleport.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerTeleport.setString(3, player.getWorld().getName());
            playerTeleport.setString(4, player.getName());
            playerTeleport.setInt(5, from.getBlockX());
            playerTeleport.setInt(6, from.getBlockY());
            playerTeleport.setInt(7, from.getBlockZ());
            playerTeleport.setInt(8, to.getBlockX());
            playerTeleport.setInt(9, to.getBlockY());
            playerTeleport.setInt(10, to.getBlockZ());
            playerTeleport.setBoolean(11, isStaff);

            playerTeleport.executeUpdate();
            playerTeleport.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerJoin(String serverName, Player player, boolean isStaff) {
        try {
            final PreparedStatement playerJoin = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Join (Server_Name, World, X, Y, Z, IP, Is_Staff) VALUES (?,?,?,?,?,?,?)");
            playerJoin.setString(1, serverName);
            playerJoin.setString(2, player.getWorld().getName());
            playerJoin.setInt(3, player.getLocation().getBlockX());
            playerJoin.setInt(4, player.getLocation().getBlockY());
            playerJoin.setInt(5, player.getLocation().getBlockZ());
            if (plugin.getConfig().getBoolean("Player-Join.Player-IP")) {
                playerJoin.setString(6, Objects.requireNonNull(player.getAddress()).getHostString());
            } else {
                playerJoin.setString(6, null);
            }
            playerJoin.setBoolean(7, isStaff);

            playerJoin.executeUpdate();
            playerJoin.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerLeave(String serverName, Player player, boolean isStaff) {
        try {
            final PreparedStatement playerLeave = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Leave (Server_Name, Date, World, X, Y, Z, Player_Name, Is_Staff) VALUES (?,?,?,?,?,?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerLeave.setString(3, player.getWorld().getName());
            playerLeave.setInt(4, player.getLocation().getBlockX());
            playerLeave.setInt(5, player.getLocation().getBlockY());
            playerLeave.setInt(6, player.getLocation().getBlockZ());
            playerLeave.setString(7, player.getName());
            playerLeave.setBoolean(8, isStaff);

            playerLeave.executeUpdate();
            playerLeave.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBlockPlace(String serverName, Player player, Material block, boolean isStaff) {
        try {
            final PreparedStatement blockPlace = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Block_Place (Server_Name, Date, World, Player_Name,Block, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
            blockPlace.setString(1, serverName);
            blockPlace.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            blockPlace.setString(3, player.getWorld().getName());
            blockPlace.setString(4, player.getName());
            blockPlace.setString(5, block.toString());
            blockPlace.setInt(6, player.getLocation().getBlockX());
            blockPlace.setInt(7, player.getLocation().getBlockY());
            blockPlace.setInt(8, player.getLocation().getBlockZ());
            blockPlace.setBoolean(9, isStaff);

            blockPlace.executeUpdate();
            blockPlace.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBlockBreak(String serverName, Player player, Material block, boolean isStaff) {
        try {
            final PreparedStatement blockBreak = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Block_Break (Server_Name, Date, World, Player_Name, Block, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
            blockBreak.setString(1, serverName);
            blockBreak.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            blockBreak.setString(3, player.getWorld().getName());
            blockBreak.setString(4, player.getName());
            blockBreak.setString(5, block.toString());
            blockBreak.setInt(6, player.getLocation().getBlockX());
            blockBreak.setInt(7, player.getLocation().getBlockY());
            blockBreak.setInt(8, player.getLocation().getBlockZ());
            blockBreak.setBoolean(9, isStaff);

            blockBreak.executeUpdate();
            blockBreak.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertTPS(String serverName, double tps) {
        try {
            final PreparedStatement tpsStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO TPS (Server_Name, Date, TPS) VALUES (?,?,?)");

            tpsStatement.setString(1, serverName);
            tpsStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            tpsStatement.setDouble(3, tps);

            tpsStatement.executeUpdate();
            tpsStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertRAM(String serverName, long totalMemory, long usedMemory, long freeMemory) {
        try {
            final PreparedStatement ramStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO RAM (Server_Name, Date, Total_Memory, Used_Memory, Free_Memory) VALUES (?,?,?,?,?)");
            ramStatement.setString(1, serverName);
            ramStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            ramStatement.setLong(3, totalMemory);
            ramStatement.setLong(4, usedMemory);
            ramStatement.setLong(5, freeMemory);

            ramStatement.executeUpdate();
            ramStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerKick(String serverName, Player player, String reason, boolean isStaff) {
        try {
            final PreparedStatement playerKickStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Kick (Server_Name, Date, World, Player_Name, X, Y, Z, Reason, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
            playerKickStatement.setString(1, serverName);
            playerKickStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerKickStatement.setString(3, player.getWorld().getName());
            playerKickStatement.setString(4, player.getName());
            playerKickStatement.setInt(5, player.getLocation().getBlockX());
            playerKickStatement.setInt(6, player.getLocation().getBlockY());
            playerKickStatement.setInt(7, player.getLocation().getBlockZ());
            playerKickStatement.setString(8, reason);
            playerKickStatement.setBoolean(9, isStaff);

            playerKickStatement.executeUpdate();
            playerKickStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPortalCreate(String serverName, String worldName, PortalCreateEvent.CreateReason reason) {
        try {
            final PreparedStatement portalCreateStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Portal_Creation (Server_Name, Date, World, Caused_By) VALUES (?,?,?,?)");
            portalCreateStatement.setString(1, serverName);
            portalCreateStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            portalCreateStatement.setString(3, worldName);
            portalCreateStatement.setString(4, reason.toString());

            portalCreateStatement.executeUpdate();
            portalCreateStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertLevelChange(String serverName, Player player, boolean isStaff) {
        try {
            final PreparedStatement levelChangeStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Level (Server_Name, Date, Player_Name, Is_Staff) VALUES (?,?,?,?)");
            levelChangeStatement.setString(1, serverName);
            levelChangeStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            levelChangeStatement.setString(3, player.getName());
            levelChangeStatement.setBoolean(4, isStaff);

            levelChangeStatement.executeUpdate();
            levelChangeStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBucketFill(String serverName, Player player, String bucket, boolean isStaff) {
        try {
            final PreparedStatement bucketFillStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Bucket_Fill (Server_Name, Date, World, Player_Name, Bucket, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
            bucketFillStatement.setString(1, serverName);
            bucketFillStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            bucketFillStatement.setString(3, player.getWorld().getName());
            bucketFillStatement.setString(4, player.getName());
            bucketFillStatement.setString(5, bucket);
            bucketFillStatement.setInt(6, player.getLocation().getBlockX());
            bucketFillStatement.setInt(7, player.getLocation().getBlockY());
            bucketFillStatement.setInt(8, player.getLocation().getBlockZ());
            bucketFillStatement.setBoolean(9, isStaff);

            bucketFillStatement.executeUpdate();
            bucketFillStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBucketEmpty(String serverName, Player player, String bucket, boolean isStaff) {
        try {
            final PreparedStatement bucketEmptyStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Bucket_Empty (Server_Name, Date, World, Player_Name, Bucket, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
            bucketEmptyStatement.setString(1, serverName);
            bucketEmptyStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            bucketEmptyStatement.setString(3, player.getWorld().getName());
            bucketEmptyStatement.setString(4, player.getName());
            bucketEmptyStatement.setString(5, bucket);
            bucketEmptyStatement.setInt(6, player.getLocation().getBlockX());
            bucketEmptyStatement.setInt(7, player.getLocation().getBlockY());
            bucketEmptyStatement.setInt(8, player.getLocation().getBlockZ());
            bucketEmptyStatement.setBoolean(9, isStaff);

            bucketEmptyStatement.executeUpdate();
            bucketEmptyStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertAnvil(String serverName, Player player, String newName, boolean isStaff) {
        try {
            final PreparedStatement anvilStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Anvil (Server_Name, Date, Player_Name,  New_Name, Is_Staff) VALUES (?,?,?,?,?)");
            anvilStatement.setString(1, serverName);
            anvilStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            anvilStatement.setString(3, player.getName());
            anvilStatement.setString(4, newName);
            anvilStatement.setBoolean(5, isStaff);

            anvilStatement.executeUpdate();
            anvilStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertServerStart(String serverName) {
        try {
            final PreparedStatement serverStartStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Server_Start (Server_Name, Date) VALUES (?,?)");
            serverStartStatement.setString(1, serverName);
            serverStartStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

            serverStartStatement.executeUpdate();
            serverStartStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertServerStop(String serverName) {
        try {
            final PreparedStatement serverStopStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Server_Stop (Server_Name, Date) VALUES (?,?)");
            serverStopStatement.setString(1, serverName);
            serverStopStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

            serverStopStatement.executeUpdate();
            serverStopStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertItemDrop(String serverName, Player player, String item, int amount, int x, int y, int z, List<String> enchantment, String changedName, boolean isStaff) {
        try {
            final PreparedStatement itemDropStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Item_Drop (Server_Name, Date, World, Player_Name, Item, Amount, X, Y, Z, Enchantment, Changed_Name, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            itemDropStatement.setString(1, serverName);
            itemDropStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            itemDropStatement.setString(3, player.getWorld().getName());
            itemDropStatement.setString(4, player.getName());
            itemDropStatement.setString(5, item);
            itemDropStatement.setInt(6, amount);
            itemDropStatement.setInt(7, x);
            itemDropStatement.setInt(8, y);
            itemDropStatement.setInt(9, z);
            itemDropStatement.setString(10, String.valueOf(enchantment));
            itemDropStatement.setString(11, changedName);
            itemDropStatement.setBoolean(12, isStaff);

            itemDropStatement.executeUpdate();
            itemDropStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertEnchant(String serverName, Player player, List<String> enchantment, int enchantmentLevel, String item, int cost, boolean isStaff) {
        try {
            final PreparedStatement enchantStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Enchanting (Server_Name, Date, World, Player_Name, X, Y, Z, Enchantment, Enchantment_Level, Item, Cost, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            enchantStatement.setString(1, serverName);
            enchantStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            enchantStatement.setString(3, player.getWorld().getName());
            enchantStatement.setString(4, player.getName());
            enchantStatement.setInt(5, player.getLocation().getBlockX());
            enchantStatement.setInt(6, player.getLocation().getBlockY());
            enchantStatement.setInt(7, player.getLocation().getBlockZ());
            enchantStatement.setString(8, String.valueOf(enchantment));
            enchantStatement.setInt(9, enchantmentLevel);
            enchantStatement.setString(10, item);
            enchantStatement.setInt(11, cost);
            enchantStatement.setBoolean(12, isStaff);

            enchantStatement.executeUpdate();
            enchantStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertBook(String serverName, Player player, int pages, List<String> content, String signed_by, boolean staff){
        try {
            final PreparedStatement book_editing = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Book_Editing (Server_Name, Date, World, Player_Name, Page_Count, Page_Content, Signed_by, Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            book_editing.setString(1, serverName);
            book_editing.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            book_editing.setString(3, player.getWorld().getName());
            book_editing.setString(4, player.getName());
            book_editing.setInt(5, pages);
            book_editing.setString(6, String.valueOf(content));
            book_editing.setString(7, signed_by);
            book_editing.setBoolean(8, staff);

            book_editing.executeUpdate();
            book_editing.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void insertAFK(String serverName, Player player, boolean isStaff) {

        if (EssentialsUtil.getEssentialsAPI() != null) {

            try {
                final PreparedStatement enchantStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO AFK (Server_Name, Date, World, Player_Name, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?)");
                enchantStatement.setString(1, serverName);
                enchantStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
                enchantStatement.setString(3, player.getWorld().getName());
                enchantStatement.setString(4, player.getName());
                enchantStatement.setInt(5, player.getLocation().getBlockX());
                enchantStatement.setInt(6, player.getLocation().getBlockY());
                enchantStatement.setInt(7, player.getLocation().getBlockZ());
                enchantStatement.setBoolean(8, isStaff);

                enchantStatement.executeUpdate();
                enchantStatement.close();

            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void insertWrongPassword(String serverName, Player player, boolean isStaff) {

        try {
            final PreparedStatement wrongPasswordStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Wrong_Password (Server_Name, Date, World, Player_Name, Is_Staff) VALUES (?,?,?,?,?)");
            wrongPasswordStatement.setString(1, serverName);
            wrongPasswordStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            wrongPasswordStatement.setString(3, player.getWorld().getName());
            wrongPasswordStatement.setString(4, player.getName());
            wrongPasswordStatement.setBoolean(5, isStaff);

            wrongPasswordStatement.executeUpdate();
            wrongPasswordStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }

    }

    public static void insertItemPickup(String serverName, Player player, Material item, int amount, int x, int y, int z, String changed_name, boolean isStaff) {
        try {
            final PreparedStatement itemPickupStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Item_Pickup (Server_Name, Date, World, Player_Name, Item, Amount, X, Y, Z, Changed_Name, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            itemPickupStatement.setString(1, serverName);
            itemPickupStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            itemPickupStatement.setString(3, player.getWorld().getName());
            itemPickupStatement.setString(4, player.getName());
            itemPickupStatement.setString(5, String.valueOf(item));
            itemPickupStatement.setInt(6, amount);
            itemPickupStatement.setInt(7, x);
            itemPickupStatement.setInt(8, y);
            itemPickupStatement.setInt(9, z);
            itemPickupStatement.setString(10, changed_name);
            itemPickupStatement.setBoolean(11, isStaff);

            itemPickupStatement.executeUpdate();
            itemPickupStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertFurnace(String serverName, Player player, Material item, int amount, int x, int y, int z, boolean isStaff) {
        try {
            final PreparedStatement furnaceStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Furnace (Server_Name, Date, World, Player_Name, Item, Amount, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?)");
            furnaceStatement.setString(1, serverName);
            furnaceStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            furnaceStatement.setString(3, player.getWorld().getName());
            furnaceStatement.setString(4, player.getName());
            furnaceStatement.setString(5, String.valueOf(item));
            furnaceStatement.setInt(6, amount);
            furnaceStatement.setInt(7, x);
            furnaceStatement.setInt(8, y);
            furnaceStatement.setInt(9, z);
            furnaceStatement.setBoolean(10, isStaff);

            furnaceStatement.executeUpdate();
            furnaceStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertRcon(String serverName, String IP, String command) {
        try {
            final PreparedStatement rcon = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO RCON (Server_Name, Date, IP, Command) VALUES (?,?,?,?)");
            rcon.setString(1, serverName);
            rcon.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            rcon.setString(3, IP);
            rcon.setString(4, command);

            rcon.executeUpdate();
            rcon.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertGameMode(String serverName, String world, String playerName, String gameMode, boolean staff) {
        try {
            final PreparedStatement game_Mode = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Game_Mode (Server_Name, Date, World, Player_Name, Game_Mode, Is_Staff) VALUES (?,?,?,?,?,?)");
            game_Mode.setString(1, serverName);
            game_Mode.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            game_Mode.setString(3, world);
            game_Mode.setString(4, playerName);
            game_Mode.setString(5, gameMode);
            game_Mode.setBoolean(6, staff);

            game_Mode.executeUpdate();
            game_Mode.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerCraft(String serverName, Player player, String item, int amount, int x, int y, int z, boolean isStaff) {
        try {
            final PreparedStatement craftStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Crafting (Server_Name, Date, World, Player_Name, Item, Amount, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?)");
            craftStatement.setString(1, serverName);
            craftStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            craftStatement.setString(3, player.getWorld().getName());
            craftStatement.setString(4, player.getName());
            craftStatement.setString(5, item);
            craftStatement.setInt(6, amount);
            craftStatement.setInt(7, x);
            craftStatement.setInt(8, y);
            craftStatement.setInt(9, z);
            craftStatement.setBoolean(10, isStaff);

            craftStatement.executeUpdate();
            craftStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertVault(String serverName, Player player, double oldBal, double newBal, boolean isStaff) {
        try {
            final PreparedStatement vault = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Vault (Server_Name, Date, Player_Name, Old_Balance, New_Balance, Is_Staff) VALUES (?,?,?,?,?,?)");
            vault.setString(1, serverName);
            vault.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            vault.setString(3, player.getName());
            vault.setDouble(4, oldBal);
            vault.setDouble(5, newBal);
            vault.setBoolean(6, isStaff);

            vault.executeUpdate();
            vault.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertRegistration(String serverName, Player player, String joinDate) {
        try {
            final PreparedStatement register = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Registration (Server_Name, Date, Player_Name, Player_UUID, Join_Date) VALUES (?,?,?,?,?)");
            register.setString(1, serverName);
            register.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            register.setString(3, player.getName());
            register.setString(4, String.valueOf(player.getUniqueId()));
            register.setString(5, joinDate);

            register.executeUpdate();
            register.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }


    public void emptyTable() {

        if (Data.sqliteDataDel <= 0) return;

        try{

            // Player Side Part
            final PreparedStatement player_Chat = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Chat WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Commands = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Commands WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Sign_Text = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Sign_Text WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Join = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Join WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Leave = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Leave WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Kick = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Kick WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Death = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Death WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Teleport = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Teleport WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Level = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Level WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement block_Place = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Block_Place WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement block_Break = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Block_Break WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement bucket_Fill = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Bucket_Fill WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement bucket_Empty = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Bucket_Empty WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement anvil = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Anvil WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement item_Drop = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Item_Drop WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement enchanting = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Enchanting WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement book_Editing = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Book_Editing WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement item_pickup = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Item_Pickup WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement furnace = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Furnace WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement gameMode = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Game_Mode WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement craft = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Crafting WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement register = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Registration WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            // Server Side Part
            final PreparedStatement server_Start = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Server_Start WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement server_Stop = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Server_Stop WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement console_Commands = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Console_Commands WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement RAM = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM RAM WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement TPS = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM TPS WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement portal_Creation = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Portal_Creation WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement rcon = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM RCON WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            // Extras Side Part
            if (EssentialsUtil.getEssentialsAPI() != null) {

                final PreparedStatement afk = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM AFK WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

                afk.executeUpdate();
                afk.close();

            }

            if (AuthMeUtil.getAuthMeAPI() != null) {

                final PreparedStatement wrong_Password = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Wrong_Password WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

                wrong_Password.executeUpdate();
                wrong_Password.close();

            }

            if (VaultUtil.getVaultAPI() && VaultUtil.getVault() != null) {

                final PreparedStatement vault = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Vault WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

                vault.executeUpdate();
                vault.close();

            }

            player_Chat.executeUpdate();
            player_Chat.close();
            player_Commands.executeUpdate();
            player_Commands.close();
            player_Sign_Text.executeUpdate();
            player_Sign_Text.close();
            player_Join.executeUpdate();
            player_Join.close();
            player_Leave.executeUpdate();
            player_Leave.close();
            player_Kick.executeUpdate();
            player_Kick.close();
            player_Death.executeUpdate();
            player_Death.close();
            player_Teleport.executeUpdate();
            player_Teleport.close();
            player_Level.executeUpdate();
            player_Level.close();
            block_Place.executeUpdate();
            block_Place.close();
            block_Break.executeUpdate();
            block_Break.close();
            bucket_Fill.executeUpdate();
            bucket_Fill.close();
            bucket_Empty.executeUpdate();
            bucket_Empty.close();
            anvil.executeUpdate();
            anvil.close();
            item_Drop.executeUpdate();
            item_Drop.close();
            enchanting.executeUpdate();
            enchanting.close();
            book_Editing.executeUpdate();
            book_Editing.close();
            item_pickup.executeUpdate();
            item_pickup.close();
            furnace.executeUpdate();
            furnace.close();
            gameMode.executeUpdate();
            gameMode.close();
            craft.executeUpdate();
            craft.close();
            register.executeUpdate();
            register.close();

            server_Start.executeUpdate();
            server_Start.close();
            server_Stop.executeUpdate();
            server_Stop.close();
            console_Commands.executeUpdate();
            console_Commands.close();
            RAM.executeUpdate();
            RAM.close();
            TPS.executeUpdate();
            TPS.close();
            portal_Creation.executeUpdate();
            portal_Creation.close();
            rcon.executeUpdate();
            rcon.close();

        }catch (SQLException e){

            plugin.getLogger().severe("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }
}