package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.Map;


/**
 * Listens for player advancement events and logs them.
 * This class implements the Listener interface to handle player advancement events.
 */
public class AdvancementUnlockListener implements Listener {

    private final LoggerAPI plugin;

    public AdvancementUnlockListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles player advancement events.
     * Logs the advancement details if the player is not exempt from logging.
     *
     * @param event the PlayerAdvancementDoneEvent to handle
     */
    @EventHandler
    public void onAdvancementUnlock(final PlayerAdvancementDoneEvent event) {

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player)) return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("advancement", event.getAdvancement().getKey().toString());

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_ADVANCEMENTS, player, placeholders);
    }
}
