package com.carpour.loggercore.database.utils;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {

    private DatabaseUtils(){}

    public static boolean checkConnectionIfAlright(HikariDataSource dataSource) {

        try (final Connection connection = dataSource.getConnection();
             final Statement statement = connection.createStatement()) {

            statement.execute("SELECT 1");

        } catch (final SQLException e) { return false; }

        return true;
    }
}
