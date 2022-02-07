package com.carpour.loggerbungeecord.Database.MySQL;

import com.carpour.loggerbungeecord.Main;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLData {

    private static Main plugin = Main.getInstance();

    public MySQLData(Main plugin){
        MySQLData.plugin = plugin;
    }

    public void createTable(){

        PreparedStatement playerChat, playerCommand, playerLogin, playerLeave, serverReload, serverStart, serverStop,
                ram, liteBans;

        try {

            playerChat = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),Message VARCHAR(200),Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerCommand = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),Command VARCHAR(200),Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerLogin = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Login_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),IP INT UNSIGNED,Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerLeave = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Leave_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),Is_Staff TINYINT,PRIMARY KEY (Date))");

            // Server Side Part
            serverReload = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Reload_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),Is_Staff TINYINT,PRIMARY KEY (Date))");

            serverStart = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),PRIMARY KEY (Date))");

            serverStop = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Stop_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),PRIMARY KEY (Date))");

            ram = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Total_Memory INT,Used_Memory INT,Free_Memory INT,PRIMARY KEY (Date))");

            // Extra Side Part
            liteBans = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS LiteBans_Proxy "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Sender VARCHAR(100),Command VARCHAR(20),OnWho VARCHAR(100)," +
                    "Reason VARCHAR(200),Duration VARCHAR(30), Is_Silent TINYINT,PRIMARY KEY (Date))");

            playerChat.executeUpdate();
            playerCommand.executeUpdate();
            playerLogin.executeUpdate();
            playerLeave.executeUpdate();

            serverReload.executeUpdate();
            serverStart.executeUpdate();
            serverStop.executeUpdate();
            ram.executeUpdate();

            liteBans.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }
    }

    public static void playerChat(String serverName, String playerName, String message, boolean staff){
        try {
            String database = "Player_Chat_Proxy";
            PreparedStatement playerChat = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,Message,Is_Staff) VALUES(?,?,?,?)");
            playerChat.setString(1, serverName);
            playerChat.setString(2, playerName);
            playerChat.setString(3, message);
            playerChat.setBoolean(4, staff);
            playerChat.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerCommands(String serverName, String playerName, String command, boolean staff){
        try {
            String database = "Player_Commands_Proxy";
            PreparedStatement playerCommand = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,Command,Is_Staff) VALUES(?,?,?,?)");
            playerCommand.setString(1, serverName);
            playerCommand.setString(2, playerName);
            playerCommand.setString(3, command);
            playerCommand.setBoolean(4, staff);
            playerCommand.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerLogin(String serverName, String playerName, InetSocketAddress IP, boolean staff){
        try {
            String database = "Player_Login_Proxy";
            PreparedStatement playerLogin = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,IP,Is_Staff) VALUES(?,?,?,?)");
            playerLogin.setString(1, serverName);
            playerLogin.setString(2, playerName);
            if (plugin.getConfig().getBoolean("Player-Login.Player-IP")) {

                playerLogin.setString(3, IP.getHostString());

            }else{

                playerLogin.setString(3, null);
            }
            playerLogin.setBoolean(4, staff);
            playerLogin.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerLeave(String serverName, String playerName, boolean staff){
        try {
            String database = "Player_Leave_Proxy";
            PreparedStatement playerLeave = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,Is_Staff) VALUES(?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, playerName);
            playerLeave.setBoolean(3, staff);
            playerLeave.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverReload(String serverName, String playerName, boolean staff){
        try {
            String database = "Server_Reload_Proxy";
            PreparedStatement serverReload= plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,Is_Staff) VALUES(?,?,?)");
            serverReload.setString(1, serverName);
            serverReload.setString(2, playerName);
            serverReload.setBoolean(3, staff);
            serverReload.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStart(String serverName){
        try {
            String database = "Server_Start_Proxy";
            PreparedStatement serverStart = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name) VALUES(?)");
            serverStart.setString(1, serverName);
            serverStart.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStop(String serverName){
        try {
            String database = "Server_Stop_Proxy";
            PreparedStatement serverStop = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name) VALUES(?)");
            serverStop.setString(1, serverName);
            serverStop.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void RAM(String serverName, long TM, long UM, long FM){
        try {
            String database = "RAM_Proxy";
            PreparedStatement RAM = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Total_Memory,Used_Memory,Free_Memory) VALUES(?,?,?,?)");
            RAM.setString(1, serverName);
            RAM.setLong(2, TM);
            RAM.setLong(3, UM);
            RAM.setLong(4, FM);
            RAM.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void liteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent){
        try {
            String database = "LiteBans_Proxy";
            PreparedStatement liteBans = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Sender,Command,OnWho,Reason,Duration,Is_Silent) VALUES(?,?,?,?,?,?,?)");
            liteBans.setString(1, serverName);
            liteBans.setString(2, executor);
            liteBans.setString(3, command);
            liteBans.setString(4, onWho);
            liteBans.setString(5, duration);
            liteBans.setString(6, reason);
            liteBans.setBoolean(7, isSilent);
            liteBans.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public void emptyTable(){

        int when = plugin.getConfig().getInt("MySQL.Data-Deletion");

        if (when <= 0) return;

        try{

            PreparedStatement player_Chat = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Chat_Proxy WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Command = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Commands_Proxy WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Login = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Login_Proxy WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Leave = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Leave_Proxy WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement server_Reload = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Server_Reload_Proxy WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement server_Start = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Server_Start_Proxy WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement server_Stop = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Server_Stop_Proxy WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement ram = plugin.mySQL.getConnection().prepareStatement("DELETE FROM RAM_Proxy WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement liteBans = plugin.mySQL.getConnection().prepareStatement("DELETE FROM LiteBans_Proxy WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            player_Chat.executeUpdate();
            player_Login.executeUpdate();
            player_Command.executeUpdate();
            player_Leave.executeUpdate();

            server_Reload.executeUpdate();
            server_Start.executeUpdate();
            server_Stop.executeUpdate();
            ram.executeUpdate();

            liteBans.executeUpdate();

        }catch (SQLException e){

            plugin.getLogger().severe("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }
}