package me.prism3.loggervelocity.managers;

import com.velocitypowered.api.proxy.Player;
import me.prism3.logger_core.objects.LogPlayer;
import me.prism3.loggervelocity.Logger;
import me.prism3.loggervelocity.utils.FileHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class LogManager {

    private final Logger plugin;

    public LogManager(Logger plugin) {
        this.plugin = plugin;
    }

    public void logPlayerEvent(String eventType, Player player, Map<String, String> placeholders, boolean isStaff) {
        String fileTemplate = plugin.getMessages().getString("Files." + (isStaff ? eventType + "-Staff" : eventType));
        if (fileTemplate == null || fileTemplate.startsWith("String at path")) {
            fileTemplate = plugin.getMessages().getString("Files." + eventType);
        }

        String fileMessage = format(fileTemplate, placeholders);

        FileHandler.logToFile(eventType, fileMessage);

        if (plugin.getDiscordManager() != null && plugin.getDiscordManager().isEnabled()) {
            String discordTemplate = plugin.getMessages().getString("Discord." + (isStaff ? eventType + "-Staff" : eventType));
            if (discordTemplate == null || discordTemplate.startsWith("String at path")) {
                discordTemplate = plugin.getMessages().getString("Discord." + eventType);
            }

            if (discordTemplate != null && !discordTemplate.startsWith("String at path")) {
                String discordMessage = format(discordTemplate, placeholders);
                
                String serverName = placeholders.getOrDefault("server", "unknown");
                LogPlayer logPlayer = new LogPlayer(player.getUsername(), player.getUniqueId(), serverName);
                
                plugin.getDiscordManager().sendMessage(eventType, discordMessage, logPlayer, eventType);
            }
        }

        if (plugin.getDatabaseManager() != null && plugin.getDatabaseManager().isEnabled()) {
            String serverName = placeholders.getOrDefault("server", "unknown");
            String tableName = plugin.getDatabaseManager().getTableName("player_events");
            
            final String msgFinal = fileMessage;

            plugin.getDatabaseManager().submit(() -> {
                String sql = "INSERT INTO " + tableName + " (server_name, player_name, event_type, message) VALUES (?, ?, ?, ?)";
                try (Connection conn = plugin.getDatabaseManager().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, serverName);
                    stmt.setString(2, player.getUsername());
                    stmt.setString(3, eventType);
                    stmt.setString(4, msgFinal);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    plugin.getSLF4JLogger().error("Failed to log player event to database: " + e.getMessage());
                }
            });
        }
    }

    public void logServerEvent(String eventType, Map<String, String> placeholders) {
        String fileTemplate = plugin.getMessages().getString("Files." + eventType);
        String fileMessage = format(fileTemplate, placeholders);

        switch (eventType) {
            case "Server-Side.Start":
                FileHandler.logToFile("Server-Side.Start", fileMessage);
                break;
            case "Server-Side.Stop":
                FileHandler.logToFile("Server-Side.Stop", fileMessage);
                break;
            case "Server-Side.Server-Commands":
            case "Server-Side.Console-Commands":
                FileHandler.logToFile("Server-Side.Console-Commands", fileMessage);
                break;
            case "Server-Side.RAM":
                FileHandler.logToFile("Server-Side.RAM", fileMessage);
                break;
            case "Server-Side.Manual-Log":
                FileHandler.logToFile("Manual Log", fileMessage);
                break;
        }

        if (plugin.getDiscordManager() != null && plugin.getDiscordManager().isEnabled()) {
            String discordTemplate = plugin.getMessages().getString("Discord." + eventType);
            if (discordTemplate != null && !discordTemplate.startsWith("String at path")) {
                String discordMessage = format(discordTemplate, placeholders);
                plugin.getDiscordManager().sendMessage(eventType, discordMessage, null, eventType);
            }
        }

        if (plugin.getDatabaseManager() != null && plugin.getDatabaseManager().isEnabled()) {
            String serverName = placeholders.getOrDefault("server", "unknown");
            String tableName = plugin.getDatabaseManager().getTableName("server_events");
            
            final String msgFinal = fileMessage;

            plugin.getDatabaseManager().submit(() -> {
                String sql = "INSERT INTO " + tableName + " (server_name, event_type, message) VALUES (?, ?, ?)";
                try (Connection conn = plugin.getDatabaseManager().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, serverName);
                    stmt.setString(2, eventType);
                    stmt.setString(3, msgFinal);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    plugin.getSLF4JLogger().error("Failed to log server event to database: " + e.getMessage());
                }
            });
        }
    }

    private String format(String template, Map<String, String> placeholders) {
        if (template == null) return "";

        String timePattern = "yyyy-MM-dd HH:mm:ss";
        if (plugin.getConfig() != null) {
            String cfgTime = plugin.getConfig().getString("Time-Formatter");
            if (cfgTime != null && !cfgTime.isEmpty() && !cfgTime.startsWith("String at path")) {
                timePattern = cfgTime;
            }
        }
        String formattedTime;
        try {
            formattedTime = DateTimeFormatter.ofPattern(timePattern).format(ZonedDateTime.now());
        } catch (Exception e) {
            formattedTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(ZonedDateTime.now());
        }
        template = template.replace("%time%", formattedTime);
        template = template.replace("%date%", formattedTime);

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                final String key = entry.getKey();
                final String val = entry.getValue() != null ? entry.getValue() : "";
                template = template.replace("%" + key + "%", val);
                template = template.replace("%" + key.toLowerCase() + "%", val);
                template = template.replace("%" + key.toUpperCase() + "%", val);
                if (key.length() > 1) {
                    String capitalized = Character.toUpperCase(key.charAt(0)) + key.substring(1).toLowerCase();
                    template = template.replace("%" + capitalized + "%", val);
                }
            }
        }

        String serverName = "Velocity";
        if (plugin.getConfig() != null) {
            String cfgServer = plugin.getConfig().getString("Server-Name");
            if (cfgServer != null && !cfgServer.isEmpty() && !cfgServer.startsWith("String at path")) {
                serverName = cfgServer;
            }
        }
        template = template.replace("%proxy%", serverName);
        template = template.replace("%server%", serverName);
        return template;
    }
}
