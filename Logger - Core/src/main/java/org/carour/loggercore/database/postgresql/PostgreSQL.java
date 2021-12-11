package org.carour.loggercore.database.postgresql;
import net.md_5.bungee.api.ChatColor;
import org.carour.loggercore.util.SqlConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PostgreSQL {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String database;
    private Connection connection;

    private final Logger logger;

    public PostgreSQL(SqlConfiguration sqlConfiguration, Logger logger) {

        this.host = sqlConfiguration.getHost();
        this.port = sqlConfiguration.getPort();
        this.username = sqlConfiguration.getUsername();
        this.password = sqlConfiguration.getPassword();
        this.database = sqlConfiguration.getDatabase();

        this.logger = logger;

    }

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + database, username, password);

                    logger.info(ChatColor.GREEN + "PostgreSQL Connection has been established!");

            } catch (SQLException | ClassNotFoundException e) {

                    logger.warning(ChatColor.RED + "Could not connect to the PostgreSQL Database!");



            }
        }

    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();

                    logger.info(ChatColor.GREEN + "PostgreSQL Connection has been closed!");

            } catch (SQLException e) {

                    logger.warning(ChatColor.RED + "PostgreSQL Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }

    public Connection getConnection() {
        return connection;
    }


}
