package me.prism3.logger.Database.External;

import me.prism3.logger.Main;
import me.prism3.logger.Utils.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class External {

    private final Main main = Main.getInstance();

    private String jdbc;
    private final String dataType = Data.dbType.toLowerCase();
    private final String host = Data.dbHost;
    private final int port = Data.dbPort;
    private final String username = Data.dbUserName;
    private final String password = Data.dbPassword;
    private final String database = Data.dbName;
    private Connection connection;

    public boolean isConnected(){ return this.connection != null; }

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
                this.main.getLogger().warning(this.jdbc + " Type is still in Development. Report any issues on Discord or Github!");
                break;

            default:
                this.main.getLogger().severe("Unknown Database Type. Available ones are: MySQL and MariaDB.");
                return;

        }

        if (!isConnected()) {

            try {

                Class.forName(jdbcDriver);
                this.connection = DriverManager.getConnection("jdbc:" + this.jdbc + "://" + this.host + ":" + this.port + "/" + this.database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", this.username, this.password);
                this.main.getLogger().info(this.jdbc + " Connection has been established!");

            } catch (SQLException | ClassNotFoundException e) {

                this.main.getLogger().severe("Could not connect to " + this.jdbc + " Database!");

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                this.connection.close();
                this.main.getLogger().info(this.jdbc + " Connection has been closed!");

            } catch (SQLException e) {

                this.main.getLogger().severe(this.jdbc + " Database couldn't be closed safely, if the issue persists contact the Authors!");
                e.printStackTrace();
            }
        }
    }
    public Connection getConnection(){ return this.connection; }
}