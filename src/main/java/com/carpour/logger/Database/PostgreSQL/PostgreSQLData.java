package com.carpour.logger.Database.PostgreSQL;

import com.carpour.logger.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgreSQLData {


    private static Main plugin;

    public PostgreSQLData(Main plugin){
        PostgreSQLData.plugin = plugin;
    }

    public void createTable() {
        PreparedStatement playerChat, playerCommands, consoleCommands, playerSignText, playerJoin, playerLeave, playerDeath, playerTeleport, blockPlace, blockBreak, TPS, RAM, playerKick, portalCreation, playerLevel, bucketPlace, anvil, serverStart, serverStop, itemDrop, enchant, bookEditing, afk, itempPickup, furnace;

        try {
            playerChat = plugin.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_chat " +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,message TEXT,is_staff BOOLEAN ,PRIMARY KEY (date))");

            playerCommands = plugin.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_commands "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,command TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

            consoleCommands = plugin.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS console_commands "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,command TEXT,PRIMARY KEY (date))");

            playerSignText = plugin.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_sign_text "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,x INTEGER,y INTEGER,z INTEGER,player_name TEXT,line TEXT,is_staff BOOLEAN,PRIMARY KEY (date))");

            playerJoin = plugin.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_join "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,x INTEGER,y INTEGER,z INTEGER,ip INET,is_staff BOOLEAN,PRIMARY KEY (date))");

            playerLeave = plugin.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_leave "
                    + "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT,player_name TEXT,x INTEGER,y INTEGER,z INTEGER,is_staff BOOLEAN,PRIMARY KEY (date))");

            playerDeath = plugin.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_death " +
                    "(server_name TEXT, date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME, world TEXT,player_name TEXT, x INTEGER, y INTEGER, z INTEGER, cause TEXT, by_who TEXT,item_used TEXT, is_staff BOOLEAN, PRIMARY KEY(date))");

            playerTeleport = plugin.getPostgreSQL().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_teleport" +
                    "(server_name TEXT,date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIME,world TEXT, player_name TEXT, from_x INTEGER,from_Y INTEGER,from_z INTEGER,to_x INTEGER,to_y INTEGER,to_z INTEGER, is_staff BOOLEAN, PRIMARY KEY (date))");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void emptyTable() {
    }
}
