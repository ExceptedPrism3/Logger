package me.prism3.logger.utils.db;

import me.prism3.logger.utils.FileHandler;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.prism3.logger.utils.playerdeathutils.InventoryToBase64;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryDB {

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS player_inventories (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "date DATE DEFAULT (datetime('now','localtime'))," +
            "player_uuid TEXT, " +
            "player_name TEXT, " +
            "world TEXT, " +
            "cause TEXT, " +
            "x INTEGER, " +
            "y INTEGER, " +
            "z INTEGER, " +
            "xp INTEGER, " +
            "inventory TEXT, " +
            "armor TEXT)";

    private static HikariDataSource dataSource;

    public PlayerInventoryDB() {

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + FileHandler.getDatabasesFolder() + File.separator + "player_inventories.db");

        dataSource = new HikariDataSource(config);

        try (final Connection conn = dataSource.getConnection()) {
            try (final Statement stmt = conn.createStatement()) { stmt.executeUpdate(CREATE_TABLE_QUERY); }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void insertInventory(String playerUUID, String playerName, String world, String cause, int x, int y, int z, int xp, ItemStack[] inventory, ItemStack[] armor) {

        final String sql = "INSERT INTO player_inventories (player_uuid, player_name, world, cause, x, y, z, xp, inventory, armor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (final Connection conn = dataSource.getConnection()) {
            try (final PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, playerUUID);
                stmt.setString(2, playerName);
                stmt.setString(3, world);
                stmt.setString(4, cause);
                stmt.setInt(5, x);
                stmt.setInt(6, y);
                stmt.setInt(7, z);
                stmt.setInt(8, xp);
                stmt.setObject(9, InventoryToBase64.toBase64(inventory));
                stmt.setObject(10, InventoryToBase64.toBase64(armor));

                stmt.executeUpdate();
            }
        } catch (final SQLException e) { e.printStackTrace(); }
    }

    public void close() { dataSource.close(); }

    public static Map<String, Integer> getPlayerNameCounts() {

        final Map<String, Integer> playerNameCounts = new HashMap<>();

        final String query = "SELECT player_name, COUNT(*) FROM player_inventories GROUP BY player_name";

        try (final Connection conn = dataSource.getConnection();
             final Statement stmt = conn.createStatement();
             final ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                playerNameCounts.put(rs.getString("player_name"), rs.getInt("COUNT(*)"));
            }
        } catch (final SQLException e) { e.printStackTrace(); }
        return playerNameCounts;
    }
}
