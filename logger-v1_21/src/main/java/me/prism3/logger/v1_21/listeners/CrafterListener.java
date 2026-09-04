package me.prism3.logger.v1_21.listeners;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Crafter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CrafterListener implements Listener {

    private final LoggerAPI plugin;

    public CrafterListener(LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(CrafterCraftEvent event) {
        Block block = event.getBlock();
        // CrafterCraftEvent is only for Crafter, no need to check state type strictly if event guarantees it, 
        // but safe to keep or remove. The event is specific to Crafter.
        
        ItemStack result = event.getResult();
        String item = result.getType().name() + " x" + result.getAmount();

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("x", String.valueOf(block.getX()));
        placeholders.put("y", String.valueOf(block.getY()));
        placeholders.put("z", String.valueOf(block.getZ()));
        placeholders.put("world", block.getWorld().getName());
        placeholders.put("item", item);

        // Crafter crafting is automated, no player involved directly.
        plugin.getLoggerManager().logEvent(LogType.PLAYER_CRAFTER_CRAFT, null, placeholders);
    }
}
