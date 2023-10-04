package me.prism3.logger.database.sqlite.global;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;

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
                Log.info("SQLite Connection has been established!");

            } catch (ClassNotFoundException | SQLException e) {

                Log.severe("Couldn't load SQLite Database, if the issue persists contact the Authors!");
                e.printStackTrace();

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                this.connection.close();
                Log.info("SQLite Database has been closed!");

            } catch (SQLException e) {

                Log.severe("SQLite Database couldn't be closed safely, if the issue persists contact the Authors!");
                e.printStackTrace();

            }
        }
    }

    public Connection getConnection() { return this.connection; }

}
