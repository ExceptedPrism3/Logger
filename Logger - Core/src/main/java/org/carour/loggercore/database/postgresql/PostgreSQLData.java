package org.carour.loggercore.database.postgresql;

import com.earth2me.essentials.Essentials;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgreSQLData<T extends PostgreSQLTemplate<?>> {

    private static PostgreSQL postgreSQL;
    private static Essentials api;
    private static Plugin bungeePlugin;
    private static Configuration configuration;
    private static JavaPlugin plugin;

    public PostgreSQLData(T t) {
        postgreSQL = t.getPostgreSQL();
        api = t.getAPI();
        if (t.getPlugin() instanceof JavaPlugin) {
            plugin = (JavaPlugin) t.getPlugin();
        }
    }

    public PostgreSQLData(T t, Configuration configuration) {

        postgreSQL = t.getPostgreSQL();
        api = t.getAPI();

    }

    public void createTable() {
        final PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin, playerLeave, playerDeath, playerTeleport, blockPlace, blockBreak, TPS, RAM, playerKick, portalCreation, playerLevel, bucketPlace, anvil, serverStart, serverStop, itemDrop, enchant, bookEditing, afk, itemPickup, furnace;

        try {
            playerChat = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_chat " +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,message TEXT,is_staff BOOLEAN ,PRIMARY KEY (date))");

            playerCommands = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_commands "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,command TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

            consoleCommands = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS console_commands "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,command TEXT,PRIMARY KEY (date))");

            playerSignText = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_sign_text "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,x INTEGER,y INTEGER,z INTEGER,player_name TEXT,line TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

            playerJoin = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_join "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,x INTEGER,y INTEGER,z INTEGER,ip INET,is_staff BOOLEAN,PRIMARY KEY (date))");

            playerLeave = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_leave "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,x INTEGER,y INTEGER,z INTEGER,is_staff BOOLEAN,PRIMARY KEY (date))");

            playerDeath = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_death " +
                    "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME, world TEXT,player_name TEXT, x INTEGER, y INTEGER, z INTEGER, cause TEXT, by_who TEXT,item_used TEXT, is_staff BOOLEAN, PRIMARY KEY(date))");

            playerTeleport = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_teleport" +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT, player_name TEXT, from_x INTEGER,from_Y INTEGER,from_z INTEGER,to_x INTEGER,to_y INTEGER,to_z INTEGER, is_staff BOOLEAN, PRIMARY KEY (date))");

            blockPlace = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS block_place" +
                    "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME, world TEXT, player_name TEXT, block TEXT,x INTEGER,y INTEGER,z INTEGER, is_staff BOOLEAN,PRIMARY KEY (date))");

            blockBreak = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS block_break" +
                    "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT, block TEXT,x INTEGER,y INTEGER, z INTEGER, is_staff BOOLEAN,PRIMARY KEY (date))");

            TPS = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tps" +
                    "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME, tps INTEGER, PRIMARY KEY (date))");

            RAM = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ram" +
                    "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME, total_memory INTEGER, used_memory INTEGER, free_memory INTEGER, PRIMARY KEY (date))");

            playerKick = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_kick" +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,x INTEGER,y INTEGER,z INTEGER,reason TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

            portalCreation = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS portal_creation" +
                    "(server_name TEXT ,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,caused_by TEXT,PRIMARY KEY (date))");

            playerLevel = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_level" +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,player_name TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

            bucketPlace = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS bucket_place" +
                    "(Server_Name VARCHAR(30),Date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,bucket TEXT,x INTEGER,y INTEGER,z INTEGER,is_staff BOOLEAN,PRIMARY KEY (date))");

            anvil = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS anvil" +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,player_name TEXT,new_name TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

            serverStart = postgreSQL.getConnection().prepareStatement("CREATE  TABLE IF NOT EXISTS server_start" +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,PRIMARY KEY (date))");

            serverStop = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS server_stop" +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,PRIMARY KEY (date))");

            itemDrop = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS item_drop" +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,item TEXT,amount INTEGER,x INTEGER,y INTEGER,z INTEGER,enchantment TEXT,changed_name TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

            enchant = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS enchanting "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,x INTEGER,y INTEGER,z INTEGER,enchantment TEXT,item TEXT,cost INTEGER,is_staff BOOLEAN,PRIMARY KEY (date))");

            bookEditing = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS book_editing "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,page_count INTEGER,page_content TEXT,signed_by TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

            if (api != null) {
                afk = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS afk "
                        + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,x INTEGER,y INTEGER,z INTEGER,is_staff BOOLEAN,PRIMARY KEY (date))");

                afk.executeUpdate();
            }

            itemPickup = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS item_pickup "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,item TEXT,amount INTEGER,X INTEGER,y INTEGER,z INTEGER,changed_name TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");


            furnace = postgreSQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS furnace "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,item TEXT,amount INTEGER,x INTEGER,y INTEGER,z INTEGER,is_staff BOOLEAN,PRIMARY KEY (date))");

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
            bookEditing.executeUpdate();
            enchant.executeUpdate();
            itemPickup.executeUpdate();
            furnace.executeUpdate();


        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }



