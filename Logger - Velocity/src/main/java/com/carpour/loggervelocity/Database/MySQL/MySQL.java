package com.carpour.loggervelocity.Database.MySQL;

import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.ConfigManager;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private final ConfigManager config = new ConfigManager();

    private final Logger logger = Main.getInstance().getLogger();

    private final String host = config.getString("MySQL.Host");
    private final int port = config.getInt("MySQL.Port");
    private final String username = config.getString("MySQL.Username");
    private final String password = config.getString("MySQL.Password");
    private final String database = config.getString("MySQL.Database");
    private static Connection connection;

    public static boolean isConnected(){ return connection != null; }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", username, password);
                logger.info("MySQL Connection has been established!");

            } catch (SQLException | ClassNotFoundException e) {

                logger.error("Could not connect to the MySQL Database!");

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                logger.info("MySQL Connection has been closed!");

            } catch (SQLException e) {

                logger.error("MySQL Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }

    public Connection getConnection(){ return connection; }

}
