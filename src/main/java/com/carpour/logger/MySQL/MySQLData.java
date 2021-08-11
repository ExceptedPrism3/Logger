package com.carpour.logger.MySQL;

import com.carpour.logger.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.world.PortalCreateEvent;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLData {

    private static Main plugin;

    public MySQLData(Main plugin){
        MySQLData.plugin = plugin;
    }

    public void createTable(){

        PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin, playerLeave, playerDeath, playerTeleport, blockPlace, blockBreak, TPS, RAM, playerKick, portalCreation, playerLevel, bucketPlace, anvil, serverStart, serverStop, itemDrop, enchant;

        try {

            playerChat = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Chat "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Message VARCHAR(200),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            playerCommands = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Commands "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Command VARCHAR(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            consoleCommands = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Console_Commands "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Command VARCHAR(50),PRIMARY KEY (Date))");

            playerSignText = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Sign_Text "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),X INT(50),Y INT(50),Z INT(50),Playername VARCHAR(100),Line VARCHAR(60),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            playerDeath = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Death "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT(50),Y INT(50),Z INT(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            playerTeleport = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Teleport "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),From_X INT(50),From_Y INT(50),From_Z INT(50),To_X INT(50),To_Y INT(50),To_Z INT(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            playerJoin = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Join "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT(50),Y INT(50),Z INT(50),IP VARCHAR(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            playerLeave = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Leave "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT(50),Y INT(50),Z INT(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            blockPlace = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Place "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Block VARCHAR(40),X INT(50),Y INT(50),Z INT(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            blockBreak = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Block_Break "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Block VARCHAR(40),X INT(50),Y INT(50),Z INT(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            TPS = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS TPS "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),TPS INT(20),PRIMARY KEY (Date))");

            RAM = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS RAM "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Total_Memory INT(100),Used_Memory INT(100),Free_Memory INT(100),PRIMARY KEY (Date))");

            playerKick = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Kick "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT(50),Y INT(50),Z INT(50),Reason VARCHAR(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            portalCreation = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Portal_Creation "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Caused_By VARCHAR(50),PRIMARY KEY (Date))");

            playerLevel = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Player_Level "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            bucketPlace = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Bucket_Place "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Bucket VARCHAR(40),X INT(50),Y INT(50),Z INT(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            anvil = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Anvil "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),Playername VARCHAR(100),New_name VARCHAR(100),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            serverStart = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Start "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),PRIMARY KEY (Date))");

            serverStop = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Server_Stop "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),PRIMARY KEY (Date))");

            itemDrop = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Item_Drop "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),Item VARCHAR(50),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

            enchant = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Enchanting "
                    + "(Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100),Playername VARCHAR(100),X INT(50),Y INT(50),Z INT(50),Enchantment VARCHAR(50),Item VARCHAR(50),Cost INT(5),Is_Staff VARCHAR(5),PRIMARY KEY (Date))");

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

    public static void playerChat(String serverName, String worldname, String playername, String msg, String staff){
        try {
            String database = "Player_Chat";
            PreparedStatement Player_Chat = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Message,Is_Staff) VALUES(?,?,?,?,?)");
            Player_Chat.setString(1, serverName);
            Player_Chat.setString(2, worldname);
            Player_Chat.setString(3, playername);
            Player_Chat.setString(4, msg);
            Player_Chat.setString(5, staff);
            Player_Chat.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerCommands(String serverName, String worldname, String playername, String msg, String staff){
        try {
            String database = "Player_Commands";
            PreparedStatement Player_Commands = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Command,Is_Staff) VALUES(?,?,?,?,?)");
            Player_Commands.setString(1, serverName);
            Player_Commands.setString(2, worldname);
            Player_Commands.setString(3, playername);
            Player_Commands.setString(4, msg);
            Player_Commands.setString(5, staff);
            Player_Commands.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void consoleCommands(String serverName, String msg){
        try {
            String database = "Console_Commands";
            PreparedStatement Console_Commands = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Command) VALUES(?,?)");
            Console_Commands.setString(1, serverName);
            Console_Commands.setString(2, msg);
            Console_Commands.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerSignText(String serverName, String worldname, double x, double y, double z, String playername, String Lines, String staff){
        try {
            String database = "Player_Sign_Text";
            PreparedStatement Player_Sign_Text = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,X,Y,Z,Playername,Line,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            Player_Sign_Text.setString(1, serverName);
            Player_Sign_Text.setString(2, worldname);
            Player_Sign_Text.setDouble(3, x);
            Player_Sign_Text.setDouble(4, y);
            Player_Sign_Text.setDouble(5, z);
            Player_Sign_Text.setString(6, playername);
            Player_Sign_Text.setString(7, Lines);
            Player_Sign_Text.setString(8, staff);
            Player_Sign_Text.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerDeath(String serverName, String worldname, String playername, double x, double y, double z,String staff){
        try {
            String database = "Player_Death";
            PreparedStatement Player_Death= plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?)");
            Player_Death.setString(1, serverName);
            Player_Death.setString(2, worldname);
            Player_Death.setString(3, playername);
            Player_Death.setDouble(4, x);
            Player_Death.setDouble(5, y);
            Player_Death.setDouble(6, z);
            Player_Death.setString(7, staff);
            Player_Death.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerTeleport(String serverName, String worldname, String playername, double ox, double oy, double oz, double tx, double ty, double tz, String staff){
        try {
            String database = "Player_Teleport";
            PreparedStatement Player_Teleport = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,From_X,From_Y,From_Z,To_X,To_Y,To_Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?,?)");
            Player_Teleport.setString(1, serverName);
            Player_Teleport.setString(2, worldname);
            Player_Teleport.setString(3, playername);
            Player_Teleport.setDouble(4, ox);
            Player_Teleport.setDouble(5, oy);
            Player_Teleport.setDouble(6, oz);
            Player_Teleport.setDouble(7, tx);
            Player_Teleport.setDouble(8, ty);
            Player_Teleport.setDouble(9, tz);
            Player_Teleport.setString(10, staff);
            Player_Teleport.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerJoin(String serverName, String worldname, String playername, double x, double y, double z, InetSocketAddress IP, String staff){
        try {
            String database = "Player_Join";
            PreparedStatement Player_Join = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,IP,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            Player_Join.setString(1, serverName);
            Player_Join.setString(2, worldname);
            Player_Join.setString(3, playername);
            Player_Join.setDouble(4, x);
            Player_Join.setDouble(5, y);
            Player_Join.setDouble(6, z);
            Player_Join.setString(7, IP.toString());
            Player_Join.setString(8, staff);
            Player_Join.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerLeave(String serverName, String worldname, String playername, double x, double y, double z, String staff){
        try {
            String database = "Player_Leave";
            PreparedStatement Player_Leave = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?)");
            Player_Leave.setString(1, serverName);
            Player_Leave.setString(2, worldname);
            Player_Leave.setString(3, playername);
            Player_Leave.setDouble(4, x);
            Player_Leave.setDouble(5, y);
            Player_Leave.setDouble(6, z);
            Player_Leave.setString(7, staff);
            Player_Leave.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void blockPlace(String serverName, String worldname, String playername, String block, double x, double y, double z, String staff){
        try {
            String database = "Block_Place";
            PreparedStatement Block_Place = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Block,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            Block_Place.setString(1, serverName);
            Block_Place.setString(2, worldname);
            Block_Place.setString(3, playername);
            Block_Place.setString(4, block);
            Block_Place.setDouble(5, x);
            Block_Place.setDouble(6, y);
            Block_Place.setDouble(7, z);
            Block_Place.setString(8, staff);
            Block_Place.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void blockBreak(String serverName, String worldname, String playername, String blockname, double x, double y, double z,String staff){
        try {
            String database = "Block_Break";
            PreparedStatement Block_Break = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Block,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            Block_Break.setString(1, serverName);
            Block_Break.setString(2, worldname);
            Block_Break.setString(3, playername);
            Block_Break.setString(4, blockname);
            Block_Break.setDouble(5, x);
            Block_Break.setDouble(6, y);
            Block_Break.setDouble(7, z);
            Block_Break.setString(8, staff);
            Block_Break.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void TPS(String serverName, double tps){
        try {
            String database = "TPS";
            PreparedStatement TPS = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,TPS) VALUES(?,?)");
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
            PreparedStatement RAM = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Total_Memory,Used_Memory,Free_Memory) VALUES(?,?,?,?)");
            RAM.setString(1, serverName);
            RAM.setDouble(2, TM);
            RAM.setDouble(3, UM);
            RAM.setDouble(4, FM);
            RAM.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void playerKick(String serverName, String worldname, String playername, double x, double y, double z, String reason, String staff){
        try {
            String database = "Player_Kick";
            PreparedStatement Player_Kick = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,Reason,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            Player_Kick.setString(1, serverName);
            Player_Kick.setString(2, worldname);
            Player_Kick.setString(3, playername);
            Player_Kick.setDouble(4, x);
            Player_Kick.setDouble(5, y);
            Player_Kick.setDouble(6, z);
            Player_Kick.setString(7, reason);
            Player_Kick.setString(8, staff);
            Player_Kick.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void portalCreate(String serverName, String worldname, PortalCreateEvent.CreateReason By){
        try {
            String database = "Portal_Creation";
            PreparedStatement Portal_Creation = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Caused_By) VALUES(?,?,?)");
            Portal_Creation.setString(1, serverName);
            Portal_Creation.setString(2, worldname);
            Portal_Creation.setString(3, By.toString());
            Portal_Creation.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void levelChange(String serverName, String playername, String staff){
        try {
            String database = "Player_Level";
            PreparedStatement Player_Level = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,Is_Staff) VALUES(?,?,?)");
            Player_Level.setString(1, serverName);
            Player_Level.setString(2, playername);
            Player_Level.setString(3, staff);
            Player_Level.executeUpdate();

        } catch (SQLException e){
            
            e.printStackTrace();
            
        }
    }

    public static void bucketPlace(String serverName, String worldname, String playername, String bucket, double x, double y, double z, String staff){
        try {
            String database = "Bucket_Place";
            PreparedStatement Bucket_Place = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Bucket,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            Bucket_Place.setString(1, serverName);
            Bucket_Place.setString(2, worldname);
            Bucket_Place.setString(3, playername);
            Bucket_Place.setString(4, bucket);
            Bucket_Place.setDouble(5, x);
            Bucket_Place.setDouble(6, y);
            Bucket_Place.setDouble(7, z);
            Bucket_Place.setString(8, staff);
            Bucket_Place.executeUpdate();

        } catch (SQLException e){
            
            e.printStackTrace();
            
        }
    }

    public static void anvil(String serverName, String playername, String newname, String staff){
        try {
            String database = "Anvil";
            PreparedStatement Anvil = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,Playername,New_name,Is_Staff) VALUES(?,?,?,?)");
            Anvil.setString(1, serverName);
            Anvil.setString(2, playername);
            Anvil.setString(3, newname);
            Anvil.setString(4, staff);
            Anvil.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public static void serverStart(String serverName){
        try {
            String database = "Server_Start";
            PreparedStatement Server_Start = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name) VALUES(?)");
            Server_Start.setString(1, serverName);
            Server_Start.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public static void serverStop(String serverName){
        try {
            String database = "Server_Stop";
            PreparedStatement Server_Stop = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name) VALUES(?)");
            Server_Stop.setString(1, serverName);
            Server_Stop.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public static void itemDrop(String serverName, String world, String playername, String item, String staff){
        try {
            String database = "Item_Drop";
            PreparedStatement Item_Drop = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,Item,Is_Staff) VALUES(?,?,?,?,?)");
            Item_Drop.setString(1, serverName);
            Item_Drop.setString(2, world);
            Item_Drop.setString(3, playername);
            Item_Drop.setString(4, item);
            Item_Drop.setString(5, staff);
            Item_Drop.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public static void enchant(String serverName, String world, String playername, double x, double y, double z, String enchantment, String item, int cost ,String staff){
        try {
            String database = "Enchanting";
            PreparedStatement Enchanting = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + database + "(Server_Name,World,Playername,X,Y,Z,Enchantment,Item,Cost,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?,?)");
            Enchanting.setString(1, serverName);
            Enchanting.setString(2, world);
            Enchanting.setString(3, playername);
            Enchanting.setDouble(4, x);
            Enchanting.setDouble(5, y);
            Enchanting.setDouble(6, z);
            Enchanting.setString(7, enchantment);
            Enchanting.setString(8, item);
            Enchanting.setInt(9, cost);
            Enchanting.setString(10, staff);
            Enchanting.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }

    public void emptyTable(){

        int When = plugin.getConfig().getInt("MySQL.Data-Deletion");

        if (When <= 0) return;

        try{

            PreparedStatement player_Chat = plugin.SQL.getConnection().prepareStatement("DELETE FROM Player_Chat WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Commands = plugin.SQL.getConnection().prepareStatement("DELETE FROM Player_Commands WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement console_Commands = plugin.SQL.getConnection().prepareStatement("DELETE FROM Console_Commands WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Sign_Text = plugin.SQL.getConnection().prepareStatement("DELETE FROM Player_Sign_Text WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Join = plugin.SQL.getConnection().prepareStatement("DELETE FROM Player_Join WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Leave = plugin.SQL.getConnection().prepareStatement("DELETE FROM Player_Leave WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Kick = plugin.SQL.getConnection().prepareStatement("DELETE FROM Player_Kick WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Death = plugin.SQL.getConnection().prepareStatement("DELETE FROM Player_Death WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Teleport = plugin.SQL.getConnection().prepareStatement("DELETE FROM Player_Teleport WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement player_Level = plugin.SQL.getConnection().prepareStatement("DELETE FROM Player_Level WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement block_Place = plugin.SQL.getConnection().prepareStatement("DELETE FROM Block_Place WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement block_Break = plugin.SQL.getConnection().prepareStatement("DELETE FROM Block_Break WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement portal_Creation = plugin.SQL.getConnection().prepareStatement("DELETE FROM Portal_Creation WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement bucket_Place = plugin.SQL.getConnection().prepareStatement("DELETE FROM Bucket_Place WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement anvil = plugin.SQL.getConnection().prepareStatement("DELETE FROM Anvil WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement TPS = plugin.SQL.getConnection().prepareStatement("DELETE FROM TPS WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement RAM = plugin.SQL.getConnection().prepareStatement("DELETE FROM RAM WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement server_Start = plugin.SQL.getConnection().prepareStatement("DELETE FROM Server_Start WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement server_Stop = plugin.SQL.getConnection().prepareStatement("DELETE FROM Server_Stop WHERE DATE < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement item_Drop = plugin.SQL.getConnection().prepareStatement("DELETE FROM Item_Drop WHERE Date < NOW() - INTERVAL " + When + " DAY");

            PreparedStatement enchanting = plugin.SQL.getConnection().prepareStatement("DELETE FROM Enchanting WHERE DATE < NOW() - INTERVAL " + When + " DAY");

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
