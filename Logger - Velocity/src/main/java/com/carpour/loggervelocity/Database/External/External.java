package com.carpour.loggervelocity.Database.External;

import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.ConfigManager;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class External {

    private final ConfigManager config = new ConfigManager();
    private final Logger logger = Main.getInstance().getLogger();

    private String jdbc;
    private final String dataType = Objects.requireNonNull(config.getString("Database.Type")).toLowerCase();
    private final String host = config.getString("Database.Host");
    private final int port = config.getInt("Database.Port");
    private final String username = config.getString("Database.Username");
    private final String password = config.getString("Database.Password");
    private final String database = config.getString("Database.Database");
    private static Connection connection;

    public static boolean isConnected(){ return connection != null; }

    public void connect() {

        String jdbcDriver;
        final String mySQL = "MySQL";
        final String mySQLDriver = "com.mysql.cj.jdbc.Driver";
        final String mariaDB = "MariaDB";
        final String mariaDBDriver = "org.mariadb.jdbc.Driver";

        switch (dataType){

            case "mysql":
                jdbc = mySQL;
                jdbcDriver = mySQLDriver;
                break;

            case "mariadb":
                jdbc = mariaDB;
                jdbcDriver = mariaDBDriver;
                logger.warn(jdbc + " Type is still in Development. Report any issues on Discord or Github!");
                break;

            default:
                logger.error("Unknown Database Type. Available ones are: MySQL and MariaDB.");
                return;

        }

        if (!isConnected()) {

            try {

                Class.forName(jdbcDriver);
                connection = DriverManager.getConnection("jdbc:" + jdbc + "://" + host + ":" + port + "/" + database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", username, password);
                logger.info(jdbc + " Connection has been established!");

            } catch (SQLException | ClassNotFoundException e) {

                logger.error("Could not connect to " + jdbc + " Database!");

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                logger.info(jdbc + " Connection has been closed!");

            } catch (SQLException e) {

                logger.error(jdbc + " Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }
    public Connection getConnection(){ return connection; }
}
