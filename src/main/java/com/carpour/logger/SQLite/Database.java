/*
package com.carpour.logger.SQLite;

import com.carpour.logger.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;


public abstract class Database {

    Main plugin;
    Connection connection;
    // The name of the table we created back in SQLite class.
    public String table = "PlayerChat";
    public Database(Main instance){

        plugin = instance;

    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){

        connection = getSQLConnection();

        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE Playername = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);

        } catch (SQLException ex) {

            plugin.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);

        }

    }

    // These are the methods you can use to get things out of your database. You of course can make new ones to return different things in the database.
    // This returns the number of people the player killed.
    public String getPlayerChatLogs(String string) {

        Connection conn;
        PreparedStatement PlayerChat;
        ResultSet rs;
        try {

            conn = getSQLConnection();
            PlayerChat = conn.prepareStatement("SELECT * FROM " + table + " WHERE Playername = '"+string+"';");

            rs = PlayerChat.executeQuery();
            while(rs.next()){
                if(rs.getString("Playername").equalsIgnoreCase(string.toLowerCase())){ // Tell database to search for the player you sent into the method. e.g getTokens(sam) It will look for sam.
                    return rs.getString("Message"); // Return the players amount of kills. If you wanted to get total (just a random number for an example for you guys) You would change this to total!
                }
            }

        } catch (SQLException ex) {

            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);

        }
        return string;
    }

    // Now we need methods to save things to the database
    public void PlayerChat(String date, String worldname, String playername, String msg, String staff){

        Connection conn;
        PreparedStatement PlayerChat;

        try {
            conn = getSQLConnection();
            String database = "PlayerChat";
            PlayerChat = conn.prepareStatement("INSERT IGNORE INTO " + database + "(Date,World,Playername,Message,Is_Staff) VALUES(?,?,?,?,?)");
            PlayerChat.setString(1, date);
            PlayerChat.setString(2, worldname);
            PlayerChat.setString(3, playername);
            PlayerChat.setString(4, msg);
            PlayerChat.setString(5, staff);
            PlayerChat.executeUpdate();

        } catch (SQLException e){

            e.printStackTrace();

        }
    }


    public void close(PreparedStatement ps,ResultSet rs){
        try {

            if (ps != null)
                ps.close();

            if (rs != null)
                rs.close();

        } catch (SQLException ex) {

            Error.close(plugin, ex);

        }
    }
}*/
