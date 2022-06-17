package org.carour.loggercore.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class SqlConfiguration {
    String host;
    int port;
    String username;
    String password;
    String database;

    public SqlConfiguration(String host, int port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }
}
