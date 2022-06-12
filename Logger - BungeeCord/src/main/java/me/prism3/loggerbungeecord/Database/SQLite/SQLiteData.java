package me.prism3.loggerbungeecord.Database.SQLite;

import me.prism3.loggerbungeecord.API.LiteBansUtil;
import me.prism3.loggerbungeecord.Main;

import java.net.InetSocketAddress;
import java.sql.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static me.prism3.loggerbungeecord.Utils.Data.sqliteDataDel;

public class SQLiteData {

    private static Main plugin;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:SSSXXX");

    public SQLiteData(Main plugin) {
        SQLiteData.plugin = plugin;
    }

    public void createTable() {

        final PreparedStatement playerChat, playerCommands, playerLogin, playerLeave, serverStart, serverStop, serverReload,
                ram, liteBans;

        try {

            // Player Side Part
            playerChat = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_chat_proxy" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, player_name TEXT(30)," +
                    "message TEXT(256), is_staff INTEGER)");

            playerCommands = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_commands_proxy" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, player_name TEXT(30)," +
                    "command TEXT(100), is_staff INTEGER)");

            playerLogin = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_login_proxy" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'now')) ," +
                    "player_name TEXT(30), ip TEXT, is_staff INTEGER)");

            playerLeave = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_leave_proxy " +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, player_name TEXT(30), is_staff INTEGER)");

            // Server Side Part
            serverReload = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS server_reload_proxy" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, player_name TEXT(30), command TEXT(256), is_staff INTEGER)");

            serverStart = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS server_start_proxy" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            serverStop = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS server_stop_proxy" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            ram = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ram_proxy" +
                    "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    " total_memory INTEGER, used_memory INTEGER, free_memory INTEGER)");

            // Extra Side Part
            if (LiteBansUtil.getLiteBansAPI() != null) {

                liteBans = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS litebans_proxy" +
                        "(server_name TEXT(30), date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        " sender TEXT(30), command TEXT(10), onwho TEXT(30), reason TEXT(60)," +
                        " duration TEXT(50), is_silent INTEGER)");

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

            serverReload.executeUpdate();
            serverReload.close();
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

            final PreparedStatement playerChat = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO player_chat_proxy (server_name, date, player_name, message, is_staff) VALUES (?,?,?,?,?)");

            playerChat.setString(1, serverName);
            playerChat.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerChat.setString(3, player);
            playerChat.setString(4, message);
            playerChat.setBoolean(5, staff);

            playerChat.executeUpdate();
            playerChat.close();

        } catch (SQLException exception) { exception.printStackTrace(); }
    }

    public static void insertPlayerCommands(String serverName, String player, String command, boolean staff) {

        try {

            final PreparedStatement playerCommands = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO player_commands_proxy (server_name, date, player_name, command, is_staff) VALUES (?,?,?,?,?)");

            playerCommands.setString(1, serverName);
            playerCommands.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerCommands.setString(3, player);
            playerCommands.setString(4, command);
            playerCommands.setBoolean(5, staff);

            playerCommands.executeUpdate();
            playerCommands.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertPlayerLogin(String serverName, String player, InetSocketAddress ip, boolean isStaff) {

        try {

            final PreparedStatement playerLogin = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO player_login_proxy (server_name, date, player_name, ip, is_staff) VALUES (?,?,?,?,?)");

            playerLogin.setString(1, serverName);
            playerLogin.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerLogin.setString(3, player);
            if (plugin.getConfig().getBoolean("Player-Login.Player-ip")) {

                playerLogin.setString(4, ip.getHostString());

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

            final PreparedStatement playerLeave = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO player_leave_proxy (server_name, date, player_name, is_staff) VALUES (?,?,?,?)");

            playerLeave.setString(1, serverName);
            playerLeave.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            playerLeave.setString(3, player);
            playerLeave.setBoolean(4, isStaff);

            playerLeave.executeUpdate();
            playerLeave.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }


    public static void insertServerReload(String serverName, String player, boolean isStaff) {

        try {

            final PreparedStatement serverReload = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO server_reload_proxy (server_name, date, player_name, is_staff) VALUES (?,?,?,?)");

            serverReload.setString(1, serverName);
            serverReload.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));
            serverReload.setString(3, player);
            serverReload.setBoolean(4, isStaff);

            serverReload.executeUpdate();
            serverReload.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertServerStart(String serverName) {

        try {

            final PreparedStatement serverStartStatement = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO server_start_proxy (server_name, date) VALUES (?,?)");

            serverStartStatement.setString(1, serverName);
            serverStartStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

            serverStartStatement.executeUpdate();
            serverStartStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertServerStop(String serverName) {

        try {

            final PreparedStatement serverStopStatement = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO server_stop_proxy (server_name, date) VALUES (?,?)");

            serverStopStatement.setString(1, serverName);
            serverStopStatement.setString(2, dateTimeFormatter.format(ZonedDateTime.now()));

            serverStopStatement.executeUpdate();
            serverStopStatement.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertRAM(String serverName, long totalMemory, long usedMemory, long freeMemory) {

        try {

            final PreparedStatement ramStatement = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO ram_proxy (server_name, date, total_memory, used_memory, free_memory) VALUES (?,?,?,?,?)");

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

            final PreparedStatement liteBansStatement = plugin.getSqLite().getConnection().prepareStatement(
                    "INSERT INTO litebans_proxy (server_name, date, sender, command, onwho, reason, " +
                            "duration, is_silent) VALUES(?,?,?,?,?,?,?,?)");

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

        if (sqliteDataDel <= 0) return;

        try{

            // Player Side Part
            final PreparedStatement player_Chat = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_chat_proxy WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement player_commands = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_commands_proxy WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement player_Login = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_login_proxy WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement player_Leave = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM player_leave_proxy WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            // Server Side Part
            final PreparedStatement server_Reload = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM server_reload_proxy WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement server_Start = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM server_start_proxy WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement server_Stop = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM server_stop_proxy WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            final PreparedStatement ram = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM ram_proxy WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

            // Extra Side Part
            if (LiteBansUtil.getLiteBansAPI() != null) {

                final PreparedStatement liteBans = plugin.getSqLite().getConnection().prepareStatement("DELETE FROM litebans_proxy WHERE date <= datetime('now','-" + sqliteDataDel + " day')");

                liteBans.executeUpdate();
                liteBans.close();
            }

            player_Chat.executeUpdate();
            player_Chat.close();
            player_commands.executeUpdate();
            player_commands.close();
            player_Login.executeUpdate();
            player_Login.close();
            player_Leave.executeUpdate();
            player_Leave.close();

            server_Reload.executeUpdate();
            server_Reload.close();
            server_Start.executeUpdate();
            server_Start.close();
            server_Stop.executeUpdate();
            server_Stop.close();
            ram.executeUpdate();
            ram.close();

        }catch (SQLException e){

            plugin.getLogger().severe("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }
}