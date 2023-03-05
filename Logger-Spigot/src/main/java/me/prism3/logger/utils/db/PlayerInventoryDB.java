package me.prism3.logger.utils.db;

import me.prism3.logger.utils.FileHandler;

import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.prism3.logger.utils.playerdeathutils.InventoryToBase64;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import static me.prism3.logger.utils.Data.allowedBackups;

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

        try (final Connection conn = dataSource.getConnection();
             final PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        } catch (final SQLException e) { e.printStackTrace(); }
    }

    public static Map<String, Integer> getPlayerNames() {

        final Map<String, Integer> playerNameCounts = new HashMap<>();

        final String query = "SELECT player_name, COUNT(*) FROM player_inventories GROUP BY player_name";

        try (final Connection conn = dataSource.getConnection();
             final Statement stmt = conn.createStatement()) {

            try (final ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next())
                    playerNameCounts.put(rs.getString("player_name"), rs.getInt("COUNT(*)"));
            }

        } catch (final SQLException e) { e.printStackTrace(); }
        return playerNameCounts;
    }

    public static boolean isAllowed(String playerUUID) {

        final String query = "SELECT COUNT(*) FROM player_inventories WHERE player_uuid = ?";

        try (final Connection conn = dataSource.getConnection();
             final PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, playerUUID);

            try (final ResultSet rs = stmt.executeQuery()) {

                final int numBackups = rs.getInt(1);

                return numBackups < allowedBackups;
            }

        } catch (final SQLException e) { e.printStackTrace(); }

        return false;
    }

    public static List<String> getDetails(String playerName) {

        final List<String> details = new ArrayList<>();

        final String sql = "SELECT cause, world, x, y, z, xp FROM player_inventories WHERE player_name = ? ORDER BY date DESC LIMIT 1";

        try (final Connection conn = dataSource.getConnection();
             final PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, playerName);

            try (final ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    details.add("");
                    details.add(ChatColor.WHITE + "Cause: " + ChatColor.AQUA + rs.getString("cause"));
                    details.add(ChatColor.WHITE + "World: " + ChatColor.AQUA + rs.getString("world"));
                    details.add(ChatColor.WHITE + "X: " + ChatColor.AQUA + rs.getInt("x"));
                    details.add(ChatColor.WHITE + "Y: " + ChatColor.AQUA + rs.getInt("y"));
                    details.add(ChatColor.WHITE + "Z: " + ChatColor.AQUA + rs.getInt("z"));
                    details.add(ChatColor.WHITE + "XP: " + ChatColor.AQUA + rs.getInt("xp"));
                }
            }

        } catch (final SQLException e) { e.printStackTrace(); }

        return details;
    }

    public static int getPlayerBackupCount(String playerName) {

        int backupCount = 0;
        final String query = "SELECT COUNT(*) FROM player_inventories WHERE player_name = ?";

        try (final Connection conn = dataSource.getConnection();
             final PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, playerName);

            try (final ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    backupCount = rs.getInt(1);
            }

        } catch (final SQLException e) { e.printStackTrace(); }
        return backupCount;
    }

    public static List<String> getDistinctPlayerNames() {

        final List<String> playerNames = new ArrayList<>();
        final String query = "SELECT DISTINCT player_name FROM player_inventories";

        try (final Connection conn = dataSource.getConnection();
             final Statement stmt = conn.createStatement()) {

            try (final ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next())
                    playerNames.add(rs.getString("player_name"));
            }

        } catch (final SQLException e) { e.printStackTrace(); }

        return playerNames;
    }

    public static List<String> getAllPlayerBackup(String playerName) {

        final List<String> backupDates = new ArrayList<>();
        final String query = "SELECT date FROM player_inventories WHERE player_name = ? ORDER BY date ASC";

        try (final Connection conn = dataSource.getConnection();
             final PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, playerName);

            try (final ResultSet rs = stmt.executeQuery()) {

                final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                while (rs.next()) {
                    final java.util.Date utilDate = dateFormat.parse(rs.getString("date"));
                    final java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                    backupDates.add(dateFormat.format(sqlDate));
                }
            }
        } catch (final SQLException | ParseException e) { e.printStackTrace(); }
        return backupDates;
    }

    public static String getPlayerInventoryData(final String backupDate) {

        final String query = "SELECT inventory FROM player_inventories WHERE date = ?";

        try (final Connection conn = dataSource.getConnection();
             final PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, backupDate);

            try (final ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("inventory");
            }
        } catch (final SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static String getPlayerArmorData(final String backupDate) {

        final String query = "SELECT armor FROM player_inventories WHERE date = ?";

        try (final Connection conn = dataSource.getConnection();
             final PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, backupDate);

            try (final ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("armor");
            }
        } catch (final SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static void close() { dataSource.close(); }
}
