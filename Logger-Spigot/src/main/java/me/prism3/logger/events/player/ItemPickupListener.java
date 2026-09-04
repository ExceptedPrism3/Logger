package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.EnchantFormatter;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.Map;


/**
 * Listens for player item pickup events and logs them.
 */
public class ItemPickupListener implements Listener {

    private final LoggerAPI plugin;

    public ItemPickupListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the player item pickup event.
     *
     * @param event The PlayerPickupItemEvent to handle.
     */
    @EventHandler
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        placeholders.put("item", event.getItem().getItemStack().getType().toString());
        placeholders.put("amount", String.valueOf(event.getItem().getItemStack().getAmount()));

        final String enchants = EnchantFormatter.format(event.getItem().getItemStack());
        placeholders.put("enchants", !enchants.isEmpty() ? enchants : "no");

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_ITEM_PICKUP, player, placeholders);
    }
}
