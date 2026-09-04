package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;


/**
 * Listens for player leave events and logs them.
 */
public class LeaveListener implements Listener { //TODO TO SEPERATE THIS FROM PLAYER KICK

    private final LoggerAPI plugin;

    public LeaveListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the player leave event.
     *
     * @param event The PlayerQuitEvent to handle.
     */
    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent event) {

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_LEAVE, player, placeholders);
    }
}
