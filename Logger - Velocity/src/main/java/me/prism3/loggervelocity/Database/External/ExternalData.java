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

public class ExternalData {

    private static Main plugin = Main.getInstance();

    public ExternalData(Main plugin){ ExternalData.plugin = plugin; }

    private static final List<String> tablesNames = Stream.of("Player_Chat_Velocity", "Player_Commands_Velocity",
            "Player_Login_Velocity", "Player_Leave_Velocity", "Console_Commands_Velocity", "Server_Start_Velocity",
            "Server_Stop_Velocity", "RAM_Velocity").collect(Collectors.toCollection(ArrayList::new));

    public void createTable(){

        final PreparedStatement playerChat, playerCommand, playerLogin, playerLeave, consoleCommands, serverStart,
                serverStop, ram, liteBans;

        try {

            playerChat = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Player_Name VARCHAR(100),Message VARCHAR(200),Is_Staff TINYINT)");

            playerCommand = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Player_Name VARCHAR(100),Command VARCHAR(200),Is_Staff TINYINT)");

            playerLogin = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Login_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Player_Name VARCHAR(100),IP INT UNSIGNED,Is_Staff TINYINT)");

            playerLeave = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Leave_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Player_Name VARCHAR(100),Is_Staff TINYINT)");

            // Server Side Part
            consoleCommands = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Console_Commands_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Command VARCHAR(256))");

            serverStart = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            serverStop = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Stop_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            ram = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM_Velocity "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Total_Memory INT,Used_Memory INT,Free_Memory INT)");

            // Extra Side Part
            if (LiteBansUtil.getLiteBansAPI().isPresent()) {

                liteBans = plugin.getExternal().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS LiteBans_Velocity "
                        + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Sender VARCHAR(100),Command VARCHAR(20),OnWho VARCHAR(100)," +
                        "Reason VARCHAR(200),Duration VARCHAR(30), Is_Silent TINYINT)");

                liteBans.executeUpdate();
                liteBans.close();

                tablesNames.add("LiteBans_Velocity");
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

            final PreparedStatement playerChat = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Player_Chat_Velocity (Server_Name,Player_Name,Message,Is_Staff) VALUES(?,?,?,?)");
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

            final PreparedStatement playerCommand = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Player_Commands_Velocity (Server_Name,Player_Name,Command,Is_Staff) VALUES(?,?,?,?)");
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

            final PreparedStatement playerLogin = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Player_Login_Velocity (Server_Name,Player_Name,IP,Is_Staff) VALUES(?,?,?,?)");
            playerLogin.setString(1, serverName);
            playerLogin.setString(2, playerName);
            if (Data.isPlayerIP) {

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

            final PreparedStatement playerLeave = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Player_Leave_Velocity (Server_Name,Player_Name,Is_Staff) VALUES(?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, playerName);
            playerLeave.setBoolean(3, staff);

            playerLeave.executeUpdate();
            playerLeave.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void consoleCommands(String serverName, String msg){

        try {

            final PreparedStatement consoleCommands = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Console_Commands_Velocity (Server_Name,Command) VALUES(?,?)");
            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, msg);

            consoleCommands.executeUpdate();
            consoleCommands.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStart(String serverName){

        try {

            final PreparedStatement serverStart = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Server_Start_Velocity (Server_Name) VALUES(?)");
            serverStart.setString(1, serverName);

            serverStart.executeUpdate();
            serverStart.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStop(String serverName){

        try {

            final PreparedStatement serverStop = plugin.getExternal().getConnection().prepareStatement("INSERT INTO Server_Stop_Velocity (Server_Name) VALUES(?)");
            serverStop.setString(1, serverName);

            serverStop.executeUpdate();
            serverStop.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void RAM(String serverName, long TM, long UM, long FM){

        try {

            final PreparedStatement RAM = plugin.getExternal().getConnection().prepareStatement("INSERT INTO RAM_Velocity (Server_Name,Total_Memory,Used_Memory,Free_Memory) VALUES(?,?,?,?)");
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

            final PreparedStatement player_Chat = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Chat_Velocity WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement player_Command = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Commands_Velocity WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement player_Login = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Login_Velocity WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement player_Leave = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Player_Leave_Velocity WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement console_Commands = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Console_Commands_Velocity WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement server_Start = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Server_Start_Velocity WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement server_Stop = plugin.getExternal().getConnection().prepareStatement("DELETE FROM Server_Stop_Velocity WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            final PreparedStatement ram = plugin.getExternal().getConnection().prepareStatement("DELETE FROM RAM_Velocity WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            if (LiteBansUtil.getLiteBansAPI().isPresent()) {

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
