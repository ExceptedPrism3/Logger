package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Listens for player interactions with anvils and logs relevant information.
 * This includes renaming or repairing items.
 */
public class AnvilInteractionListener implements Listener {

    private static final int ANVIL_OUTPUT_SLOT = 2; // Anvil output slot index
    private final LoggerAPI plugin;

    public AnvilInteractionListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles player interactions with anvils.
     * Logs the action if the player is not exempt from logging.
     *
     * @param event the InventoryClickEvent to handle
     */
    @EventHandler
    public void onPlayerAnvil(final InventoryClickEvent event) {

        if (event.isCancelled())
            return;

        if (!(event.getWhoClicked() instanceof Player))
            return;
        final Player player = (Player) event.getWhoClicked();

        if (PermissionManager.isExempt(player))
            return;

        final Inventory inventory = event.getInventory();
        if (inventory.getType() != org.bukkit.event.inventory.InventoryType.ANVIL)
            return;
        if (!(inventory instanceof AnvilInventory))
            return;
        final AnvilInventory anvil = (AnvilInventory) inventory;
        if (event.getRawSlot() != ANVIL_OUTPUT_SLOT)
            return;

        final ItemStack inputItem = anvil.getItem(0); // base item
        final ItemStack result = event.getCurrentItem(); // output item

        if (inputItem == null || result == null || !result.hasItemMeta())
            return;
        if (!inputItem.getType().isItem())
            return;

        final String itemType = result.getType().toString();

        // Determine old and new names.
        String oldName = null;

        if (inputItem.hasItemMeta() && inputItem.getItemMeta().hasDisplayName()) {
            oldName = inputItem.getItemMeta().getDisplayName();
        }

        String outputName = null;

        if (result.hasItemMeta() && result.getItemMeta().hasDisplayName()) {
            outputName = result.getItemMeta().getDisplayName();
        }

        final boolean isRename = (outputName != null) && (oldName == null || !outputName.equals(oldName));

        // Check for repair (durability change).
        final boolean isRepair = inputItem.getType().getMaxDurability() > 0
                && inputItem.getDurability() > result.getDurability();

        // Determine the action.
        final String action;
        if (isRepair && isRename) {
            action = "repaired_and_renamed";
        } else if (isRepair) {
            action = "repaired";
        } else if (isRename) {
            action = "renamed";
        } else {
            return; // no change occurred that is worth logging.
        }

        // Populate placeholders including both old and new names.
        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("item_type", itemType);
        placeholders.put("action", action);
        placeholders.put("old_name", oldName != null ? oldName : "");
        placeholders.put("new_name", isRename ? outputName : "");
        placeholders.put("anvil_x", String.valueOf(anvil.getLocation().getBlockX()));
        placeholders.put("anvil_y", String.valueOf(anvil.getLocation().getBlockY()));
        placeholders.put("anvil_z", String.valueOf(anvil.getLocation().getBlockZ()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_ANVIL_INTERACTION, player, placeholders);
    }
}
