package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Listens for item crafting events and logs them to the database.
 */
// TODO To add if crafted by a physical workbench or player 2 by 2 crafting
public class ItemCraftListener implements Listener {

    private final LoggerAPI plugin;
    private final List<String> itemsToLog;

    public ItemCraftListener(final LoggerAPI plugin) {
        this.plugin = plugin;
        this.itemsToLog = plugin.getData().getItemsToLog();
    }

    /**
     * Handles the item crafting event.
     *
     * @param event The CraftItemEvent to handle.
     */
    @EventHandler
    public void onCraft(final CraftItemEvent event) {

        if (event.isCancelled())
            return;

        if (!(event.getWhoClicked() instanceof Player))
            return;
        final Player player = (Player) event.getWhoClicked();

        if (PermissionManager.isExempt(player))
            return;

        if (event.getRecipe() == null)
            return;

        final ItemStack result = event.getRecipe().getResult();
        final String matName = result.getType().name();

        // only log if the result is in our configured list
        if (!this.itemsToLog.contains(matName))
            return;

        // populate placeholders + craft info
        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("item_type", matName);
        placeholders.put("item_amount", String.valueOf(result.getAmount()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_ITEM_CRAFT, player, placeholders);
    }
}
