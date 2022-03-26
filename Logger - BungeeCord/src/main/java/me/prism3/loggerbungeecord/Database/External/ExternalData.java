package me.prism3.loggerbungeecord.Database.External;

import me.prism3.loggerbungeecord.API.LiteBansUtil;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.Utils.Data;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExternalData {

    private static Main plugin;

    public ExternalData(Main plugin){
        ExternalData.plugin = plugin;
    }

    private static final List<String> tablesNames = Stream.of("Player_Chat_Proxy", "Player_Commands_Proxy", "Player_Login_Proxy",
            "Player_Leave_Proxy", "Server_Reload_Proxy", "Server_Start_Proxy", "Server_Stop_Proxy", "RAM_Proxy").collect(Collectors.toCollection(ArrayList::new));

    public void createTable(){

        final PreparedStatement playerChat, playerCommand, playerLogin, playerLeave, serverReload, serverStart, serverStop,
                ram, liteBans;

        try {

            playerChat = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Player_Name VARCHAR(100),Message VARCHAR(200),Is_Staff TINYINT)");

            playerCommand = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Player_Name VARCHAR(100),Command VARCHAR(200),Is_Staff TINYINT)");

            playerLogin = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Login_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Player_Name VARCHAR(100),IP INT UNSIGNED,Is_Staff TINYINT)");

            playerLeave = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Leave_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Player_Name VARCHAR(100),Is_Staff TINYINT)");

            // Server Side Part
            serverReload = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Reload_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Player_Name VARCHAR(100),Is_Staff TINYINT)");

            serverStart = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            serverStop = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Stop_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            ram = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Total_Memory INT,Used_Memory INT,Free_Memory INT)");

            // Extra Side Part
            if (LiteBansUtil.getLiteBansAPI() != null) {

                liteBans = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS LiteBans_Proxy "
                        + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Sender VARCHAR(100),Command VARCHAR(20),OnWho VARCHAR(100)," +
                        "Reason VARCHAR(200),Duration VARCHAR(30), Is_Silent TINYINT)");

                liteBans.executeUpdate();
                liteBans.close();

                tablesNames.add("LiteBans_Proxy");
            }

            playerChat.executeUpdate();
            playerChat.close();
            playerCommand.executeUpdate();
            playerCommand.close();
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

        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerChat(String serverName, String playerName, String message, boolean staff){

        try {

            final PreparedStatement playerChat = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Player_Chat_Proxy (Server_Name,Player_Name,Message,Is_Staff) VALUES(?,?,?,?)");
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

            final PreparedStatement playerCommand = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Player_Commands_Proxy (Server_Name,Player_Name,Command,Is_Staff) VALUES(?,?,?,?)");
            playerCommand.setString(1, serverName);
            playerCommand.setString(2, playerName);
            playerCommand.setString(3, command);
            playerCommand.setBoolean(4, staff);

            playerCommand.executeUpdate();
            playerCommand.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerLogin(String serverName, String playerName, InetSocketAddress IP, boolean staff){

        try {

            final PreparedStatement playerLogin = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Player_Login_Proxy (Server_Name,Player_Name,IP,Is_Staff) VALUES(?,?,?,?)");
            playerLogin.setString(1, serverName);
            playerLogin.setString(2, playerName);
            if (plugin.getConfig().getBoolean("Player-Login.Player-IP")) {

                playerLogin.setString(3, IP.getHostString());

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

            final PreparedStatement playerLeave = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Player_Leave_Proxy (Server_Name,Player_Name,Is_Staff) VALUES(?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, playerName);
            playerLeave.setBoolean(3, staff);

            playerLeave.executeUpdate();
            playerLeave.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverReload(String serverName, String playerName, boolean staff){

        try {

            final PreparedStatement serverReload= plugin.getExternal().getConnection().prepareStatement("INSERT INTO Server_Reload_Proxy (Server_Name,Player_Name,Is_Staff) VALUES(?,?,?)");
            serverReload.setString(1, serverName);
            serverReload.setString(2, playerName);
            serverReload.setBoolean(3, staff);

            serverReload.executeUpdate();
            serverReload.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStart(String serverName){

        try {

            final PreparedStatement serverStart = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Server_Start_Proxy (Server_Name) VALUES(?)");
            serverStart.setString(1, serverName);

            serverStart.executeUpdate();
            serverStart.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStop(String serverName){

        try {

            final PreparedStatement serverStop = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Server_Stop_Proxy (Server_Name) VALUES(?)");
            serverStop.setString(1, serverName);

            serverStop.executeUpdate();
            serverStop.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void RAM(String serverName, long TM, long UM, long FM){

        try {

            final PreparedStatement RAM = plugin.getExternal().getConnection().prepareStatement("INSERT INTO RAM_Proxy (Server_Name,Total_Memory,Used_Memory,Free_Memory) VALUES(?,?,?,?)");
            RAM.setString(1, serverName);
            RAM.setLong(2, TM);
            RAM.setLong(3, UM);
            RAM.setLong(4, FM);

            RAM.executeUpdate();
            RAM.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void liteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent){

        try {

            final PreparedStatement liteBans = plugin.getExternal().getConnection().prepareStatement("INSERT INTO LiteBans_Proxy (Server_Name,Sender,Command,OnWho,Reason,Duration,Is_Silent) VALUES(?,?,?,?,?,?,?)");
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

        if (Data.externalDataDel <= 0) return;

        try{

            final PreparedStatement player_Chat = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Chat_Proxy WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement player_Command = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Commands_Proxy WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement player_Login = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Login_Proxy WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement player_Leave = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Leave_Proxy WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement server_Reload = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Server_Reload_Proxy WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement server_Start = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Server_Start_Proxy WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement server_Stop = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Server_Stop_Proxy WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement ram = plugin.getExternal().getConnection().prepareStatement("DELETE FROM RAM_Proxy WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            if (LiteBansUtil.getLiteBansAPI() != null) {

                final PreparedStatement liteBans = plugin.getExternal().getConnection().prepareStatement("DELETE FROM LiteBans_Proxy WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

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

    public static List<String> getTablesNames() { return tablesNames; }
}