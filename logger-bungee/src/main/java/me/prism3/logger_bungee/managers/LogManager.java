package me.prism3.logger_bungee.managers;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_core.objects.LogPlayer;
import me.prism3.logger_core.utils.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class LogManager {
    private final LoggerBungee plugin;

    public LogManager(LoggerBungee plugin) {
        this.plugin = plugin;
    }

    /**
     * Logs a player event to all configured destinations
     */
    public void logPlayerEvent(me.prism3.logger_bungee.utils.Constants.Events event, ProxiedPlayer player,
            Map<String, String> placeholders,
            boolean isStaff) {
        if (player != null && this.plugin.getPermissionManager().isExempt(player)) {
            return;
        }
        String eventType = event.getConfigKey();
        String folderName = event.getFolderName();

        // Format the message for file logging
        String fileMessage = this.plugin.getMessageManager().formatFileMessage(
                isStaff ? eventType + "-Staff" : eventType,
                placeholders);

        // Log to file
        this.plugin.getFileManager().logToFile(folderName, fileMessage);

        // Log to Discord if enabled
        if (this.plugin.getDiscordManager() != null && this.plugin.getDiscordManager().isEnabled()) {
            if (!this.plugin.getPermissionManager().isExemptDiscord(player)) {
                String discordMessage = this.plugin.getMessageManager().formatDiscordMessage(
                        isStaff ? eventType + "-Staff" : eventType,
                        placeholders);

                // Convert ProxiedPlayer to LogPlayer
                LogPlayer logPlayer = new LogPlayer(player.getName(), player.getUniqueId(),
                        player.getServer() != null ? player.getServer().getInfo().getName() : "unknown");
                this.plugin.getDiscordManager().sendMessage(eventType, discordMessage, logPlayer, eventType);
            }
        }

        // Log to database if enabled
        if (this.plugin.getDatabaseManager() != null && this.plugin.getDatabaseManager().isEnabled()) {
            String serverName = placeholders.getOrDefault("server", "unknown");
            String tableName = this.plugin.getDatabaseManager().getTableName("player_events");

            this.plugin.getDatabaseManager().submit(() -> {
                String sql = "INSERT INTO " + tableName
                        + " (server_name, player_name, event_type, message) VALUES (?, ?, ?, ?)";
                try (Connection conn = this.plugin.getDatabaseManager().getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, serverName);
                    stmt.setString(2, player.getName());
                    stmt.setString(3, eventType);
                    stmt.setString(4, fileMessage);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    Log.severe("Failed to log player event to database: " + e.getMessage());
                }
            });
        }
    }

    /**
     * Logs a server event to all configured destinations
     */
    public void logServerEvent(me.prism3.logger_bungee.utils.Constants.Events event, Map<String, String> placeholders) {
        String eventType = event.getConfigKey();
        String folderName = event.getFolderName();

        // Format the message for file logging
        String fileMessage = this.plugin.getMessageManager().formatFileMessage(eventType, placeholders);

        // Log to file
        this.plugin.getFileManager().logToFile(folderName, fileMessage);

        // Log to Discord if enabled
        if (this.plugin.getDiscordManager() != null && this.plugin.getDiscordManager().isEnabled()) {
            String discordMessage = this.plugin.getMessageManager().formatDiscordMessage(eventType, placeholders);
            this.plugin.getDiscordManager().sendMessage(eventType, discordMessage, null, eventType);
        }

        // Log to database if enabled
        if (this.plugin.getDatabaseManager() != null && this.plugin.getDatabaseManager().isEnabled()) {
            String serverName = placeholders.getOrDefault("server", "unknown"); // Or get from config
            String tableName = this.plugin.getDatabaseManager().getTableName("server_events");

            this.plugin.getDatabaseManager().submit(() -> {
                String sql = "INSERT INTO " + tableName + " (server_name, event_type, message) VALUES (?, ?, ?)";
                try (Connection conn = this.plugin.getDatabaseManager().getConnection();
                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, serverName);
                    stmt.setString(2, eventType);
                    stmt.setString(3, fileMessage);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    Log.severe("Failed to log server event to database: " + e.getMessage());
                }
            });
        }
    }

    /**
     * Convenience method to log a player event with staff check
     */
    public void logPlayerEvent(me.prism3.logger_bungee.utils.Constants.Events event, ProxiedPlayer player,
            Map<String, String> placeholders,
            String permission) {
        boolean isStaff = permission != null && player.hasPermission(permission);
        logPlayerEvent(event, player, placeholders, isStaff);
    }
}