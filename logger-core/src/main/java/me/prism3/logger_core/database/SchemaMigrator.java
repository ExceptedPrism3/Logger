package me.prism3.logger_core.database;

import me.prism3.logger_core.utils.Log;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Universal Database Schema Migrator and Auto-Evolution Engine.
 * Automatically aligns legacy tables with modern schema requirements
 * without dropping or modifying historical data.
 */
public class SchemaMigrator {

    public static void migrate(Connection conn, DatabaseConfig config, Map<String, String> tables) {
        if (conn == null || config == null || tables == null) return;

        boolean isSqlite = "sqlite".equalsIgnoreCase(config.type);
        int columnsAdded = 0;
        int indexesCreated = 0;

        try {
            DatabaseMetaData meta = conn.getMetaData();

            // First, migrate any legacy v1.6 / v1.7 table names to modern standard names
            migrateLegacyTableNames(conn, meta, config, isSqlite);

            for (String rawTableName : tables.keySet()) {
                String fullTableName = config.tablePrefix + rawTableName;

                // Check if table exists in database
                if (!tableExists(meta, fullTableName)) {
                    continue;
                }

                // Inspect existing columns
                Set<String> existingColumns = getExistingColumns(meta, fullTableName);
                boolean isPlayerTable = rawTableName.startsWith("player_");

                // 1. Check 'server_name'
                if (!existingColumns.contains("server_name")) {
                    if (addColumn(conn, fullTableName, "server_name", "VARCHAR(100) DEFAULT 'default_server'")) {
                        columnsAdded++;
                    }
                }

                // 2. Check 'is_staff'
                if (isPlayerTable && !existingColumns.contains("is_staff")) {
                    String colType = isSqlite ? "INTEGER DEFAULT 0" : "TINYINT(1) DEFAULT 0";
                    if (addColumn(conn, fullTableName, "is_staff", colType)) {
                        columnsAdded++;
                    }
                }

                // 3. Check 'world_name'
                if (isPlayerTable && !rawTableName.equals("player_events") && !existingColumns.contains("world_name")) {
                    if (addColumn(conn, fullTableName, "world_name", "VARCHAR(100) DEFAULT 'world'")) {
                        columnsAdded++;
                    }
                }

                // 4. Check player coordinates
                if (isPlayerTable && !rawTableName.equals("player_events")) {
                    if (!existingColumns.contains("location_x")) {
                        if (addColumn(conn, fullTableName, "location_x", "INT DEFAULT 0")) columnsAdded++;
                    }
                    if (!existingColumns.contains("location_y")) {
                        if (addColumn(conn, fullTableName, "location_y", "INT DEFAULT 0")) columnsAdded++;
                    }
                    if (!existingColumns.contains("location_z")) {
                        if (addColumn(conn, fullTableName, "location_z", "INT DEFAULT 0")) columnsAdded++;
                    }
                }

                // 5. Index Acceleration (Date & Player Name)
                if (createIndexSafely(conn, isSqlite, fullTableName, "idx_" + rawTableName + "_date", "date")) {
                    indexesCreated++;
                }
                if (isPlayerTable && existingColumns.contains("player_name")) {
                    if (createIndexSafely(conn, isSqlite, fullTableName, "idx_" + rawTableName + "_player", "player_name")) {
                        indexesCreated++;
                    }
                }
            }

            if (columnsAdded > 0 || indexesCreated > 0) {
                Log.info("Database auto-migration complete: " + columnsAdded + " missing columns added, " + indexesCreated + " indexes optimized.");
            } else {
                Log.info("Database schema verified: up-to-date.");
            }

        } catch (Exception e) {
            Log.warn("Non-fatal error during schema auto-migration: " + e.getMessage());
        }
    }

