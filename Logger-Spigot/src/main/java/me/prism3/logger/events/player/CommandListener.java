package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;


/**
 * Listens for player command events and logs them.
 * This class implements the Listener interface to handle events.
 */
public class CommandListener implements Listener {

    private final LoggerAPI plugin;

    public CommandListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the PlayerCommandPreprocessEvent.
     * Logs the command executed by the player, unless the event is cancelled or the player has exempt permissions.
     *
     * @param event the PlayerCommandPreprocessEvent to handle
     */
    @EventHandler
    public void onPlayerCommand(final PlayerCommandPreprocessEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("command", event.getMessage());

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_COMMAND, player, placeholders);
    }
}
