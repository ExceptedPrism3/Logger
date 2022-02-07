package com.carpour.logger.Database.External;

import com.carpour.logger.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class External {

    private final Main main = Main.getInstance();

    private String jdbc;
    private final String dataType = Objects.requireNonNull(main.getConfig().getString("Database.Type")).toLowerCase();
    private final String host = main.getConfig().getString("Database.Host");
    private final int port = main.getConfig().getInt("Database.Port");
    private final String username = main.getConfig().getString("Database.Username");
    private final String password = main.getConfig().getString("Database.Password");
    private final String database = main.getConfig().getString("Database.Database");
    private Connection connection;

    public boolean isConnected(){ return connection != null; }

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
                main.getLogger().warning(jdbc + " Type is still in Development. Report any issues on Discord or Github!");
            break;

            default:
                main.getLogger().severe("Unknown Database Type. Available ones are: MySQL and MariaDB.");
                return;

        }

        if (!isConnected()) {

            try {

                Class.forName(jdbcDriver);
                connection = DriverManager.getConnection("jdbc:" + jdbc + "://" + host + ":" + port + "/" + database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", username, password);
                main.getLogger().info(jdbc + " Connection has been established!");

            } catch (SQLException | ClassNotFoundException e) {

                main.getLogger().severe("Could not connect to " + jdbc + " Database!");

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                connection.close();
                main.getLogger().info(jdbc + " Connection has been closed!");

            } catch (SQLException e) {

                main.getLogger().severe(jdbc + " Database couldn't be closed safely, if the issue persists contact the Authors!");

            }
        }
    }
    public Connection getConnection(){ return connection; }
}
