package me.prism3.loggervelocity.Database.SQLite;

import me.prism3.loggervelocity.API.LiteBansUtil;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.Utils.Data;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SQLiteData {

    private static Main plugin;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSSXXX");

    public SQLiteData(Main plugin) { SQLiteData.plugin = plugin; }

    public void createTable() {

        final PreparedStatement playerChat, playerCommands, playerLogin, playerLeave, serverStart, serverStop,
                consoleCommands, ram, liteBans;

        try {

            // Player Side Part
            playerChat = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat_Velocity" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, Player_Name TEXT(30)," +
                    "Message TEXT(256), Is_Staff INTEGER)");

            playerCommands = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands_Velocity" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, Player_Name TEXT(30)," +
                    "Command TEXT(100), Is_Staff INTEGER)");

            playerLogin = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Login_Velocity" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'now'))  PRIMARY KEY," +
                    "Player_Name TEXT(30), IP TEXT, Is_Staff INTEGER)");

            playerLeave = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS  Player_Leave_Velocity " +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, Player_Name TEXT(30), Is_Staff INTEGER)");

            // Server Side Part
            consoleCommands = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Console_Commands_Velocity" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY,Command TEXT(256))");

            serverStart = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start_Velocity" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY)");

            serverStop = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE  IF NOT EXISTS Server_Stop_Velocity" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY )");

            ram = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM_Velocity" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY," +
                    " Total_Memory INTEGER, Used_Memory INTEGER, Free_Memory INTEGER)");

            // Extra Side Part
            if (LiteBansUtil.getLiteBansAPI().isPresent()) {
                liteBans = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS LiteBans_Proxy" +
                        "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY," +
                        " Sender TEXT(30), Command TEXT(10), OnWho TEXT(30), Reason TEXT(60)," +
                        " Duration TEXT(50), Is_Silent INTEGER)");

                liteBans.executeUpdate();
                liteBans.close();

            }


            playerChat.executeUpdate();
            playerChat.close();
            playerCommands.executeUpdate();
            playerCommands.close();
            playerLogin.executeUpdate();
            playerLogin.close();
            playerLeave.executeUpdate();
            playerLeave.close();

            consoleCommands.executeUpdate();
            consoleCommands.close();
            serverStart.executeUpdate();
            serverStart.close();
            serverStop.executeUpdate();
            serverStop.close();
            ram.executeUpdate();
            ram.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerChat(String serverName, String player, String message, boolean staff) {

        try {
            final PreparedStatement playerChat = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Chat_Velocity (Server_Name, Date, Player_Name, Message, Is_Staff) VALUES (?,?,?,?,?)");
            playerChat.setString(1, serverName);
            playerChat.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerChat.setString(3, player);
            playerChat.setString(4, message);
            playerChat.setBoolean(5, staff);

            playerChat.executeUpdate();
            playerChat.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerCommands(String serverName, String player, String command, boolean staff) {

        try {

            final PreparedStatement playerCommands = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Commands_Velocity (Server_Name, Date, Player_Name, Command, Is_Staff) VALUES (?,?,?,?,?)");
            playerCommands.setString(1, serverName);
            playerCommands.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerCommands.setString(3, player);
            playerCommands.setString(4, command);
            playerCommands.setBoolean(5, staff);

            playerCommands.executeUpdate();
            playerCommands.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerLogin(String serverName, String player, InetSocketAddress IP, boolean isStaff) {

        try {

            final PreparedStatement playerLogin = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Login_Velocity (Server_Name, Date, Player_Name, IP, Is_Staff) VALUES (?,?,?,?,?)");
            playerLogin.setString(1, serverName);
            playerLogin.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerLogin.setString(3, player);
            if (Data.isPlayerIP) {

                playerLogin.setString(4, IP.getHostString());

            }else{

                playerLogin.setString(4, null);
            }

            playerLogin.setBoolean(5, isStaff);

            playerLogin.executeUpdate();
            playerLogin.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerLeave(String serverName, String player, boolean isStaff) {

        try {

            final PreparedStatement playerLeave = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Leave_Velocity (Server_Name, Date, Player_Name, Is_Staff) VALUES (?,?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerLeave.setString(3, player);
            playerLeave.setBoolean(4, isStaff);

            playerLeave.executeUpdate();
            playerLeave.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertConsoleCommands(String serverName, String command) {

        try {

            final PreparedStatement serverReload = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Console_Commands_Velocity (Server_Name, Date, Command) VALUES (?,?,?)");
            serverReload.setString(1, serverName);
            serverReload.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            serverReload.setString(3, command);

            serverReload.executeUpdate();
            serverReload.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertServerStart(String serverName) {

        try {

            final PreparedStatement serverStartStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Server_Start_Velocity (Server_Name, Date) VALUES (?,?)");
            serverStartStatement.setString(1, serverName);
            serverStartStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

            serverStartStatement.executeUpdate();
            serverStartStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertServerStop(String serverName) {

        try {

            final PreparedStatement serverStopStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Server_Stop_Velocity (Server_Name, Date) VALUES (?,?)");
            serverStopStatement.setString(1, serverName);
            serverStopStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

            serverStopStatement.executeUpdate();
            serverStopStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertRAM(String serverName, long totalMemory, long usedMemory, long freeMemory) {

        try {

            final PreparedStatement ramStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO RAM_Velocity (Server_Name, Date, Total_Memory, Used_Memory, Free_Memory) VALUES (?,?,?,?,?)");
            ramStatement.setString(1, serverName);
            ramStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            ramStatement.setLong(3, totalMemory);
            ramStatement.setLong(4, usedMemory);
            ramStatement.setLong(5, freeMemory);

            ramStatement.executeUpdate();
            ramStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertLiteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent) {

        try {

            final PreparedStatement liteBansStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO LiteBans_Proxy (Server_Name, Date, Sender, Command, OnWho, Reason, Duration, Is_Silent) VALUES(?,?,?,?,?,?,?,?)");
            liteBansStatement.setString(1, serverName);
            liteBansStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            liteBansStatement.setString(3, executor);
            liteBansStatement.setString(4, command);
            liteBansStatement.setString(5, onWho);
            liteBansStatement.setString(6, duration);
            liteBansStatement.setString(7, reason);
            liteBansStatement.setBoolean(8, isSilent);

            liteBansStatement.executeUpdate();
            liteBansStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }


    public void emptyTable() {

        if (Data.sqliteDataDel <= 0) return;

        try{

            // Player Side Part
            final PreparedStatement player_Chat = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Chat_Velocity WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Commands = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Commands_Velocity WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Login = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Login_Velocity WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement player_Leave = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Player_Leave_Velocity WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            // Server Side Part
            final PreparedStatement console_Commands = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Console_Commands_Velocity WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement server_Start = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Server_Start_Velocity WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement server_Stop = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM Server_Stop_Velocity WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            final PreparedStatement ram = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM RAM_Velocity WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

            // Extra Side Part
            if (LiteBansUtil.getLiteBansAPI().isPresent()) {

                final PreparedStatement liteBans = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM LiteBans_Proxy WHERE Date <= datetime('now','-" + Data.sqliteDataDel + " day')");

                liteBans.executeUpdate();
                liteBans.close();
            }

            player_Chat.executeUpdate();
            player_Chat.close();
            player_Commands.executeUpdate();
            player_Commands.close();
            player_Login.executeUpdate();
            player_Login.close();
            player_Leave.executeUpdate();
            player_Leave.close();

            console_Commands.executeUpdate();
            console_Commands.close();
            server_Start.executeUpdate();
            server_Start.close();
            server_Stop.executeUpdate();
            server_Stop.close();
            ram.executeUpdate();
            ram.close();

        }catch (SQLException e){

            plugin.getLogger().error("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }
}
