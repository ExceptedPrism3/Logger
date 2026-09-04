package me.prism3.logger_core.database;

public class DatabaseConfig {
    public final boolean enabled;
    public final String type;
    public final String host;
    public final int port;
    public final String database;
    public final String username;
    public final String password;
    public final String tablePrefix;
    public final int dataDeletion;

    public DatabaseConfig(boolean enabled, String type, String host, int port, String database, String username, String password, String tablePrefix, int dataDeletion) {
        this.enabled = enabled;
        this.type = type;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.tablePrefix = tablePrefix;
        this.dataDeletion = dataDeletion;
    }
}
