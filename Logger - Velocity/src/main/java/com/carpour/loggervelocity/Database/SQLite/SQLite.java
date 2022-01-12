package com.carpour.loggervelocity.Database.SQLite;

import com.carpour.loggervelocity.Main;
import org.slf4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    private final Logger logger = Main.getInstance().getLogger();

    private static Connection connection;

    private final File databaseFile = new File(Main.getInstance().getFolder().toFile(), "LoggerData.db");

    public static boolean isConnected() { return (connection != null); }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
                logger.info("SQLite Connection has been established!");

            } catch (ClassNotFoundException | SQLException e) {

                logger.error("Couldn't load SQLite Database, if the issue persists contact the Authors!");
                e.printStackTrace();

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                logger.info("SQLite Database has been closed!");

            } catch (SQLException e) {

                logger.error("SQLite Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }

    public Connection getConnection(){ return connection; }

}
