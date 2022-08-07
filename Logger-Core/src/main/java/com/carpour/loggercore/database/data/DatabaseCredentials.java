package com.carpour.loggercore.database.data;

import lombok.Data;

import java.util.Properties;

@Data

public final class DatabaseCredentials {

    private final String dbType;
    private final String dbHost;
    private final  String dbUsername;
    private final String dbPassword;
    private final String dbName;
    private final int dbPort;
    private final boolean isEnabled;
    public Properties getPropertiesForHib()
    {
        Properties properties = new Properties();

        properties.setProperty("hibernate.connection.url", this.getJdbcUrl());
        properties.setProperty("hibernate.connection.username",  this.getDbUsername());
        properties.setProperty("hibernate.connection.password", this.getDbPassword());
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("show_sql", "true");
        properties.setProperty("hibernate.connection.pool_size", "10");
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
