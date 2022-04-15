package me.prism3.logger.Database.External;

import me.prism3.logger.API.AuthMeUtil;
import me.prism3.logger.API.EssentialsUtil;
import me.prism3.logger.API.VaultUtil;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.world.PortalCreateEvent;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExternalData {

    private static Main plugin;

    public ExternalData(Main plugin){ ExternalData.plugin = plugin; }

    private static final List<String> tablesNames = Stream.of("Player_Chat", "Player_Commands", "Player_Sign_Text",
            "Player_Death", "Player_Teleport", "Player_Join", "Player_Leave", "Block_Place", "Block_Break",
            "Player_Kick", "Player_Level", "Bucket_Fill", "Bucket_Empty", "Anvil", "Item_Drop", "Enchanting",
            "Book_Editing", "Item_Pickup", "Furnace", "Game_Mode", "Crafting", "Registration", "Server_Start",
            "Server_Stop", "Console_Commands", "RAM", "TPS", "Portal_Creation", "RCON").collect(Collectors.toCollection(ArrayList::new));

    public void createTable(){

        try {
            Statement stsm = plugin.getExternal().getConnection().createStatement();
            // Player Side Part
            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_chat "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Message VARCHAR(200),Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_commands "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Command VARCHAR(256),Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_sign_text "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "X INT,Y INT,Z INT,Player_Name VARCHAR(100),Line VARCHAR(60),Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_death "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100), Player_Level INT, X INT,Y INT,Z INT,Cause VARCHAR(40),By_Who VARCHAR(30)," +
                    "Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_teleport "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),From_X INT,From_Y INT,From_Z INT,To_X INT,To_Y INT,To_Z INT," +
                    "Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_join "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),X INT,Y INT,Z INT,IP INT UNSIGNED,Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_leave "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),X INT,Y INT,Z INT,Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS block_place "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Block VARCHAR(40),X INT,Y INT,Z INT,Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS block_break "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Block VARCHAR(40),X INT,Y INT,Z INT,Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_kick "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),X INT,Y INT,Z INT,Reason VARCHAR(50),Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS player_level "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                    "Player_Name VARCHAR(100),Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_fill "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)" +
                    ",Player_Name VARCHAR(100),Bucket VARCHAR(40),X INT,Y INT,Z INT,Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS bucket_empty "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Bucket VARCHAR(40),X INT,Y INT,Z INT,Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS anvil "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                    "Player_Name VARCHAR(100),New_name VARCHAR(100),Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS item_drop "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)" +
                    ",Player_Name VARCHAR(100),Item VARCHAR(50),Amount INT,X INT,Y INT,Z INT,Enchantment VARCHAR(50)" +
                    ",Changed_Name VARCHAR(50),Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS enchanting "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),X INT,Y INT,Z INT,Enchantment VARCHAR(50), Enchantment_Level INT, " +
                    "Item VARCHAR(50),Cost INT(5),Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS book_editing "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Page_Count INT,Page_Content VARCHAR(250),Signed_by VARCHAR(25)," +
                    "Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS item_pickup "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Item VARCHAR(250),Amount INT,X INT,Y INT,Z INT,Changed_Name VARCHAR(250)," +
                    "Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS furnace "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Item VARCHAR(250),Amount INT,X INT,Y INT,Z INT,Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS game_mode "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Game_Mode VARCHAR(15),Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS crafting "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                    "Player_Name VARCHAR(100),Item VARCHAR(50),Amount INT,X INT,Y INT,Z INT," +
                    "Is_Staff TINYINT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS registration "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30), Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(), Player_Name VARCHAR(30)," +
                    " Player_UUID VARCHAR(80), Join_Date VARCHAR(30))");

            // Server Side Part
            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS server_start "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS server_stop "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP())");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS console_commands "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                    "Command VARCHAR(256))");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS ram "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                    "Total_Memory INT,Used_Memory INT,Free_Memory INT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS tps "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                    "TPS INT)");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS portal_creation "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                    "World VARCHAR(100),Caused_By VARCHAR(50))");

            stsm.executeUpdate("CREATE TABLE IF NOT EXISTS rcon "
                    + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                    "IP INT UNSIGNED,Command VARCHAR(50))");

            // Extras Side Part
            if (EssentialsUtil.getEssentialsAPI() != null) {

                stsm.executeUpdate("CREATE TABLE IF NOT EXISTS afk "
                        + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                        "World VARCHAR(100),Player_Name VARCHAR(100),X INT,Y INT,Z INT,Is_Staff TINYINT," +
                        "PRIMARY KEY (id))");

                tablesNames.add("AFK");
            }

            if (AuthMeUtil.getAuthMeAPI() != null) {

                stsm.executeUpdate("CREATE TABLE IF NOT " +
                        "EXISTS wrong_password (id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30)," +
                        "Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),World VARCHAR(100)," +
                        "Player_Name VARCHAR(100),Is_Staff TINYINT)");

         

                tablesNames.add("Wrong_Password");
            }

            if (VaultUtil.getVaultAPI() && VaultUtil.getVault() != null) {

                stsm.executeUpdate("CREATE TABLE IF NOT EXISTS vault "
                        + "(id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT , Server_Name VARCHAR(30),Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP()," +
                        "Player_Name VARCHAR(100), Old_Balance DOUBLE, New_Balance DOUBLE, " +
                        "Is_Staff TINYINT)");

          

                tablesNames.add("Vault");
            }

            stsm.close();


        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerChat(String serverName, String worldName, String playerName, String msg, boolean staff){

        try {

            final PreparedStatement playerChat = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_chat (Server_Name,World,Player_Name,Message,Is_Staff) VALUES(?,?,?,?,?)");
            playerChat.setString(1, serverName);
            playerChat.setString(2, worldName);
            playerChat.setString(3, playerName);
            playerChat.setString(4, msg);
            playerChat.setBoolean(5, staff);
            
            playerChat.executeUpdate();
            playerChat.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerCommands(String serverName, String worldName, String playerName, String msg, boolean staff){

        try {

            final PreparedStatement playerCommands = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_commands (Server_Name,World,Player_Name,Command,Is_Staff) VALUES(?,?,?,?,?)");
            playerCommands.setString(1, serverName);
            playerCommands.setString(2, worldName);
            playerCommands.setString(3, playerName);
            playerCommands.setString(4, msg);
            playerCommands.setBoolean(5, staff);

            playerCommands.executeUpdate();
            playerCommands.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void consoleCommands(String serverName, String msg){

        try {

            final PreparedStatement consoleCommands = plugin.getExternal().getConnection().prepareStatement("INSERT INTO console_commands (Server_Name,Command) VALUES(?,?)");
            consoleCommands.setString(1, serverName);
            consoleCommands.setString(2, msg);

            consoleCommands.executeUpdate();
            consoleCommands.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerSignText(String serverName, String worldName, int x, int y, int z, String playerName, String Lines, boolean staff){

        try {

            final PreparedStatement playerSignText = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_sign_text (Server_Name,World,X,Y,Z,Player_Name,Line,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            playerSignText.setString(1, serverName);
            playerSignText.setString(2, worldName);
            playerSignText.setInt(3, x);
            playerSignText.setInt(4, y);
            playerSignText.setInt(5, z);
            playerSignText.setString(6, playerName);
            playerSignText.setString(7, Lines);
            playerSignText.setBoolean(8, staff);

            playerSignText.executeUpdate();
            playerSignText.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerDeath(String serverName, String worldName, String playerName, int level, int x, int y, int z, String cause, String who, boolean staff){

        try {

            final PreparedStatement playerDeath= plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_death (Server_Name,World,Player_Name,Player_Level,X,Y,Z,Cause,By_Who,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?,?)");
            playerDeath.setString(1, serverName);
            playerDeath.setString(2, worldName);
            playerDeath.setString(3, playerName);
            playerDeath.setInt(4, level);
            playerDeath.setInt(5, x);
            playerDeath.setInt(6, y);
            playerDeath.setInt(7, z);
            playerDeath.setString(8, cause);
            playerDeath.setString(9, who);
            playerDeath.setBoolean(10, staff);

            playerDeath.executeUpdate();
            playerDeath.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerTeleport(String serverName, String worldName, String playerName, int ox, int oy, int oz, int tx, int ty, int tz, boolean staff){

        try {

            final PreparedStatement playerTeleport = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_teleport (Server_Name,World,Player_Name,From_X,From_Y,From_Z,To_X,To_Y,To_Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?,?)");
            playerTeleport.setString(1, serverName);
            playerTeleport.setString(2, worldName);
            playerTeleport.setString(3, playerName);
            playerTeleport.setInt(4, ox);
            playerTeleport.setInt(5, oy);
            playerTeleport.setInt(6, oz);
            playerTeleport.setInt(7, tx);
            playerTeleport.setInt(8, ty);
            playerTeleport.setInt(9, tz);
            playerTeleport.setBoolean(10, staff);

            playerTeleport.executeUpdate();
            playerTeleport.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerJoin(String serverName, String worldName, String playerName, int x, int y, int z, InetSocketAddress IP, boolean staff) {

        try {

            final PreparedStatement playerJoin = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_join (Server_Name,World,Player_Name,X,Y,Z,IP,Is_Staff) VALUES(?,?,?,?,?,?,INET_ATON(?),?)");
            playerJoin.setString(1, serverName);
            playerJoin.setString(2, worldName);
            playerJoin.setString(3, playerName);
            playerJoin.setInt(4, x);
            playerJoin.setInt(5, y);
            playerJoin.setInt(6, z);
            if (plugin.getConfig().getBoolean("Player-Join.Player-IP")) {
                playerJoin.setString(7, IP.getHostString());
            }else{

                playerJoin.setString(7, null);
            }
            playerJoin.setBoolean(8, staff);

            playerJoin.executeUpdate();
            playerJoin.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void playerLeave(String serverName, String worldName, String playerName, int x, int y, int z, boolean staff){

        try {

            final PreparedStatement playerLeave = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_leave (Server_Name,World,Player_Name,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?)");
            playerLeave.setString(1, serverName);
            playerLeave.setString(2, worldName);
            playerLeave.setString(3, playerName);
            playerLeave.setInt(4, x);
            playerLeave.setInt(5, y);
            playerLeave.setInt(6, z);
            playerLeave.setBoolean(7, staff);

            playerLeave.executeUpdate();
            playerLeave.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void blockPlace(String serverName, String worldName, String playerName, String block, int x, int y, int z, boolean staff){

        try {

            final PreparedStatement blockPlace = plugin.getExternal().getConnection().prepareStatement("INSERT INTO block_place (Server_Name,World,Player_Name,Block,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            blockPlace.setString(1, serverName);
            blockPlace.setString(2, worldName);
            blockPlace.setString(3, playerName);
            blockPlace.setString(4, block);
            blockPlace.setInt(5, x);
            blockPlace.setInt(6, y);
            blockPlace.setInt(7, z);
            blockPlace.setBoolean(8, staff);

            blockPlace.executeUpdate();
            blockPlace.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void blockBreak(String serverName, String worldName, String playerName, String blockname, int x, int y, int z,boolean staff){

        try {

            final PreparedStatement blockBreak = plugin.getExternal().getConnection().prepareStatement("INSERT INTO block_break (Server_Name,World,Player_Name,Block,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            blockBreak.setString(1, serverName);
            blockBreak.setString(2, worldName);
            blockBreak.setString(3, playerName);
            blockBreak.setString(4, blockname);
            blockBreak.setInt(5, x);
            blockBreak.setInt(6, y);
            blockBreak.setInt(7, z);
            blockBreak.setBoolean(8, staff);

            blockBreak.executeUpdate();
            blockBreak.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void tps(String serverName, double tpss){

        try {

            final PreparedStatement tps = plugin.getExternal().getConnection().prepareStatement("INSERT INTO tps (Server_Name,TPS) VALUES(?,?)");
            tps.setString(1, serverName);
            tps.setDouble(2, tpss);

            tps.executeUpdate();
            tps.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void ram(String serverName, long TM, long UM, long FM){

        try {

            final PreparedStatement ram = plugin.getExternal().getConnection().prepareStatement("INSERT INTO ram (Server_Name,Total_Memory,Used_Memory,Free_Memory) VALUES(?,?,?,?)");
            ram.setString(1, serverName);
            ram.setLong(2, TM);
            ram.setLong(3, UM);
            ram.setLong(4, FM);

            ram.executeUpdate();
            ram.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerKick(String serverName, String worldName, String playerName, int x, int y, int z, String reason, boolean staff){

        try {

            final PreparedStatement playerKick = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_kick (Server_Name,World,Player_Name,X,Y,Z,Reason,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            playerKick.setString(1, serverName);
            playerKick.setString(2, worldName);
            playerKick.setString(3, playerName);
            playerKick.setInt(4, x);
            playerKick.setInt(5, y);
            playerKick.setInt(6, z);
            playerKick.setString(7, reason);
            playerKick.setBoolean(8, staff);

            playerKick.executeUpdate();
            playerKick.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void portalCreate(String serverName, String worldName, PortalCreateEvent.CreateReason By){

        try {

            final PreparedStatement portalCreation = plugin.getExternal().getConnection().prepareStatement("INSERT INTO portal_creation (Server_Name,World,Caused_By) VALUES(?,?,?)");
            portalCreation.setString(1, serverName);
            portalCreation.setString(2, worldName);
            portalCreation.setString(3, By.toString());

            portalCreation.executeUpdate();
            portalCreation.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void levelChange(String serverName, String playerName, boolean staff){

        try {

            final PreparedStatement playerLevel = plugin.getExternal().getConnection().prepareStatement("INSERT INTO player_level (Server_Name,Player_Name,Is_Staff) VALUES(?,?,?)");
            playerLevel.setString(1, serverName);
            playerLevel.setString(2, playerName);
            playerLevel.setBoolean(3, staff);

            playerLevel.executeUpdate();
            playerLevel.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void bucketFill(String serverName, String worldName, String playerName, String bucket, int x, int y, int z, boolean staff){

        try {

            final PreparedStatement bucketPlace = plugin.getExternal().getConnection().prepareStatement("INSERT INTO bucket_fill (Server_Name,World,Player_Name,Bucket,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            bucketPlace.setString(1, serverName);
            bucketPlace.setString(2, worldName);
            bucketPlace.setString(3, playerName);
            bucketPlace.setString(4, bucket);
            bucketPlace.setInt(5, x);
            bucketPlace.setInt(6, y);
            bucketPlace.setInt(7, z);
            bucketPlace.setBoolean(8, staff);

            bucketPlace.executeUpdate();
            bucketPlace.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void bucketEmpty(String serverName, String worldName, String playerName, String bucket, int x, int y, int z, boolean staff){

        try {

            final PreparedStatement bucketPlace = plugin.getExternal().getConnection().prepareStatement("INSERT INTO bucket_empty (Server_Name,World,Player_Name,Bucket,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?)");
            bucketPlace.setString(1, serverName);
            bucketPlace.setString(2, worldName);
            bucketPlace.setString(3, playerName);
            bucketPlace.setString(4, bucket);
            bucketPlace.setInt(5, x);
            bucketPlace.setInt(6, y);
            bucketPlace.setInt(7, z);
            bucketPlace.setBoolean(8, staff);

            bucketPlace.executeUpdate();
            bucketPlace.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void anvil(String serverName, String playerName, String newName, boolean staff){

        try {

            final PreparedStatement anvil = plugin.getExternal().getConnection().prepareStatement("INSERT INTO anvil (Server_Name,Player_Name,New_name,Is_Staff) VALUES(?,?,?,?)");
            anvil.setString(1, serverName);
            anvil.setString(2, playerName);
            anvil.setString(3, newName);
            anvil.setBoolean(4, staff);

            anvil.executeUpdate();
            anvil.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStart(String serverName){

        try {

            final PreparedStatement serverStart = plugin.getExternal().getConnection().prepareStatement("INSERT INTO server_start (Server_Name) VALUES(?)");
            serverStart.setString(1, serverName);

            serverStart.executeUpdate();
            serverStart.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void serverStop(String serverName){

        try {

            final PreparedStatement serverStop = plugin.getExternal().getConnection().prepareStatement("INSERT INTO server_stop (Server_Name) VALUES(?)");
            serverStop.setString(1, serverName);

            serverStop.executeUpdate();
            serverStop.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void itemDrop(String serverName, String world, String playerName, String item, int amount, int x, int y, int z, List<String> enchantment, String changedName, boolean staff){

        try {

            final PreparedStatement itemDrop = plugin.getExternal().getConnection().prepareStatement("INSERT INTO item_drop (Server_Name,World,Player_Name,Item,Amount,X,Y,Z,Enchantment,Changed_Name,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            itemDrop.setString(1, serverName);
            itemDrop.setString(2, world);
            itemDrop.setString(3, playerName);
            itemDrop.setString(4, item);
            itemDrop.setInt(5, amount);
            itemDrop.setInt(6, x);
            itemDrop.setInt(7, y);
            itemDrop.setInt(8, z);
            itemDrop.setString(9, String.valueOf(enchantment));
            itemDrop.setString(10, changedName);
            itemDrop.setBoolean(11, staff);

            itemDrop.executeUpdate();
            itemDrop.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void enchant(String serverName, String world, String playerName, int x, int y, int z, List<String> enchantment, int enchantmentLevel, String item, int cost ,boolean staff){

        try {

            final PreparedStatement enchanting = plugin.getExternal().getConnection().prepareStatement("INSERT INTO enchanting (Server_Name,World,Player_Name,X,Y,Z,Enchantment,Enchantment_Level,Item,Cost,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            enchanting.setString(1, serverName);
            enchanting.setString(2, world);
            enchanting.setString(3, playerName);
            enchanting.setInt(4, x);
            enchanting.setInt(5, y);
            enchanting.setInt(6, z);
            enchanting.setString(7, String.valueOf(enchantment));
            enchanting.setInt(8, enchantmentLevel);
            enchanting.setString(9, item);
            enchanting.setInt(10, cost);
            enchanting.setBoolean(11, staff);

            enchanting.executeUpdate();
            enchanting.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void bookEditing(String serverName, String world, String playerName, int pages, List<String> content, String signed_by, boolean staff){

        try {

            final PreparedStatement enchanting = plugin.getExternal().getConnection().prepareStatement("INSERT INTO book_editing (Server_Name,World,Player_Name,Page_Count,Page_Content,Signed_by,Is_Staff) VALUES(?,?,?,?,?,?,?)");
            enchanting.setString(1, serverName);
            enchanting.setString(2, world);
            enchanting.setString(3, playerName);
            enchanting.setInt(4, pages);
            enchanting.setString(5, String.valueOf(content));
            enchanting.setString(6, signed_by);
            enchanting.setBoolean(7, staff);

            enchanting.executeUpdate();
            enchanting.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void afk(String serverName, String world, String playerName, int x, int y, int z,boolean staff){

        if (EssentialsUtil.getEssentialsAPI() != null) {

            try {

                final PreparedStatement afk = plugin.getExternal().getConnection().prepareStatement("INSERT INTO afk (Server_Name,World,Player_Name,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?)");
                afk.setString(1, serverName);
                afk.setString(2, world);
                afk.setString(3, playerName);
                afk.setInt(4, x);
                afk.setInt(5, y);
                afk.setInt(6, z);
                afk.setBoolean(7, staff);

                afk.executeUpdate();
                afk.close();

            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void wrongPassword(String serverName, String world, String playerName,boolean staff){

        if (AuthMeUtil.getAuthMeAPI() != null) {

            try {

                final PreparedStatement wrongPassword = plugin.getExternal().getConnection().prepareStatement("INSERT INTO wrong_password (Server_Name,World,Player_Name,Is_Staff) VALUES(?,?,?,?)");
                wrongPassword.setString(1, serverName);
                wrongPassword.setString(2, world);
                wrongPassword.setString(3, playerName);
                wrongPassword.setBoolean(4, staff);

                wrongPassword.executeUpdate();
                wrongPassword.close();

            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void itemPickup(String serverName, String world, String playerName, Material item, int amount, int x, int y, int z, String changedName, boolean staff){

        try {

            final PreparedStatement itemPickup = plugin.getExternal().getConnection().prepareStatement("INSERT INTO item_pickup (Server_Name,World,Player_Name,Item,Amount,X,Y,Z,Changed_Name,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?,?)");
            itemPickup.setString(1, serverName);
            itemPickup.setString(2, world);
            itemPickup.setString(3, playerName);
            itemPickup.setString(4, String.valueOf(item));
            itemPickup.setInt(5, amount);
            itemPickup.setInt(6, x);
            itemPickup.setInt(7, y);
            itemPickup.setInt(8, z);
            itemPickup.setString(9, changedName);
            itemPickup.setBoolean(10, staff);

            itemPickup.executeUpdate();
            itemPickup.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void furnace(String serverName, String world, String playerName, Material item, int amount, int x, int y, int z, boolean staff){

        try {

            final PreparedStatement furnace = plugin.getExternal().getConnection().prepareStatement("INSERT INTO furnace (Server_Name,World,Player_Name,Item,Amount,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?)");
            furnace.setString(1, serverName);
            furnace.setString(2, world);
            furnace.setString(3, playerName);
            furnace.setString(4, String.valueOf(item));
            furnace.setInt(5, amount);
            furnace.setInt(6, x);
            furnace.setInt(7, y);
            furnace.setInt(8, z);
            furnace.setBoolean(9, staff);

            furnace.executeUpdate();
            furnace.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void rcon(String serverName, String IP, String command) {

        try {

            final PreparedStatement rcon = plugin.getExternal().getConnection().prepareStatement("INSERT INTO rcon (Server_Name,IP,Command) VALUES(?,?,?)");
            rcon.setString(1, serverName);
            rcon.setString(2, IP);
            rcon.setString(3, command);

            rcon.executeUpdate();
            rcon.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void gameMode(String serverName, String world, String playerName, String gameMode, boolean staff) {

        try {

            final PreparedStatement game_Mode = plugin.getExternal().getConnection().prepareStatement("INSERT INTO game_mode (Server_Name,World,Player_Name,Game_Mode,Is_Staff) VALUES(?,?,?,?,?)");
            game_Mode.setString(1, serverName);
            game_Mode.setString(2, world);
            game_Mode.setString(3, playerName);
            game_Mode.setString(4, gameMode);
            game_Mode.setBoolean(5, staff);

            game_Mode.executeUpdate();
            game_Mode.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void playerCraft(String serverName, String world, String playerName, String item, int amount, int x, int y, int z, boolean staff){

        try {

            final PreparedStatement craft = plugin.getExternal().getConnection().prepareStatement("INSERT INTO crafting (Server_Name,World,Player_Name,Item,Amount,X,Y,Z,Is_Staff) VALUES(?,?,?,?,?,?,?,?,?)");
            craft.setString(1, serverName);
            craft.setString(2, world);
            craft.setString(3, playerName);
            craft.setString(4, item);
            craft.setInt(5, amount);
            craft.setInt(6, x);
            craft.setInt(7, y);
            craft.setInt(8, z);
            craft.setBoolean(9, staff);

            craft.executeUpdate();
            craft.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void vault(String serverName, String playerName, double oldBal, double newBal, boolean staff){

        try {

            final PreparedStatement vault = plugin.getExternal().getConnection().prepareStatement("INSERT INTO vault (Server_Name,Player_Name,Old_Balance,New_Balance,Is_Staff) VALUES(?,?,?,?,?)");
            vault.setString(1, serverName);
            vault.setString(2, playerName);
            vault.setDouble(3, oldBal);
            vault.setDouble(4, newBal);
            vault.setBoolean(5, staff);

            vault.executeUpdate();
            vault.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static void playerRegistration(String serverName, Player player, String joinDate){

        try {

            final PreparedStatement register = plugin.getExternal().getConnection().prepareStatement("INSERT INTO registration (Server_Name,Player_Name,Player_UUID,Join_Date) VALUES(?,?,?,?)");
            register.setString(1, serverName);
            register.setString(2, player.getName());
            register.setString(3, String.valueOf(player.getUniqueId()));
            register.setString(4, joinDate);

            register.executeUpdate();
            register.close();

        } catch (SQLException e){ e.printStackTrace(); }
    }
    public static ResultSet getMessages(String playerName, int offset) {
        try {
            
            PreparedStatement getStatement = plugin.getExternal().getConnection().prepareStatement(
                    "SELECT Message, id, Date FROM player_chat WHERE Player_Name=? ORDER BY id DESC, Date DESC LIMIT 10 OFFSET ?");

            getStatement.setString(1, playerName);
            getStatement.setInt(2, offset);

            return getStatement.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }
    public void emptyTable(){

        if (Data.externalDataDel <= 0) return;

        try{
            // Player Side Part
            Statement stsm = plugin.getExternal().getConnection().createStatement();
             stsm.executeUpdate("DELETE FROM player_chat WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM player_commands WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM player_sign_text WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM player_join WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM player_leave WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM player_kick WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM player_death WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM player_teleport WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM player_level WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM block_place WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM block_break WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM bucket_fill WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM bucket_empty WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM anvil WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM item_drop WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM enchanting WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM book_editing WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM item_pickup WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM furnace WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM game_mode WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM crafting WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

             stsm.executeUpdate("DELETE FROM registration WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            // Server Side Part
            stsm.executeUpdate("DELETE FROM server_start WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM server_stop WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM console_commands WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM ram WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM tps WHERE Date < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM portal_creation WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            stsm.executeUpdate("DELETE FROM rcon WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

            // Extras Side Part
            if (EssentialsUtil.getEssentialsAPI() != null) {

                stsm.executeUpdate("DELETE FROM afk WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");


            }

            if (AuthMeUtil.getAuthMeAPI() != null) {

                stsm.executeUpdate("DELETE FROM wrong_password WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

    
            }

            if (VaultUtil.getVaultAPI() && VaultUtil.getVault() != null) {

                stsm.executeUpdate("DELETE FROM vault WHERE DATE < NOW() - INTERVAL " + Data.externalDataDel + " DAY");

           
            }

            stsm.close();

        }catch (SQLException e){

            plugin.getLogger().severe("An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }

    public static List<String> getTableNames() { return tablesNames; }
}