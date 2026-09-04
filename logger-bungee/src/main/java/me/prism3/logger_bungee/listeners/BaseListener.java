package me.prism3.logger_bungee.listeners;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Constants;
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
        placeholders.put("IP", player.getAddress().getAddress().getHostAddress());
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
        if (this.plugin.getPermissionManager().isExempt(player)) {
            return;
        }

        this.plugin.getLogManager().logPlayerEvent(
                eventType,
                player,
                placeholders,
                isStaff(player));
    }

    /**
     * Logs a server event (no player involved)
     */
    protected void logServerEvent(Constants.Events eventType, Map<String, String> placeholders) {
        this.plugin.getLogManager().logServerEvent(eventType, placeholders);
    }
}
