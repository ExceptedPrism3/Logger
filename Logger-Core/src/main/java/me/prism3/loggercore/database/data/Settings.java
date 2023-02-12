package me.prism3.loggercore.database.data;

import java.util.Properties;

public final class Settings {

    private final String dbType;
    private final String dbHost;
    private final String dbUsername;
    private final String dbPassword;
    private final String dbName;
    private final int dbPort;
    private final boolean isEnabled;

    public Settings(String dbType, String dbHost, String dbUsername, String dbPassword,
                    String dbName, int dbPort, boolean isEnabled) {

        this.dbType = dbType;
        this.dbHost = dbHost;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbName = dbName;
        this.dbPort = dbPort;
        this.isEnabled = isEnabled;
    }

    public String getDbType() {
        return this.dbType;
    }

    public String getDbHost() {
        return this.dbHost;
    }

    public String getDbUsername() {
        return this.dbUsername;
    }

    public String getDbPassword() {
        return this.dbPassword;
    }

    public String getDbName() {
        return this.dbName;
    }

    public int getDbPort() {
        return this.dbPort;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }


    public String getJdbcUrl() {
        return "jdbc:" + this.dbType + "://" + this.dbHost + ":" + this.dbPort + "/" + this.dbName
                + "?AllowPublicKeyRetrieval=true?useSSL=false&jdbcCompliantTruncation=false";

    }

    @Override
    public String toString() {
        return "Settings{" +
                "dbType='" + dbType + '\'' +
                ", dbHost='" + dbHost + '\'' +
                ", dbUsername='" + dbUsername + '\'' +
                ", dbPassword='" + dbPassword + '\'' +
                ", dbName='" + dbName + '\'' +
                ", dbPort=" + dbPort +
                ", isEnabled=" + isEnabled +
                '}' + "JDBC = "+ getJdbcUrl();
    }
}
