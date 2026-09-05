package me.prism3.logger.events.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * WhitelistListener listens for whitelist commands executed via player or console
 * and logs additions, removals, toggles, and reloads.
 */
public class WhitelistListener implements Listener {

    private final LoggerAPI plugin;

    public WhitelistListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Captures whitelist commands executed by players.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerWhitelistCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        if (PermissionManager.isExempt(player)) {
            return;
        }

        // Only log if the player actually has permission to manage whitelist
        if (!player.hasPermission("minecraft.command.whitelist")
                && !player.hasPermission("bukkit.command.whitelist")
                && !player.isOp()) {
            return;
        }

        processWhitelistCommand(event.getMessage(), player);
    }

    /**
     * Captures whitelist commands executed via the server console.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onServerWhitelistCommand(final ServerCommandEvent event) {
        if (event.getSender() instanceof BlockCommandSender) {
            return;
        }

        processWhitelistCommand(event.getCommand(), null);
    }

    private void processWhitelistCommand(final String rawCommand, final Player player) {
        if (rawCommand == null) return;

        String cmd = rawCommand.trim();
        if (cmd.startsWith("/")) {
            cmd = cmd.substring(1).trim();
        }

        String[] parts = cmd.split("\\s+");
        if (parts.length < 2) {
            return;
        }

        String base = parts[0].toLowerCase(Locale.ROOT);
        if (!base.equals("whitelist") && !base.equals("minecraft:whitelist")) {
            return;
        }

        String action = parts[1].toLowerCase(Locale.ROOT);
        String target;

        if (action.equals("add") || action.equals("remove")) {
            target = parts.length >= 3 ? parts[2] : "Unknown";
        } else if (action.equals("on") || action.equals("off") || action.equals("reload")) {
            target = "Server";
        } else {
            // Ignore non-mutation commands (e.g., /whitelist list)
            return;
        }

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("executor", player != null ? player.getName() : "Console");
        placeholders.put("action", action);
        placeholders.put("target", target);
        placeholders.put("command", rawCommand);

        this.plugin.getLoggerManager().logEvent(LogType.SERVER_WHITELIST, player, placeholders);
    }
}
