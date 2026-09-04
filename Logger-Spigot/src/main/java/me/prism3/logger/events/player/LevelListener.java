package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.util.Map;


/**
 * Listens for player level change events and logs them.
 */
public class LevelListener implements Listener {

    private final LoggerAPI plugin;

    public LevelListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the player level change event.
     *
     * @param event The PlayerLevelChangeEvent to handle.
     */
    @EventHandler
    public void onPlayerLevelChange(final PlayerLevelChangeEvent event) {

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final int newLevel = event.getNewLevel();
        final int levelToLog = plugin.getData().getLevelToLog();

        // If the new level is less than the level to log, do not log it.
        if (newLevel < levelToLog)
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("level", String.valueOf(levelToLog));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_LEVEL, player, placeholders);
    }
}
