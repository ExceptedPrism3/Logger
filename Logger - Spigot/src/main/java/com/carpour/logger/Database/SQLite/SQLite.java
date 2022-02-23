package com.carpour.logger.Database.SQLite;

import com.carpour.logger.Main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    private final Main main = Main.getInstance();

    private Connection connection;

    private final File databaseFile = new File(this.main.getDataFolder(), "LoggerData.db");

    public boolean isConnected() {
        return (this.connection != null);
    }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
                this.main.getLogger().info("SQLite Connection has been established!");

            } catch (ClassNotFoundException | SQLException e) {

                this.main.getLogger().severe("Couldn't load SQLite Database, if the issue persists contact the Authors!");
                e.printStackTrace();

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                this.connection.close();
                this.main.getLogger().info("SQLite Database has been closed!");

            } catch (SQLException e) {

                this.main.getLogger().severe("SQLite Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }

    public Connection getConnection(){ return this.connection; }

}
