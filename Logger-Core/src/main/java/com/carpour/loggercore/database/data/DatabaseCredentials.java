package com.carpour.loggercore.database.data;


import java.util.Properties;

public final class DatabaseCredentials {

    public String getDbType() {
        return dbType;
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public int getDbPort() {
        return dbPort;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public DatabaseCredentials(String dbType, String dbHost, String dbUsername, String dbPassword, String dbName, int dbPort, boolean isEnabled) {
        this.dbType = dbType;
        this.dbHost = dbHost;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbName = dbName;
        this.dbPort = dbPort;
        this.isEnabled = isEnabled;
    }

    private final String dbType;
    private final String dbHost;
    private final  String dbUsername;
    private final String dbPassword;
    private final String dbName;
    private final int dbPort;
    private final boolean isEnabled;

    public Properties getPropertiesForHib() {

        final Properties properties = new Properties();

        properties.setProperty("hibernate.connection.url", this.getJdbcUrl());
        properties.setProperty("hibernate.connection.username",  this.getDbUsername());
        properties.setProperty("hibernate.connection.password", this.getDbPassword());
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.connection.CharSet", "utf8");

        properties.setProperty("hibernate.connection.characterEncoding", "utf8");

        properties.setProperty("hibernate.connection.useUnicode", "true");

        properties.setProperty("hibernate.generate_statistics", "true");
        properties.setProperty("hibernate.connection.provider_class","org.hibernate.hikaricp.internal.HikariCPConnectionProvider");

        //properties.setProperty("hibernate.connection.autocommit", "true");

       return properties;
    }

    private String getJdbcUrl() {
        return ("jdbc:"+this.dbType+"://"
                + this.getDbHost()
                + ":" + this.getDbPort()
                + "/" + this.getDbName());
    }
}
