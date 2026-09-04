package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.EnchantFormatter;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Map;


/**
 * Listens for player item drop events and logs them.
 */
public class ItemDropListener implements Listener {

    private final LoggerAPI plugin;

    public ItemDropListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the player item drop event.
     *
     * @param event The PlayerDropItemEvent to handle.
     */
    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        // item type & amount
        placeholders.put("item",   event.getItemDrop().getItemStack().getType().toString());
        placeholders.put("amount", String.valueOf(event.getItemDrop().getItemStack().getAmount()));

        // formatted enchantments
        final String enchants = EnchantFormatter.format(event.getItemDrop().getItemStack());
        // if no enchants, set to "no" to avoid null in database
        placeholders.put("enchants", !enchants.isEmpty() ? enchants : "no");

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_ITEM_DROP, player, placeholders);
    }
}
