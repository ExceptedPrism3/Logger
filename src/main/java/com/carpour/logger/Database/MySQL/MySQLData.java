package com.carpour.logger.Database.MySQL;

import com.carpour.logger.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.world.PortalCreateEvent;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLData {

    private static Main plugin;

    public MySQLData(Main plugin){
        MySQLData.plugin = plugin;
    }

    public void createTable(){

        PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin, playerLeave, playerDeath, playerTeleport, blockPlace, blockBreak, TPS, RAM, playerKick, portalCreation, playerLevel, bucketPlace, anvil, serverStart, serverStop, itemDrop, enchant;

        try {

            playerChat = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Message VARCHAR(200),Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerCommands = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Command VARCHAR(256),Is_Staff TINYINT,PRIMARY KEY (Date))");

            consoleCommands = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Console_Commands "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Command VARCHAR(256),PRIMARY KEY (Date))");

            playerSignText = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Sign_Text "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),X INT,Y INT,Z INT,Playername VARCHAR(100),Line VARCHAR(60),Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerDeath = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Death "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT,Y INT,Z INT,Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerTeleport = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Teleport "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),From_X INT,From_Y INT,From_Z INT,To_X INT,To_Y INT,To_Z INT,Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerJoin = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Join "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT,Y INT,Z INT,IP INT UNSIGNED,Is_Staff TINYINT,PRIMARY KEY (Date))");

            playerLeave = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Leave "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT,Y INT,Z INT,Is_Staff TINYINT,PRIMARY KEY (Date))");

            blockPlace = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Place "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Block VARCHAR(40),X INT,Y INT,Z INT,Is_Staff TINYINT,PRIMARY KEY (Date))");

            blockBreak = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Break "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Block VARCHAR(40),X INT,Y INT,Z INT,Is_Staff TINYINT,PRIMARY KEY (Date))");

            TPS = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS TPS "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),TPS INT,PRIMARY KEY (Date))");

            RAM = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Total_Memory INT,Used_Memory INT,Free_Memory INT,PRIMARY KEY (Date))");

            playerKick = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Kick "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT,Y INT,Z INT,Reason VARCHAR(50),Is_Staff TINYINT,PRIMARY KEY (Date))");

            portalCreation = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Portal_Creation "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Caused_By VARCHAR(50),PRIMARY KEY (Date))");

            playerLevel = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Level "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),Is_Staff TINYINT,PRIMARY KEY (Date))");

            bucketPlace = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Bucket_Place "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Bucket VARCHAR(40),X INT,Y INT,Z INT,Is_Staff TINYINT,PRIMARY KEY (Date))");

            anvil = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Anvil "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),New_name VARCHAR(100),Is_Staff TINYINT,PRIMARY KEY (Date))");

            serverStart = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),PRIMARY KEY (Date))");

            serverStop = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Stop "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),PRIMARY KEY (Date))");

            itemDrop = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Item_Drop "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Item VARCHAR(50),Is_Staff TINYINT,PRIMARY KEY (Date))");

            enchant = plugin.mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Enchanting "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT,Y INT,Z INT,Enchantment VARCHAR(50),Item VARCHAR(50),Cost INT(5),Is_Staff TINYINT,PRIMARY KEY (Date))");

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

        }catch (SQLException e){

            e.printStackTrace();

        }
    
    }

    public static void playerChat(String serverName, String worldname, String playername, String msg, boolean staff){
        try {
            String database = "Player_Chat";
            PreparedStatement playerChat = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Message,Is_Staff) VALUES(?,?,?,?,?)");
            playerChat.setString(1, serverName);
            playerChat.setString(2, worldname);
            playerChat.setString(3, playername);
            playerChat.setString(4, msg);
            playerChat.setBoolean(5, staff);
            playerChat.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerCommands(String serverName, String worldname, String playername, String msg, boolean staff){
        try {
            String database = "Player_Commands";
            PreparedStatement playerCommands = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Command,Is_Staff) VALUES(?,?,?,?,?)");
            playerCommands.setString(1, serverName);
            playerCommands.setString(2, worldname);
            playerCommands.setString(3, playername);
            playerCommands.setString(4, msg);
            playerCommands.setBoolean(5, staff);
            playerCommands.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void consoleCommands(String serverName, String msg){
        try {
            String database = "Console_Commands";
            PreparedStatement consoleCommands = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Command) VALUES(?,?)");
            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, msg);
            consoleCommands.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerSignText(String serverName, String worldname, double x, double y, double z, String playername, String Lines, boolean staff){
        try {
            String database = "Player_Sign_Text";
            PreparedStatement playerSignText = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,X,Y,Z,Playername,Line,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            playerSignText.setString(1, serverName);
            playerSignText.setString(2, worldname);
            playerSignText.setDouble(3, x);
            playerSignText.setDouble(4, y);
            playerSignText.setDouble(5, z);
            playerSignText.setString(6, playername);
            playerSignText.setString(7, Lines);
            playerSignText.setBoolean(8, staff);
            playerSignText.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerDeath(String serverName, String worldname, String playername, double x, double y, double z,boolean staff){
        try {
            String database = "Player_Death";
            PreparedStatement playerDeath= plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?)");
            playerDeath.setString(1, serverName);
            playerDeath.setString(2, worldname);
            playerDeath.setString(3, playername);
            playerDeath.setDouble(4, x);
            playerDeath.setDouble(5, y);
            playerDeath.setDouble(6, z);
            playerDeath.setBoolean(7, staff);
            playerDeath.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerTeleport(String serverName, String worldname, String playername, double ox, double oy, double oz, double tx, double ty, double tz, boolean staff){
        try {
            String database = "Player_Teleport";
            PreparedStatement playerTeleport = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,From_X,From_Y,From_Z,To_X,To_Y,To_Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?,?)");
            playerTeleport.setString(1, serverName);
            playerTeleport.setString(2, worldname);
            playerTeleport.setString(3, playername);
            playerTeleport.setDouble(4, ox);
            playerTeleport.setDouble(5, oy);
            playerTeleport.setDouble(6, oz);
            playerTeleport.setDouble(7, tx);
            playerTeleport.setDouble(8, ty);
            playerTeleport.setDouble(9, tz);
            playerTeleport.setBoolean(10, staff);
            playerTeleport.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerJoin(String serverName, String worldname, String playername, double x, double y, double z, InetSocketAddress IP, boolean staff) {
        try {
            String database = "Player_Join";
            PreparedStatement playerJoin = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,IP,Is_Staff) VALUES(?,?,?,?,?,?,INET_ATON(?),?)");
            playerJoin.setString(1, serverName);
            playerJoin.setString(2, worldname);
            playerJoin.setString(3, playername);
            playerJoin.setDouble(4, x);
            playerJoin.setDouble(5, y);
            playerJoin.setDouble(6, z);
            if (plugin.getConfig().getBoolean("Player-Join.Player-IP")) {
                playerJoin.setString(7, IP.getHostString());
            }else{

                playerJoin.setString(7, null);
            }
            playerJoin.setBoolean(8, staff);
            playerJoin.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void playerLeave(String serverName, String worldname, String playername, double x, double y, double z, boolean staff){
        try {
            String database = "Player_Leave";
            PreparedStatement playerLeave = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, worldname);
            playerLeave.setString(3, playername);
            playerLeave.setDouble(4, x);
            playerLeave.setDouble(5, y);
            playerLeave.setDouble(6, z);
            playerLeave.setBoolean(7, staff);
            playerLeave.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void blockPlace(String serverName, String worldname, String playername, String block, double x, double y, double z, boolean staff){
        try {
            String database = "Block_Place";
            PreparedStatement blockPlace = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Block,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            blockPlace.setString(1, serverName);
            blockPlace.setString(2, worldname);
            blockPlace.setString(3, playername);
            blockPlace.setString(4, block);
            blockPlace.setDouble(5, x);
            blockPlace.setDouble(6, y);
            blockPlace.setDouble(7, z);
            blockPlace.setBoolean(8, staff);
            blockPlace.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void blockBreak(String serverName, String worldname, String playername, String blockname, double x, double y, double z,boolean staff){
        try {
            String database = "Block_Break";
            PreparedStatement blockBreak = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Block,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            blockBreak.setString(1, serverName);
            blockBreak.setString(2, worldname);
            blockBreak.setString(3, playername);
            blockBreak.setString(4, blockname);
            blockBreak.setDouble(5, x);
            blockBreak.setDouble(6, y);
            blockBreak.setDouble(7, z);
            blockBreak.setBoolean(8, staff);
            blockBreak.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void TPS(String serverName, double tps){
        try {
            String database = "TPS";
            PreparedStatement TPS = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,TPS) VALUES(?,?)");
            TPS.setString(1, serverName);
            TPS.setDouble(2, tps);
            TPS.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void RAM(String serverName, long TM, long UM, long FM){
        try {
            String database = "RAM";
            PreparedStatement RAM = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Total_Memory,Used_Memory,Free_Memory) VALUES(?,?,?,?)");
            RAM.setString(1, serverName);
            RAM.setDouble(2, TM);
            RAM.setDouble(3, UM);
            RAM.setDouble(4, FM);
            RAM.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerKick(String serverName, String worldname, String playername, double x, double y, double z, String reason, boolean staff){
        try {
            String database = "Player_Kick";
            PreparedStatement playerKick = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,Reason,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            playerKick.setString(1, serverName);
            playerKick.setString(2, worldname);
            playerKick.setString(3, playername);
            playerKick.setDouble(4, x);
            playerKick.setDouble(5, y);
            playerKick.setDouble(6, z);
            playerKick.setString(7, reason);
            playerKick.setBoolean(8, staff);
            playerKick.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void portalCreate(String serverName, String worldname, PortalCreateEvent.CreateReason By){
        try {
            String database = "Portal_Creation";
            PreparedStatement portalCreation = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Caused_By) VALUES(?,?,?)");
            portalCreation.setString(1, serverName);
            portalCreation.setString(2, worldname);
            portalCreation.setString(3, By.toString());
            portalCreation.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void levelChange(String serverName, String playername, boolean staff){
        try {
            String database = "Player_Level";
            PreparedStatement playerLevel = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,Is_Staff) VALUES(?,?,?)");
            playerLevel.setString(1, serverName);
            playerLevel.setString(2, playername);
            playerLevel.setBoolean(3, staff);
            playerLevel.executeUpdate();

        } catch (SQLException e){
            
            e.printStackTrace();
            
        }
    }

    public static void bucketPlace(String serverName, String worldname, String playername, String bucket, double x, double y, double z, boolean staff){
        try {
            String database = "Bucket_Place";
            PreparedStatement bucketPlace = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Bucket,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            bucketPlace.setString(1, serverName);
            bucketPlace.setString(2, worldname);
            bucketPlace.setString(3, playername);
            bucketPlace.setString(4, bucket);
            bucketPlace.setDouble(5, x);
            bucketPlace.setDouble(6, y);
            bucketPlace.setDouble(7, z);
            bucketPlace.setBoolean(8, staff);
            bucketPlace.executeUpdate();

        } catch (SQLException e){
            
            e.printStackTrace();
            
        }
    }

    public static void anvil(String serverName, String playername, String newname, boolean staff){
        try {
            String database = "Anvil";
            PreparedStatement anvil = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,New_name,Is_Staff) VALUES(?,?,?,?)");
            anvil.setString(1, serverName);
            anvil.setString(2, playername);
            anvil.setString(3, newname);
            anvil.setBoolean(4, staff);
            anvil.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public static void serverStart(String serverName){
        try {
            String database = "Server_Start";
            PreparedStatement serverStart = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name) VALUES(?)");
            serverStart.setString(1, serverName);
            serverStart.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public static void serverStop(String serverName){
        try {
            String database = "Server_Stop";
            PreparedStatement serverStop = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name) VALUES(?)");
            serverStop.setString(1, serverName);
            serverStop.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public static void itemDrop(String serverName, String world, String playername, String item, boolean staff){
        try {
            String database = "Item_Drop";
            PreparedStatement itemDrop = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Item,Is_Staff) VALUES(?,?,?,?,?)");
            itemDrop.setString(1, serverName);
            itemDrop.setString(2, world);
            itemDrop.setString(3, playername);
            itemDrop.setString(4, item);
            itemDrop.setBoolean(5, staff);
            itemDrop.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public static void enchant(String serverName, String world, String playername, double x, double y, double z, String enchantment, String item, int cost ,boolean staff){
        try {
            String database = "Enchanting";
            PreparedStatement enchanting = plugin.mySQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,Enchantment,Item,Cost,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?,?)");
            enchanting.setString(1, serverName);
            enchanting.setString(2, world);
            enchanting.setString(3, playername);
            enchanting.setDouble(4, x);
            enchanting.setDouble(5, y);
            enchanting.setDouble(6, z);
            enchanting.setString(7, enchantment);
            enchanting.setString(8, item);
            enchanting.setInt(9, cost);
            enchanting.setBoolean(10, staff);
            enchanting.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public void emptyTable(){

        int When = plugin.getConfig().getInt("MySQL.Data-Deletion");

        if (When <= 0) return;

        try{

            PreparedStatement player_Chat = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Chat WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Commands = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Commands WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement console_Commands = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Console_Commands WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Sign_Text = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Sign_Text WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Join = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Join WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Leave = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Leave WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Kick = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Kick WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Death = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Death WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Teleport = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Teleport WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Level = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Player_Level WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement block_Place = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Block_Place WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement block_Break = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Block_Break WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement portal_Creation = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Portal_Creation WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement bucket_Place = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Bucket_Place WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement anvil = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Anvil WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement TPS = plugin.mySQL.getConnection().prepareStatement("DELETE FROM TPS WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement RAM = plugin.mySQL.getConnection().prepareStatement("DELETE FROM RAM WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement server_Start = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Server_Start WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement server_Stop = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Server_Stop WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement item_Drop = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Item_Drop WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement enchanting = plugin.mySQL.getConnection().prepareStatement("DELETE FROM Enchanting WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            player_Chat.executeUpdate();
            player_Commands.executeUpdate();
            console_Commands.executeUpdate();
            player_Sign_Text.executeUpdate();
            player_Join.executeUpdate();
            player_Leave.executeUpdate();
            player_Kick.executeUpdate();
            player_Death.executeUpdate();
            player_Teleport.executeUpdate();
            player_Level.executeUpdate();
            block_Place.executeUpdate();
            block_Break.executeUpdate();
            portal_Creation.executeUpdate();
            bucket_Place.executeUpdate();
            anvil.executeUpdate();
            TPS.executeUpdate();
            RAM.executeUpdate();
            server_Start.executeUpdate();
            server_Stop.executeUpdate();
            item_Drop.executeUpdate();
            enchanting.executeUpdate();

        }catch (SQLException e){

            System.out.println(ChatColor.RED + "An error has occurred while cleaning the tables, if the error persists, contact the Author!");
            e.printStackTrace();

        }
    }
}