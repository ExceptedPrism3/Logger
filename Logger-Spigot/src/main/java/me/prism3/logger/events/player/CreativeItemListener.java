package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CreativeItemListener implements Listener {

    private final LoggerAPI plugin;

    public CreativeItemListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreativeItem(final InventoryCreativeEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        final Player player = (Player) event.getWhoClicked();

        ItemStack cursor = event.getCursor();
        if (cursor == null || cursor.getType() == Material.AIR) {
            return; 
        }

        if (PermissionManager.isExempt(player)) {
            return;
        }

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        
        placeholders.put("item", cursor.getType().name());
        placeholders.put("amount", String.valueOf(cursor.getAmount()));
        boolean hasNbt = cursor.hasItemMeta();
        placeholders.put("has_nbt", hasNbt ? "Yes" : "No");
        placeholders.put("nbt_data", hasNbt ? cursor.getItemMeta().toString() : "None");

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_CREATIVE_ITEM, player, placeholders);
    }
}
