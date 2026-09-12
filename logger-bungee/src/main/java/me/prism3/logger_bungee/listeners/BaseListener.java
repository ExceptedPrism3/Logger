package me.prism3.logger_bungee.listeners;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Constants;
import me.prism3.logger_bungee.utils.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseListener implements Listener {
    protected final LoggerBungee plugin;

    protected BaseListener(LoggerBungee plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a basic placeholder map with player information
     */
    protected Map<String, String> createPlayerPlaceholders(ProxiedPlayer player) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", player.getName());
        placeholders.put("uuid", player.getUniqueId().toString());
        if (player.getAddress() != null && player.getAddress().getAddress() != null) {
            placeholders.put("IP", player.getAddress().getAddress().getHostAddress());
        } else {
            placeholders.put("IP", "127.0.0.1");
        }
        return placeholders;
    }

    /**
     * Checks if a player has staff logging permission
     */
    protected boolean isStaff(ProxiedPlayer player) {
        return this.plugin.getPermissionManager().isStaff(player);
    }

    /**
     * Logs an event with the given parameters (checks for exempt)
     */
    protected void logEvent(Constants.Events eventType, ProxiedPlayer player, Map<String, String> placeholders) {
        try {
            if (player != null && this.plugin.getPermissionManager().isExempt(player)) {
                return;
            }

            this.plugin.getLogManager().logPlayerEvent(
                    eventType,
                    player,
                    placeholders,
                    isStaff(player));
        } catch (Throwable t) {
            Log.warn("Error processing player event " + eventType + ": " + t.getMessage());
            if (plugin.isDebug()) {
                t.printStackTrace();
            }
        }
    }

    /**
     * Logs a server event (no player involved)
     */
    protected void logServerEvent(Constants.Events eventType, Map<String, String> placeholders) {
        try {
            this.plugin.getLogManager().logServerEvent(eventType, placeholders);
        } catch (Throwable t) {
            Log.warn("Error processing server event " + eventType + ": " + t.getMessage());
            if (plugin.isDebug()) {
                t.printStackTrace();
            }
        }
    }
}
