package com.carpour.logger.database.SQLite;

import com.carpour.logger.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SQLiteData {


    private static Main plugin;

    public SQLiteData(Main plugin){
        SQLiteData.plugin = plugin;
    }

    public void createTable() {

        PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin, playerLeave, playerDeath, playerTeleport, blockPlace, blockBreak, TPS, RAM, playerKick, portalCreation, playerLevel, bucketPlace, anvil, serverStart, serverStop, itemDrop, enchant;

        try {
            playerChat = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat " +
                    "(Server_Name TEXT(30),Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY , World TEXT(100), Player_Name TEXT(20)," +
                    "Message TEXT(256), Is_Staff INTEGER)");


            playerCommands = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100),Player_Name TEXT(100)," +
                    "Command TEXT(100),Is_Staff INTEGER)");

            consoleCommands = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Console_Commands" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY,Command TEXT(256))");

            playerSignText = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Sign_Text" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(50)," +
                    " X INTEGER, Y INTEGER, Z INTEGER," +
                    "Player_Name TEXT(100), Line TEXT(100), Is_Staff INTEGER)");

            playerJoin = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Join" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT  CURRENT_TIMESTAMP PRIMARY KEY , World TEXT(50)," +
                    "X INTEGER, Y INTEGER, Z INTEGER, IP TEXT, Is_Staff INTEGER)");

            playerLeave = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS  Player_Leave " +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(50), X INTEGER, Y INTEGER, Z INTEGER , Player_Name TEXT(100), Is_Staff INTEGER)");

            playerDeath = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Deaths" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
                    "X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            playerTeleport = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Teleports" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
                    "From_X INTEGER, From_Y INTEGER, From_Z INTEGER, To_X INTEGER, To_Y INTEGER, To_Z INTEGER, Is_Staff INTEGER)");

            blockPlace = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Place" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
                    "Block TEXT(40),X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            blockBreak = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Break" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY , World TEXT(100), Player_Name TEXT(100)," +
                    "Block TEXT(40), X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            TPS = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS TPS" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, TPS INTEGER )");

            RAM = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY," +
                    " Total_Memory INTEGER, Used_Memory INTEGER, Free_Memory INTEGER)");

            playerKick = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Kicks" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY,World TEXT(100), Player_Name TEXT(100)," +
                    " X INTEGER, Y INTEGER, Z INTEGER, Reason TEXT(50), Is_Staff INTEGER )");


            portalCreation = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Portal_Creation" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(50), Caused_By TEXT(50))");

            playerLevel = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE  IF NOT EXISTS Player_Level" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, Player_Name TEXT(100), Is_Staff INTEGER )");

            bucketPlace = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Bucket_Place" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY ,World TEXT(100), Player_Name TEXT(100)," +
                    "Bucket TEXT(40), X INTEGER, Y INTEGER, Z INTEGER, Is_Staff INTEGER)");

            anvil = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Anvil" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY , New_Name TEXT(100), Is_Staff INTEGER)");

            serverStart = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY)");

            serverStop = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE  IF NOT EXISTS Server_Stop" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY )");

            itemDrop = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Item_Drop" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
                    "Item TEXT(50), Is_Staff INTEGER)");

            enchant = plugin.getSqLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Enchanting" +
                    "(Server_Name TEXT(30), Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP PRIMARY KEY, World TEXT(100), Player_Name TEXT(100)," +
                    "X INTEGER, Y INTEGER, Z INTEGER, Enchantment TEXT(50), Item TEXT(50), Cost INTEGER, Is_Staff INTEGER)");

            playerChat.executeUpdate();
            playerCommands.executeUpdate();
            consoleCommands.executeUpdate();
            playerSignText.executeUpdate();
            playerDeath.executeUpdate();
            playerTeleport.executeUpdate();
            playerJoin.executeUpdate();
            playerLeave.executeUpdate();
            blockPlace.executeUpdate();
            blockBreak.executeUpdate();
            TPS.executeUpdate();
            RAM.executeUpdate();
            playerKick.executeUpdate();
            portalCreation.executeUpdate();
            playerLevel.executeUpdate();
            bucketPlace.executeUpdate();
            anvil.executeUpdate();
            serverStart.executeUpdate();
            serverStop.executeUpdate();
            itemDrop.executeUpdate();
            enchant.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insertPlayerChat(String serverName, Player player, String message, boolean staff) {

        try {
            PreparedStatement playerChat = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Chat (Server_Name,Date, World, Player_Name, Message, Is_Staff) VALUES (?,?,?,?,?,?)");
            playerChat.setString(1, serverName);
            playerChat.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            playerChat.setString(3, player.getWorld().getName());
            playerChat.setString(4, player.getName());
            playerChat.setString(5, message);
            playerChat.setBoolean(6, staff);


            playerChat.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void insertPlayerCommands(String serverName, Player player, String command, boolean staff) {
        try {
            PreparedStatement playerCommands = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Commands(Server_Name,Date, World, Player_Name, Command, Is_Staff) VALUES (?,?,?,?,?,?)");
            playerCommands.setString(1, serverName);
            playerCommands.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            playerCommands.setString(3, player.getWorld().getName());
            playerCommands.setString(4, player.getName());
            playerCommands.setString(5, command);
            playerCommands.setBoolean(6, staff);

            playerCommands.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void insertConsoleCommands(String servername, String commanmd) {
        try {
            PreparedStatement consoleCommands = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Console_Commands(Server_Name, Date, Command) VALUES (?,?,?)");
            consoleCommands.setString(1, servername);
            consoleCommands.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            consoleCommands.setString(3, commanmd);

            consoleCommands.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void insertPlayerSignText(String serverName, Player player, String lines, boolean isStaff) {
        try {
            PreparedStatement playerSignText = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Sign_Text(Server_Name, Date, World, X, Y, Z, Player_Name, Line, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
            playerSignText.setString(1, serverName);
            playerSignText.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            playerSignText.setString(3, player.getWorld().getName());
            playerSignText.setInt(4, player.getLocation().getBlockX());
            playerSignText.setInt(5, player.getLocation().getBlockY());
            playerSignText.setInt(6, player.getLocation().getBlockZ());
            playerSignText.setString(7, player.getName());
            playerSignText.setString(8, lines);
            playerSignText.setBoolean(9, isStaff);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void insertPlayerDeath(String serverName, Player player, boolean isStaff) {
        try {
            PreparedStatement playerDeath = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Deaths(Server_Name, Date, World, Player_Name, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?)");
            playerDeath.setString(1, serverName);
            playerDeath.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            playerDeath.setString(3, player.getWorld().getName());
            playerDeath.setString(4, player.getName());
            playerDeath.setInt(5, player.getLocation().getBlockX());
            playerDeath.setInt(6, player.getLocation().getBlockY());
            playerDeath.setInt(7, player.getLocation().getBlockZ());
            playerDeath.setBoolean(8, isStaff);

            playerDeath.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insertPlayerTeleport(String serverName, Player player, Location from, Location to, boolean isStaff) {
        try {
            PreparedStatement playerTeleport = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Teleports(Server_Name, Date, World, Player_Name, From_X, From_Y, From_Z, To_X, To_Y, To_Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            playerTeleport.setString(1, serverName);
            playerTeleport.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            playerTeleport.setString(3, player.getWorld().getName());
            playerTeleport.setString(4, player.getName());
            playerTeleport.setInt(5, from.getBlockX());
            playerTeleport.setInt(6, from.getBlockY());
            playerTeleport.setInt(7, from.getBlockZ());
            playerTeleport.setInt(8, to.getBlockX());
            playerTeleport.setInt(9, to.getBlockY());
            playerTeleport.setInt(10, to.getBlockZ());
            playerTeleport.setBoolean(11,isStaff);

            playerTeleport.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insertPlayerJoin(String serverName, Player player, boolean isStaff) {
        try {
            PreparedStatement playerJoin = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Join(Server_Name, Date, World, X, Y, Z, IP, Is_Staff) VALUES (?,?,?,?,?,?,?,?)");
            playerJoin.setString(1, serverName);
            playerJoin.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            playerJoin.setString(3, player.getWorld().getName());
            playerJoin.setInt(4, player.getLocation().getBlockX());
            playerJoin.setInt(5, player.getLocation().getBlockY());
            playerJoin.setInt(6, player.getLocation().getBlockZ());
            if (plugin.getConfig().getBoolean("Player-Join.Player-IP")) {
                playerJoin.setString(7, player.getAddress().getHostString());
            }else{
                playerJoin.setString(7, null);
            }
            playerJoin.setBoolean(8, isStaff);



            playerJoin.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insertPlayerLeave(String serverName, Player player, boolean isStaff) {
        try {
            PreparedStatement playerLeave = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Player_Leave(Server_Name, Date, World, X, Y, Z, Player_Name, Is_Staff) VALUES (?,?,?,?,?,?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            playerLeave.setString(3, player.getWorld().getName());
            playerLeave.setInt(4, player.getLocation().getBlockX());
            playerLeave.setInt(5, player.getLocation().getBlockY());
            playerLeave.setInt(6, player.getLocation().getBlockZ());
            playerLeave.setString(7, player.getName());
            playerLeave.setBoolean(8, isStaff);

            playerLeave.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insertBlockPlace(String serverName, Player player, Material block, boolean isStaff) {
        try {
            PreparedStatement blockPlace = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Block_Place(Server_Name, Date, World, Player_Name,Block, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
            blockPlace.setString(1, serverName);
            blockPlace.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            blockPlace.setString(3, player.getWorld().getName());
            blockPlace.setString(4, player.getName());
            blockPlace.setString(5, block.toString());
            blockPlace.setInt(6, player.getLocation().getBlockX());
            blockPlace.setInt(7, player.getLocation().getBlockY());
            blockPlace.setInt(8, player.getLocation().getBlockZ());
            blockPlace.setBoolean(9, isStaff);

            blockPlace.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insertBlockBreak(String serverName, Player player, Material block, boolean isStaff) {
        try {
            PreparedStatement blockBreak = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO Block_Break(Server_Name, Date, World, Player_Name, Block, X, Y, Z, Is_Staff) VALUES (?,?,?,?,?,?,?,?,?)");
            blockBreak.setString(1, serverName);
            blockBreak.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            blockBreak.setString(3, player.getWorld().getName());
            blockBreak.setString(4, player.getName());
            blockBreak.setString(5, block.toString());
            blockBreak.setInt(6, player.getLocation().getBlockX());
            blockBreak.setInt(7, player.getLocation().getBlockY());
            blockBreak.setInt(8, player.getLocation().getBlockZ());
            blockBreak.setBoolean(9, isStaff);


            blockBreak.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insertTPS(String serverName, double tps) {
        try {
            PreparedStatement tpsStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO TPS(Server_Name, Date, TPS) VALUES (?,?,?)");

            tpsStatement.setString(1, serverName);
            tpsStatement.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            tpsStatement.setDouble(3, tps);

            tpsStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insertRAM(String serverName, long totalMemory, long usedMemory, long freeMemory) {
        try {
            PreparedStatement ramStatement = plugin.getSqLite().getConnection().prepareStatement("INSERT INTO RAM(Server_Name, Date, Total_Memory, Used_Memory, Free_Memory) VALUES (?,?,?,?,?)");
            ramStatement.setString(1, serverName);
            ramStatement.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").format(ZonedDateTime.now()));
            ramStatement.setDouble(3, totalMemory);
            ramStatement.setDouble(4, usedMemory);
            ramStatement.setDouble(5, freeMemory);

            ramStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }







    public void emptyTable() {


    }


}
