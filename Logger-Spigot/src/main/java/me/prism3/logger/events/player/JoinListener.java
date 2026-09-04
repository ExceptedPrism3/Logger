package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;


/**
 * Listens for player join events and logs them.
 */
public class JoinListener implements Listener {

    private final LoggerAPI plugin;

    public JoinListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the player join event.
     *
     * @param event The PlayerJoinEvent to handle.
     */
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        if (this.plugin.getData().isEnabled(LogType.PLAYER_REGISTRATION))
            this.isPlayerRegistered(player);

        final String playerIP = player.getAddress() != null ? player.getAddress().getHostString() : null;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("ip", plugin.getData().isShowIP() ? playerIP : "");
        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_JOIN, player, placeholders);
    }

    /**
     * Checks if the player is registered and logs the event if not.
     *
     * @param player The player to check.
     */
    private void isPlayerRegistered(final Player player) {

        if (player.hasPlayedBefore())
            return;

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_REGISTRATION, player, null);
    }
}
