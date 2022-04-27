package me.prism3.loggervelocity.Database.External;

import me.prism3.loggervelocity.API.LiteBansUtil;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.Utils.Data;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.prism3.loggervelocity.Utils.Data.externalDataDel;
import static me.prism3.loggervelocity.Utils.Data.isPlayerIP;

public class ExternalData {

    private static Main plugin = Main.getInstance();

    public ExternalData(Main plugin){ ExternalData.plugin = plugin; }

    private static final List<String> tablesNames = Stream.of("player_chat_velocity", "player_commands_velocity",
            "player_login_velocity", "player_leave_velocity", "console_commands_velocity", "server_start_velocity",
            "server_stop_velocity", "ram_velocity").collect(Collectors.toCollection(ArrayList::new));

    public void createTable(){

        final PreparedStatement playerChat, playerCommand, playerLogin, playerLeave, consoleCommands, serverStart,
                serverStop, ram, liteBans;

        try {

            playerChat = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_chat_velocity "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100), message VARCHAR(200), is_staff TINYINT)");

            playerCommand = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_commands_velocity "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100), command VARCHAR(200), is_staff TINYINT)");

            playerLogin = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_login_velocity "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100),ip INT UNSIGNED, is_staff TINYINT)");

            playerLeave = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_leave_velocity "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), player_name VARCHAR(100), is_staff TINYINT)");

            // Server Side Part
            consoleCommands = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS console_commands_velocity "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), command VARCHAR(256))");

            serverStart = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS server_start_velocity "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            serverStop = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS server_stop_velocity "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            ram = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ram_velocity "
                    + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), total_memory INT, used_memory INT, free_memory INT)");

            // Extra Side Part
            if (LiteBansUtil.getLiteBansAPI().isPresent()) {

                liteBans = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS litebans_velocity "
                        + "(server_name VARCHAR(30), date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), sender VARCHAR(100), command VARCHAR(20), onwho VARCHAR(100)," +
                        "reason VARCHAR(200), duration VARCHAR(30), is_silent TINYINT)");

                liteBans.executeUpdate();
                liteBans.close();

                tablesNames.add("litebans_velocity");
            }

            playerChat.executeUpdate();
            playerChat.close();
            playerCommand.executeUpdate();
            playerCommand.close();
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

        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerChat(String serverName, String playerName, String message, boolean staff){

        try {

            final PreparedStatement playerChat = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_chat_velocity (server_name, player_name, message, is_staff) VALUES(?,?,?,?)");
            playerChat.setString(1, serverName);
            playerChat.setString(2, playerName);
            playerChat.setString(3, message);
            playerChat.setBoolean(4, staff);

            playerChat.executeUpdate();
            playerChat.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerCommands(String serverName, String playerName, String command, boolean staff){

        try {

            final PreparedStatement playerCommand = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_commands_velocity (server_name, player_name, command, is_staff) VALUES(?,?,?,?)");
            playerCommand.setString(1, serverName);
            playerCommand.setString(2, playerName);
            playerCommand.setString(3, command);
            playerCommand.setBoolean(4, staff);

            playerCommand.executeUpdate();
            playerCommand.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerLogin(String serverName, String playerName, InetSocketAddress ip, boolean staff){

        try {

            final PreparedStatement playerLogin = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_login_velocity (server_name, player_name, ip, is_staff) VALUES(?,?,?,?)");
            playerLogin.setString(1, serverName);
            playerLogin.setString(2, playerName);
            if (isPlayerIP) {

                playerLogin.setString(3, ip.getHostString());

            }else{

                playerLogin.setString(3, null);
            }
            playerLogin.setBoolean(4, staff);

            playerLogin.executeUpdate();
            playerLogin.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerLeave(String serverName, String playerName, boolean staff){

        try {

            final PreparedStatement playerLeave = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_leave_velocity (server_name, player_name, is_staff) VALUES(?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, playerName);
            playerLeave.setBoolean(3, staff);

            playerLeave.executeUpdate();
            playerLeave.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void consoleCommands(String serverName, String msg){

        try {

            final PreparedStatement consoleCommands = plugin.getExternal().getConnection().prepareStatement("INSERT INTO console_commands_velocity (server_name, command) VALUES(?,?)");
            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, msg);

            consoleCommands.executeUpdate();
            consoleCommands.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStart(String serverName){

        try {

            final PreparedStatement serverStart = plugin.getExternal().getConnection().prepareStatement("INSERT INTO server_start_velocity (server_name) VALUES(?)");
            serverStart.setString(1, serverName);

            serverStart.executeUpdate();
            serverStart.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStop(String serverName){

        try {

            final PreparedStatement serverStop = plugin.getExternal().getConnection().prepareStatement("INSERT INTO server_stop_velocity (server_name) VALUES(?)");
            serverStop.setString(1, serverName);

            serverStop.executeUpdate();
            serverStop.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void ram(String serverName, long TM, long UM, long FM){

        try {

            final PreparedStatement ram = plugin.getExternal().getConnection().prepareStatement("INSERT INTO ram_velocity (server_name, total_memory, used_memory, free_memory) VALUES(?,?,?,?)");
            ram.setString(1, serverName);
            ram.setLong(2, TM);
            ram.setLong(3, UM);
            ram.setLong(4, FM);

            ram.executeUpdate();
            ram.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void liteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent){

        try {

            final PreparedStatement liteBans = plugin.getExternal().getConnection().prepareStatement("INSERT INTO litebans_proxy (server_name, sender, command, onwho, reason, duration, is_silent) VALUES(?,?,?,?,?,?,?)");
            liteBans.setString(1, serverName);
            liteBans.setString(2, executor);
            liteBans.setString(3, command);
            liteBans.setString(4, onWho);
            liteBans.setString(5, duration);
            liteBans.setString(6, reason);
            liteBans.setBoolean(7, isSilent);

            liteBans.executeUpdate();
            liteBans.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public void emptyTable(){

        if (externalDataDel <= 0) return;

        try{

            final PreparedStatement player_Chat = plugin.getExternal().getConnection().prepareStatement("DELETE FROM player_chat_velocity WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            final PreparedStatement player_Command = plugin.getExternal().getConnection().prepareStatement("DELETE FROM player_commands_velocity WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            final PreparedStatement player_Login = plugin.getExternal().getConnection().prepareStatement("DELETE FROM player_login_velocity WHERE Date < NOW() - INTERVAL " + externalDataDel + " DAY");

            final PreparedStatement player_Leave = plugin.getExternal().getConnection().prepareStatement("DELETE FROM player_leave_velocity WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            final PreparedStatement console_Commands = plugin.getExternal().getConnection().prepareStatement("DELETE FROM console_commands_velocity WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            final PreparedStatement server_Start = plugin.getExternal().getConnection().prepareStatement("DELETE FROM server_start_velocity WHERE Date < NOW() - INTERVAL " + externalDataDel + " DAY");

            final PreparedStatement server_Stop = plugin.getExternal().getConnection().prepareStatement("DELETE FROM server_stop_velocity WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            final PreparedStatement ram = plugin.getExternal().getConnection().prepareStatement("DELETE FROM ram_velocity WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

            if (LiteBansUtil.getLiteBansAPI().isPresent()) {

                final PreparedStatement liteBans = plugin.getExternal().getConnection().prepareStatement("DELETE FROM litebans_proxy WHERE date < NOW() - INTERVAL " + externalDataDel + " DAY");

                liteBans.executeUpdate();
                liteBans.close();
            }

            player_Chat.executeUpdate();
            player_Chat.close();
            player_Login.executeUpdate();
            player_Login.close();
            player_Command.executeUpdate();
            player_Command.close();
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
    public static List<String> getTablesNames() { return tablesNames; }
}
