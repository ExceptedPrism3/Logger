package me.prism3.logger.v1_21.listeners;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VaultListener implements Listener {

    private final LoggerAPI plugin;

    public VaultListener(LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVaultDispense(BlockDispenseLootEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.VAULT) {
            Player player = event.getPlayer();
            if (player != null) {
                if (PermissionManager.isExempt(player)) return;

                List<ItemStack> items = event.getDispensedLoot();
                String itemList = items.stream()
                        .map(item -> item.getType().name() + " x" + item.getAmount())
                        .collect(Collectors.joining(", "));

                Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
                placeholders.put("vault_x", String.valueOf(block.getX()));
                placeholders.put("vault_y", String.valueOf(block.getY()));
                placeholders.put("vault_z", String.valueOf(block.getZ()));
                placeholders.put("items", itemList);

                plugin.getLoggerManager().logEvent(LogType.PLAYER_VAULT_UNLOCK, player, placeholders);
            }
        }
    }
}
