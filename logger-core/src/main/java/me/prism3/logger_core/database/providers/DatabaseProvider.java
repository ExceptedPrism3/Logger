package me.prism3.logger_core.database.providers;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseProvider {
    void initialize() throws SQLException, ClassNotFoundException;
    Connection getConnection() throws SQLException;
    void close() throws SQLException;
}
