package com.carpour.loggercore.database.data;

import lombok.Data;

@Data
public final class DatabaseCredentials {

    private final String dbType;
    private final String dbHost;
    private final  String dbUsername;
    private final String dbPassword;
    private final String dbName;
    private final int dbPort;
    private final boolean isEnabled;


    
}
