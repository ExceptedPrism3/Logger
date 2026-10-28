package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;


/**
 * Listens for player teleport events and logs them.
 * <p>
 * This includes all teleportation events, including those caused by plugins.
 * <p>
 * The event is ignored if the cause is unknown or caused by a plugin.
 */
public class TeleportListener implements Listener {

    private final LoggerAPI plugin;

    public TeleportListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Called when a player teleports.
     * <p>
     * This includes all teleportation events, including those caused by plugins.
     * <p>
     * The event is ignored if the cause is unknown or caused by a plugin.
     *
     * @param event the teleport event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {

        // Filter out the internal spawn/join teleport
        final PlayerTeleportEvent.TeleportCause cause = event.getCause();

        // Ignore teleportation caused by plugins or unknown causes
        if (cause == PlayerTeleportEvent.TeleportCause.PLUGIN
                || cause == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            return;
        }

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        final String fromX = String.valueOf(event.getFrom().getBlockX());
        final String fromY = String.valueOf(event.getFrom().getBlockY());
        final String fromZ = String.valueOf(event.getFrom().getBlockZ());
        final String toX = String.valueOf(event.getTo() != null ? event.getTo().getBlockX() : 0);
        final String toY = String.valueOf(event.getTo() != null ? event.getTo().getBlockY() : 0);
        final String toZ = String.valueOf(event.getTo() != null ? event.getTo().getBlockZ() : 0);
        placeholders.put("to_x",    toX);
        placeholders.put("to_y",    toY);
        placeholders.put("to_z",    toZ);
        placeholders.put("oldX",    fromX);
        placeholders.put("oldY",    fromY);
        placeholders.put("oldZ",    fromZ);
        placeholders.put("newX",    toX);
        placeholders.put("newY",    toY);
        placeholders.put("newZ",    toZ);
        placeholders.put("cause",   cause.name());

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_TELEPORT, player, placeholders);
    }
}
