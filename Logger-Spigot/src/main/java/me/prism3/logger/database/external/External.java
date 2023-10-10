package me.prism3.logger.database.external;

import com.zaxxer.hikari.HikariDataSource;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.Log;

public class External {

    private final Main main = Main.getInstance();

    private String jdbc;
    private static final String DATATYPE = Data.dbType.toLowerCase();
    private static final String HOST = Data.dbHost;
    private static final int PORT = Data.dbPort;
    private static final String USERNAME = Data.dbUserName;
    private static final String PASSWORD = Data.dbPassword;
    private static final String DATABASE = Data.dbName;
    private HikariDataSource hikari;

    public boolean isConnected() {
        return this.hikari != null;
    }

    public void connect() {

        String jdbcDriver;
        final String mySQL = "MySQL";
        final String mySQLDriver = "com.mysql.cj.jdbc.Driver";
        final String mariaDB = "mariadb";
        final String mariaDBDriver = "org.mariadb.jdbc.Driver";

        switch (DATATYPE) {

            case "mysql":
                this.jdbc = mySQL;
                jdbcDriver = mySQLDriver;
                break;

            case "mariadb":
                this.jdbc = mariaDB;
                jdbcDriver = mariaDBDriver;
                break;

            default:
                Log.severe("Unknown Database Type. Available ones are: MySQL and MariaDB.");
                return;

        }

        if (!isConnected()) {

            hikari = new HikariDataSource();
            hikari.setDriverClassName(jdbcDriver);
            hikari.setJdbcUrl(this.getJdbcUrl());
            hikari.addDataSourceProperty("user", USERNAME);
            hikari.addDataSourceProperty("password", PASSWORD);
            Log.info(this.jdbc + " Connection has been established!");
        }
    }

    public void disconnect() {

        if (isConnected()) {

            this.hikari.close();
            Log.info(this.jdbc + " Connection has been closed!");

        }
    }

    private String getJdbcUrl() {
        return "jdbc:" + this.jdbc + "://" + External.HOST + ":" + External.PORT + "/" + External.DATABASE
                + "?AllowPublicKeyRetrieval=true&useSSL=false&jdbcCompliantTruncation=false";

    }

    public HikariDataSource getHikari() {
        return this.hikari;
    }
}
