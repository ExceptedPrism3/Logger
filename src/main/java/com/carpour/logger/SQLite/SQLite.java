/*
package com.carpour.logger.SQLite;

import com.carpour.logger.Main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database{

    private Connection connection;
    String dbname;

    private Main main = Main.getInstance();

    public SQLite(Main instance){
        super(instance);

        dbname = main.getConfig().getString("SQLite.Logs", "PlayerChat"); // Set the table name here e.g player_kills
    }

    private Main plugin = Main.getInstance();

    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS PlayerChat "
            + "(Date VARCHAR(50),World VARCHAR(100),Playername VARCHAR(100),Message VARCHAR(200),Is_Staff VARCHAR(5),PRIMARY KEY (Date))";


    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {

        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");

        if (!dataFolder.exists()){

            try {

                dataFolder.createNewFile();

            } catch (IOException e) {

                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");

            }

        }

        try {

            if(connection!=null && !connection.isClosed()){ return connection; }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;

        } catch (SQLException ex) {

            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);

        } catch (ClassNotFoundException ex) {

            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");

        }
        return null;
    }

    public void load() {

        connection = getSQLConnection();
        try {

            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        initialize();

    }
}*/
