package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;


/**
 * Listens for player chat events and logs them.
 * This class implements the Listener interface to handle chat events.
 */
public class ChatListener implements Listener {

    private final LoggerAPI plugin;

    public ChatListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the AsyncPlayerChatEvent.
     * Logs the chat message and player information if the event is not cancelled and the player is not exempt.
     *
     * @param event the AsyncPlayerChatEvent to handle
     */
    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("message", event.getMessage());

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_CHAT, player, placeholders);
    }
}
