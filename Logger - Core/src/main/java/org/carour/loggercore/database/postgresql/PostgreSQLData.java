package org.carour.loggercore.database.postgresql;

import com.earth2me.essentials.Essentials;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgreSQLData<T extends PostgreSQLTemplate<?>> {

    private static PostgreSQL postgreSQL;
    private static Essentials api;
    private static Plugin bungeePlugin;
    private static Configuration configuration;

    public PostgreSQLData(T t) {
        postgreSQL = t.getPostgreSQL();
        api = t.getAPI();
        if (t.getPlugin() instanceof JavaPlugin) {
            JavaPlugin plugin = (JavaPlugin) t.getPlugin();
        }
    }

    public PostgreSQLData(T t, Configuration configuration) {

        postgreSQL = t.getPostgreSQL();
        api = t.getAPI();

    }

    public void createTable() {
        final PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin, playerLeave, playerDeath, playerTeleport, blockPlace, blockBreak, TPS, RAM, playerKick, portalCreation, playerLevel, bucketPlace, anvil, serverStart, serverStop, itemDrop, enchant, bookEditing, afk, itempPickup, furnace;

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
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void emptyTable() {
    }
}