/*
    public void emptyTable() {

        int when = plugin.getConfig().getInt("MySQL.Data-Deletion");

        if (when <= 0) return;

        try {

            PreparedStatement player_Chat = postgreSQL.getConnection().prepareStatement("DELETE FROM player_chat WHERE date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Commands = postgreSQL.getConnection().prepareStatement("DELETE FROM Player_Commands WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement console_Commands = postgreSQL.getConnection().prepareStatement("DELETE FROM Console_Commands WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Sign_Text = postgreSQL.getConnection().prepareStatement("DELETE FROM Player_Sign_Text WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Join = postgreSQL.getConnection().prepareStatement("DELETE FROM Player_Join WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Leave = postgreSQL.getConnection().prepareStatement("DELETE FROM Player_Leave WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Kick = postgreSQL.getConnection().prepareStatement("DELETE FROM Player_Kick WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Death = postgreSQL.getConnection().prepareStatement("DELETE FROM Player_Death WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Teleport = postgreSQL.getConnection().prepareStatement("DELETE FROM player_teleport WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement player_Level = postgreSQL.getConnection().prepareStatement("DELETE FROM Player_Level WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement block_Place = postgreSQL.getConnection().prepareStatement("DELETE FROM Block_Place WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement block_Break = postgreSQL.getConnection().prepareStatement("DELETE FROM Block_Break WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement portal_Creation = postgreSQL.getConnection().prepareStatement("DELETE FROM Portal_Creation WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement bucket_Place = postgreSQL.getConnection().prepareStatement("DELETE FROM Bucket_Place WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement anvil = postgreSQL.getConnection().prepareStatement("DELETE FROM Anvil WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement TPS = postgreSQL.getConnection().prepareStatement("DELETE FROM TPS WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement RAM = postgreSQL.getConnection().prepareStatement("DELETE FROM RAM WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement server_Start = postgreSQL.getConnection().prepareStatement("DELETE FROM Server_Start WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement server_Stop = postgreSQL.getConnection().prepareStatement("DELETE FROM Server_Stop WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement item_Drop = postgreSQL.getConnection().prepareStatement("DELETE FROM Item_Drop WHERE Date < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement enchanting = postgreSQL.getConnection().prepareStatement("DELETE FROM Enchanting WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement book_Editing = postgreSQL.getConnection().prepareStatement("DELETE FROM Book_Editing WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            if (api != null) {

                PreparedStatement afk = postgreSQL.getConnection().prepareStatement("DELETE FROM AFK WHERE DATE < NOW() - INTERVAL " + when + " DAY");

                afk.executeUpdate();
            }

            PreparedStatement item_pickup = postgreSQL.getConnection().prepareStatement("DELETE FROM Item_Pickup WHERE DATE < NOW() - INTERVAL " + when + " DAY");

            PreparedStatement furnace = postgreSQL.getConnection().prepareStatement("DELETE FROM Furnace WHERE DATE < NOW() - INTERVAL " + when + " DAY");

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
            book_Editing.executeUpdate();
            item_pickup.executeUpdate();
            furnace.executeUpdate();

        } catch (SQLException e) {

            plugin.getLogger().warning(ChatColor.RED + "An error has occurred while cleaning the tables, if the error persists, contact the Authors!");
            e.printStackTrace();

        }
    }
*/
}
