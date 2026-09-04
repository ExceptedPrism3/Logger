package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.EnchantFormatter;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.Map;


/**
 * Listens for player item enchant events and logs them.
 */
public class ItemEnchantListener implements Listener {

    private final LoggerAPI plugin;

    public ItemEnchantListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the player item enchant event.
     *
     * @param event The EnchantItemEvent to handle.
     */
    @EventHandler
    public void onItemEnchant(final EnchantItemEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getEnchanter();
        if (player == null)
            return;

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        if (event.getEnchantBlock() != null && event.getEnchantBlock().getLocation() != null) {
            placeholders.put("enchanting_table_x", String.valueOf(event.getEnchantBlock().getLocation().getBlockX()));
            placeholders.put("enchanting_table_y", String.valueOf(event.getEnchantBlock().getLocation().getBlockY()));
            placeholders.put("enchanting_table_z", String.valueOf(event.getEnchantBlock().getLocation().getBlockZ()));
        } else {
            placeholders.put("enchanting_table_x", "0");
            placeholders.put("enchanting_table_y", "0");
            placeholders.put("enchanting_table_z", "0");
        }

        placeholders.put("item", (event.getItem() != null && event.getItem().getType() != null) ? event.getItem().getType().toString() : "AIR");

        final String enchants = (event.getEnchantsToAdd() != null) ? EnchantFormatter.format(event.getEnchantsToAdd()) : "";
        placeholders.put("enchants", enchants);

        placeholders.put("level", String.valueOf(event.getExpLevelCost()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_ITEM_ENCHANTING, player, placeholders);
    }
}
