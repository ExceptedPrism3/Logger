package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RespawnAnchorListener implements Listener {

    private final LoggerAPI plugin;

    public RespawnAnchorListener(LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.RESPAWN_ANCHOR) return;

        Player player = event.getPlayer();
        if (PermissionManager.isExempt(player)) return;

        Block block = event.getClickedBlock();
        RespawnAnchor anchor = (RespawnAnchor) block.getBlockData();
        String action = "";
        int charges = anchor.getCharges();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() == Material.GLOWSTONE) {
                if (charges < anchor.getMaximumCharges()) {
                    action = "charged";
                    charges++; // Anticipate charge increase
                }
            } else {
                if (charges > 0) {
                    action = "set spawn at";
                }
            }
        }

        if (!action.isEmpty()) {
            Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
            placeholders.put("action", action);
            placeholders.put("charges", String.valueOf(charges));
            
            plugin.getLoggerManager().logEvent(LogType.PLAYER_RESPAWN_ANCHOR, player, placeholders);
        }
    }
}
