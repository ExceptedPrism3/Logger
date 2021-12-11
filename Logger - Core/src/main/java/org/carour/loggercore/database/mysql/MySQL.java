package org.carour.loggercore.database.mysql;

import net.md_5.bungee.api.ChatColor;
import org.carour.loggercore.util.SqlConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MySQL {


    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String database;
    private Connection connection;

    private final Logger logger;

    public MySQL(SqlConfiguration sqlConfiguration, Logger logger) {
        this.host = sqlConfiguration.getHost();
        this.port = sqlConfiguration.getPort();
        this.username = sqlConfiguration.getUsername();
        this.password = sqlConfiguration.getPassword();
        this.database = sqlConfiguration.getDatabase();

        this.logger = logger;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", username, password);
                    logger.info(ChatColor.GREEN + "MySQL Connection has been established!");

            } catch (SQLException | ClassNotFoundException e) {

                    logger.warning(ChatColor.RED + "Could not connect to the MySQL Database!");

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();

                    logger.info(ChatColor.GREEN + "MySQL Connection has been closed!");

            } catch (SQLException e) {

                    logger.warning(ChatColor.RED + "MySQL Database couldn't be closed safely, if the issue persists contact the Authors!");


            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
