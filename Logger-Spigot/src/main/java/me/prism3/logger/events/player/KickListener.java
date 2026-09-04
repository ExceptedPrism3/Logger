package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.Map;


/**
 * Listens for player kick events and logs them.
 */
public class KickListener implements Listener {

    private final LoggerAPI plugin;

    public KickListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the player kick event.
     *
     * @param event The PlayerKickEvent to handle.
     */
    @EventHandler
    public void onPlayerKick(final PlayerKickEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player)) return;

        final String reason = event.getReason();

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("reason", reason);

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_KICK, player, placeholders);
    }
}
