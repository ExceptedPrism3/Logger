package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import java.util.Map;

/**
 * Listens for when a player uses a totem of undying.
 * <p>
 * This is a special event that is not triggered by the player, but rather by
 * the server.
 * It is triggered when the player dies and the totem is used.
 */
public class TotemUseListener implements Listener {

    private final LoggerAPI plugin;

    public TotemUseListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when a player uses a totem of undying.
     * <p>
     * This is called when the player dies and the totem is used.
     *
     * @param event The event
     */
    @EventHandler
    public void onEntityResurrect(final EntityResurrectEvent event) {

        // only players, only if actually had a totem (not cancelled)
        if (!(event.getEntity() instanceof Player) || event.isCancelled())
            return;
        final Player player = (Player) event.getEntity();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_TOTEM_OF_UNDYING, player, placeholders);
    }
}