    private static void migrateLegacyTableNames(Connection conn, DatabaseMetaData meta, DatabaseConfig config, boolean isSqlite) {
        Map<String, String> legacyToModern = new java.util.HashMap<>();
        legacyToModern.put("Player_Chat", "player_chat");
        legacyToModern.put("Player_Commands", "player_command");
        legacyToModern.put("Player_Sign_Text", "player_sign_interaction");
        legacyToModern.put("Player_Death", "player_death");
        legacyToModern.put("Player_Teleport", "player_teleport");
        legacyToModern.put("Player_Join", "player_join");
        legacyToModern.put("Player_Leave", "player_leave");
        legacyToModern.put("Block_Place", "player_block_place");
        legacyToModern.put("Block_Break", "player_block_break");
        legacyToModern.put("Player_Kick", "player_kick");
        legacyToModern.put("Player_Level", "player_level");
        legacyToModern.put("Bucket_Fill", "player_bucket_fill");
        legacyToModern.put("Bucket_Empty", "player_bucket_empty");
        legacyToModern.put("Anvil", "player_anvil");
        legacyToModern.put("Item_Drop", "player_item_drop");
        legacyToModern.put("Enchanting", "player_item_enchant");
        legacyToModern.put("Book_Editing", "player_book_interaction");
        legacyToModern.put("Item_Pickup", "player_item_pickup");
        legacyToModern.put("Furnace", "player_furnace_interaction");
        legacyToModern.put("Game_Mode", "player_gamemode");
        legacyToModern.put("Crafting", "player_item_craft");
        legacyToModern.put("Registration", "player_registration");
        legacyToModern.put("Server_Start", "server_start");
        legacyToModern.put("Server_Stop", "server_stop");
        legacyToModern.put("Console_Commands", "server_console_command");
        legacyToModern.put("RAM", "server_ram");
        legacyToModern.put("TPS", "server_tps");
        legacyToModern.put("Portal_Creation", "player_portal_creation");
        legacyToModern.put("RCON", "server_rcon_command");

        for (Map.Entry<String, String> entry : legacyToModern.entrySet()) {
            String legacyTable = config.tablePrefix + entry.getKey();
            String modernTable = config.tablePrefix + entry.getValue();

            try {
                if (tableExists(meta, legacyTable) && !legacyTable.equalsIgnoreCase(modernTable)) {
                    if (!tableExists(meta, modernTable)) {
                        String renameSql = isSqlite 
                                ? "ALTER TABLE " + legacyTable + " RENAME TO " + modernTable + ";"
                                : "RENAME TABLE " + legacyTable + " TO " + modernTable + ";";
                        try (Statement stmt = conn.createStatement()) {
                            stmt.executeUpdate(renameSql);
                            Log.info("Migrated legacy table " + legacyTable + " -> " + modernTable);
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    private static boolean tableExists(DatabaseMetaData meta, String tableName) throws SQLException {
        try (ResultSet rs = meta.getTables(null, null, tableName, null)) {
            if (rs.next()) return true;
        }
        // Try uppercase for Oracle/PostgreSQL compatibility
        try (ResultSet rs = meta.getTables(null, null, tableName.toUpperCase(), null)) {
            if (rs.next()) return true;
        }
        // Try lowercase for MySQL on Linux
        try (ResultSet rs = meta.getTables(null, null, tableName.toLowerCase(), null)) {
            return rs.next();
        }
    }

    private static Set<String> getExistingColumns(DatabaseMetaData meta, String tableName) throws SQLException {
        Set<String> columns = new HashSet<>();
        try (ResultSet rs = meta.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME").toLowerCase());
            }
        }
        if (columns.isEmpty()) {
            try (ResultSet rs = meta.getColumns(null, null, tableName.toUpperCase(), null)) {
                while (rs.next()) {
                    columns.add(rs.getString("COLUMN_NAME").toLowerCase());
                }
            }
        }
        if (columns.isEmpty()) {
            try (ResultSet rs = meta.getColumns(null, null, tableName.toLowerCase(), null)) {
                while (rs.next()) {
                    columns.add(rs.getString("COLUMN_NAME").toLowerCase());
                }
            }
        }
        return columns;
    }

    private static boolean addColumn(Connection conn, String tableName, String columnName, String columnDefinition) {
        String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition + ";";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            Log.info("Auto-migrated table " + tableName + ": added column " + columnName);
            return true;
        } catch (SQLException e) {
            // Column may already exist or ALTER not permitted
            return false;
        }
    }

    private static boolean createIndexSafely(Connection conn, boolean isSqlite, String tableName, String indexName, String columnName) {
        try (Statement stmt = conn.createStatement()) {
            String sql;
            if (isSqlite) {
                sql = "CREATE INDEX IF NOT EXISTS " + indexName + " ON " + tableName + " (" + columnName + ");";
            } else {
                // MySQL / MariaDB / Postgres
                sql = "CREATE INDEX " + indexName + " ON " + tableName + " (" + columnName + ");";
            }
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            // Index already exists, ignore
            return false;
        }
    }
}
