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
        placeholders.put("to_x",    String.valueOf(event.getTo().getBlockX()));
        placeholders.put("to_y",    String.valueOf(event.getTo().getBlockY()));
        placeholders.put("to_z",    String.valueOf(event.getTo().getBlockZ()));
        placeholders.put("cause",   cause.name());

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_TELEPORT, player, placeholders);
    }
}
