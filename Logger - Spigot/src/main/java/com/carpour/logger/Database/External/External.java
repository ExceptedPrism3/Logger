package com.carpour.logger.Database.External;

import com.carpour.logger.Main;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.carpour.logger.Utils.Data.*;

public class External {

    private final Main main = Main.getInstance();

    private String jdbc;
    private final String dataType = dbType.toLowerCase();
    private final String host = dbHost;
    private final int port = dbPort;
    private final String username = dbUserName;
    private final String password = dbPassword;
    private final String database = dbName;
    private HikariDataSource hikari;

    public boolean isConnected(){ return this.hikari != null; }

    public void connect() {/*

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

        }*/

        if (!isConnected()) {

            hikari = new HikariDataSource();
            hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            hikari.addDataSourceProperty("serverName", host);
            hikari.addDataSourceProperty("port", port);
            hikari.addDataSourceProperty("databaseName", database);
            hikari.addDataSourceProperty("user", username);
            hikari.addDataSourceProperty("password", password);

                /*Class.forName(jdbcDriver);
                this.connection = DriverManager.getConnection("jdbc:" + this.jdbc + "://" + this.host + ":" + this.port + "/" + this.database + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false", this.username, this.password);
                this.main.getLogger().info(this.jdbc + " Connection has been established!");*/

        }
    }

    public void disconnect() {

        if (isConnected()) {

            this.hikari.close();
            this.main.getLogger().info(this.jdbc + " Connection has been closed!");

        }
    }
    public HikariDataSource getHikari(){ return this.hikari; }
}
