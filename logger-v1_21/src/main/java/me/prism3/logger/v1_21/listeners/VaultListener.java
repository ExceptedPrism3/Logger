package me.prism3.logger.v1_21.listeners;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.Log;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class VaultListener implements Listener {

    private final LoggerAPI plugin;

    public VaultListener(LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVaultDispense(BlockDispenseLootEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.VAULT) {
            Player player = event.getPlayer();
            if (player != null) {
                List<ItemStack> items = event.getDispensedLoot();
                
                String itemList = items.stream()
                        .map(item -> item.getType().name() + " x" + item.getAmount())
                        .collect(Collectors.joining(", "));

                // Log to console/file
                Log.info(player.getName() + " unlocked a Vault at " + 
                        block.getX() + "," + block.getY() + "," + block.getZ() + 
                        " and received: " + itemList);
            }
        }
    }
}
