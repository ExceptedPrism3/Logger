package com.carpour.loggervelocity.Database.External;

import com.carpour.loggervelocity.API.LiteBansUtil;
import com.carpour.loggervelocity.Main;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.carpour.loggervelocity.Utils.Data.externalDataDel;
import static com.carpour.loggervelocity.Utils.Data.isPlayerIP;

public class ExternalData {

    private static Main plugin = Main.getInstance();

    public ExternalData(Main plugin){ ExternalData.plugin = plugin; }

    public void createTable(){

        final PreparedStatement playerChat, playerCommand, playerLogin, playerLeave, consoleCommands, serverStart,
                serverStop, ram, liteBans;

        try {

            playerChat = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),Message VARCHAR(200),Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerCommand = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),Command VARCHAR(200),Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerLogin = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Login_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),IP INT UNSIGNED,Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerLeave = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Leave_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),Is_Staff TINYINT,PRIMARY KEY (Date))");

            // Server Side Part
            consoleCommands = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Console_Commands_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Command VARCHAR(256),PRIMARY KEY (Date))");

            serverStart = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),PRIMARY KEY (Date))");

            serverStop = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Stop_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),PRIMARY KEY (Date))");

            ram = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Total_Memory INT,Used_Memory INT,Free_Memory INT,PRIMARY KEY (Date))");

            // Extra Side Part
            if (LiteBansUtil.getLiteBansAPI().isPresent()) {

                liteBans = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS LiteBans_Proxy "
                        + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Sender VARCHAR(100),Command VARCHAR(20),OnWho VARCHAR(100)," +
                        "Reason VARCHAR(200),Duration VARCHAR(30), Is_Silent TINYINT,PRIMARY KEY (Date))");

                liteBans.executeUpdate();
            }

            playerChat.executeUpdate();
            playerCommand.executeUpdate();
            playerLogin.executeUpdate();
            playerLeave.executeUpdate();

            consoleCommands.executeUpdate();
            serverStart.executeUpdate();
            serverStop.executeUpdate();
            ram.executeUpdate();

        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerChat(String serverName, String playerName, String message, boolean staff){

        try {

            final String database = "Player_Chat_Velocity";
            final PreparedStatement playerChat = plugin.getExternal().getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,Message,Is_Staff) VALUES(?,?,?,?)");
            playerChat.setString(1, serverName);
            playerChat.setString(2, playerName);
            playerChat.setString(3, message);
            playerChat.setBoolean(4, staff);

            playerChat.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerCommands(String serverName, String playerName, String command, boolean staff){

        try {

            final String database = "Player_Commands_Velocity";
            final PreparedStatement playerCommand = plugin.getExternal().getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,Command,Is_Staff) VALUES(?,?,?,?)");
            playerCommand.setString(1, serverName);
            playerCommand.setString(2, playerName);
            playerCommand.setString(3, command);
            playerCommand.setBoolean(4, staff);

            playerCommand.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerLogin(String serverName, String playerName, InetSocketAddress IP, boolean staff){

        try {

            final String database = "Player_Login_Velocity";
            final PreparedStatement playerLogin = plugin.getExternal().getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,IP,Is_Staff) VALUES(?,?,?,?)");
            playerLogin.setString(1, serverName);
            playerLogin.setString(2, playerName);
            if (isPlayerIP) {

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

            final String database = "Player_Leave_Velocity";
            final PreparedStatement playerLeave = plugin.getExternal().getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,Is_Staff) VALUES(?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, playerName);
            playerLeave.setBoolean(3, staff);

            playerLeave.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void consoleCommands(String serverName, String msg){

        try {

            final String database = "Console_Commands_Velocity";
            final PreparedStatement consoleCommands = plugin.getExternal().getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Command) VALUES(?,?)");
            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, msg);

            consoleCommands.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStart(String serverName){

        try {

            final String database = "Server_Start_Velocity";
            final PreparedStatement serverStart = plugin.getExternal().getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name) VALUES(?)");
            serverStart.setString(1, serverName);

            serverStart.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStop(String serverName){

        try {

            final String database = "Server_Stop_Velocity";
            final PreparedStatement serverStop = plugin.getExternal().getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name) VALUES(?)");
            serverStop.setString(1, serverName);

            serverStop.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void RAM(String serverName, long TM, long UM, long FM){

        try {

            final String database = "RAM_Velocity";
            final PreparedStatement RAM = plugin.getExternal().getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Total_Memory,Used_Memory,Free_Memory) VALUES(?,?,?,?)");
            RAM.setString(1, serverName);
            RAM.setLong(2, TM);
            RAM.setLong(3, UM);
            RAM.setLong(4, FM);

            RAM.executeUpdate();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void liteBans(String serverName, String executor, String command, String onWho, String duration, String reason, boolean isSilent){

        try {

            final String database = "LiteBans_Proxy";
            final PreparedStatement liteBans = plugin.getExternal().getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Sender,Command,OnWho,Reason,Duration,Is_Silent) VALUES(?,?,?,?,?,?,?)");
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

        int when = externalDataDel;

        if (when <= 0) return;

        try{

            final PreparedStatement player_Chat = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Chat_Velocity WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            final PreparedStatement player_Command = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Commands_Velocity WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            final PreparedStatement player_Login = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Login_Velocity WHERE Date < NOW() - INTERVAL " + when + " DAY");

            final PreparedStatement player_Leave = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Leave_Velocity WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            final PreparedStatement console_Commands = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Console_Commands_Velocity WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            final PreparedStatement server_Start = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Server_Start_Velocity WHERE Date < NOW() - INTERVAL " + when + " DAY");

            final PreparedStatement server_Stop = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Server_Stop_Velocity WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            final PreparedStatement ram = plugin.getExternal().getConnection().prepareStatement("DELETE FROM RAM_Velocity WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            if (LiteBansUtil.getLiteBansAPI().isPresent()) {

                final PreparedStatement liteBans = plugin.getExternal().getConnection().prepareStatement("DELETE FROM LiteBans_Proxy WHERE DATE < NOW() - INTERVAL " + when + " DAY");

                liteBans.executeUpdate();
            }

            player_Chat.executeUpdate();
            player_Login.executeUpdate();
            player_Command.executeUpdate();
            player_Leave.executeUpdate();

            console_Commands.executeUpdate();
            server_Start.executeUpdate();
            server_Stop.executeUpdate();
            ram.executeUpdate();

        }catch (SQLException e){

            plugin.getLogger().error("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }
}
