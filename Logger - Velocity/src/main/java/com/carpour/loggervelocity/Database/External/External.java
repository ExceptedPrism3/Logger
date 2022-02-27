package com.carpour.loggervelocity.Database.External;

import com.carpour.loggervelocity.Main;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.carpour.loggervelocity.Utils.Data.*;

public class External {

    private final Logger logger = Main.getInstance().getLogger();

    private String jdbc;
    private final String dataType = dbType.toLowerCase();
    private final String host = dbHost;
    private final int port = dbPort;
    private final String username = dbUserName;
    private final String password = dbPassword;
    private final String database = dbName;
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
                this.jdbc = mySQL;
                jdbcDriver = mySQLDriver;
                break;

            case "mariadb":
                this.jdbc = mariaDB;
                jdbcDriver = mariaDBDriver;
                this.logger.warn(this.jdbc + " Type is still in Development. Report any issues on Discord or Github!");
                break;

            default:
                this.logger.error("Unknown Database Type. Available ones are: MySQL and MariaDB.");
                return;

        }

        if (!isConnected()) {

            try {

                Class.forName(jdbcDriver);
                connection = DriverManager.getConnection("jdbc:" + this.jdbc + "://" + this.host + ":" + this.port + "/" + this.database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", this.username, this.password);
                this.logger.info(this.jdbc + " Connection has been established!");

            } catch (SQLException | ClassNotFoundException e) {

                this.logger.error("Could not connect to " + this.jdbc + " Database!");

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                this.logger.info(this.jdbc + " Connection has been closed!");

            } catch (SQLException e) {

                this.logger.error(this.jdbc + " Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }
    public Connection getConnection(){ return connection; }
}
