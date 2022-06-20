package me.prism3.loggervelocity.database.sqlite;

import me.prism3.loggervelocity.Main;
import org.slf4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    private final Logger logger = Main.getInstance().getLogger();

    private Connection connection;

    private final File databaseFile = new File(Main.getInstance().getFolder().toFile(), "LoggerData - Velocity.db");

    public boolean isConnected() { return (this.connection != null); }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
                this.logger.info("SQLite Connection has been established!");

            } catch (ClassNotFoundException | SQLException e) {

                this.logger.error("Couldn't load SQLite Database, if the issue persists contact the Authors!");
                e.printStackTrace();

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                this.connection.close();
                this.logger.info("SQLite Database has been closed!");

            } catch (SQLException e) {

                this.logger.error("SQLite Database couldn't be closed safely, if the issue persists contact the Authors!");
                e.printStackTrace();

            }
        }
    }
    public Connection getConnection() { return this.connection; }
}
