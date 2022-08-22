package com.carpour.loggercore.database.utils;

import org.hibernate.dialect.MySQLDialect;

public class LocalMySQLDialect extends MySQLDialect {
    @Override
    public String getTableTypeString() { return " DEFAULT CHARSET=utf8"; }

}
