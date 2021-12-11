package org.carour.loggercore.database.sqlite;

import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLite {
    private Connection connection;
    private final File databaseFile;
    private final Logger logger;

    public SQLite(File dataFolder, Logger logger) {
        databaseFile = new File(dataFolder, "LoggerData.db");
        this.logger = logger;
    }


    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());

                logger.info(ChatColor.GREEN + "SQLite Connection has been established!");


            } catch (ClassNotFoundException | SQLException e) {

                logger.warning(ChatColor.RED + "Couldn't load SQLite Database, if the issue persists contact the Authors!");

                e.printStackTrace();

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                logger.info(ChatColor.GREEN + "SQLite Database has been closed!");

                connection.close();

            } catch (SQLException e) {

                logger.warning(ChatColor.RED + "SQLite Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
