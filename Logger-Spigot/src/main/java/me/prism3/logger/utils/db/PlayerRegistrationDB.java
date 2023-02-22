package me.prism3.logger.utils.db;

import me.prism3.logger.utils.FileHandler;

import java.io.File;
import java.sql.*;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;

public class PlayerRegistrationDB {

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS registration (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "date DATE DEFAULT (datetime('now', 'localtime'))," +
            "player_uuid TEXT, " +
            "player_name TEXT)";

    private static HikariDataSource dataSource;

    public PlayerRegistrationDB() {

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + FileHandler.getDatabasesFolder() + File.separator + "registration.db");

        dataSource = new HikariDataSource(config);

        try (final Connection conn = dataSource.getConnection()) {
            try (final Statement stmt = conn.createStatement()) { stmt.executeUpdate(CREATE_TABLE_QUERY); }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertRegistration(final Player player) {

        final String sql = "INSERT INTO registration (player_uuid, player_name) VALUES (?,?)";

        try (final Connection conn = dataSource.getConnection()) {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, player.getUniqueId().toString());
                stmt.setString(2, player.getName());

                stmt.executeUpdate();
            }
        } catch (final SQLException e) { e.printStackTrace(); }
    }

    public static boolean playerExists(final Player player) {

        final String sql = "SELECT * FROM registration WHERE player_uuid=? LIMIT 1";

        try (final Connection conn = dataSource.getConnection()) {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, player.getUniqueId().toString());
                final ResultSet results = stmt.executeQuery();

                if (results.next())
                    return true;
            }
        } catch (final SQLException e) { e.printStackTrace(); }

        return false;
    }

    public static void close() { dataSource.close(); }

    public static HikariDataSource getDataSource() { return dataSource; }
}
