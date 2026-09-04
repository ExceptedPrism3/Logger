package me.prism3.logger.v1_21.listeners;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.GameEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockReceiveGameEvent;

import java.util.Map;

public class SculkShriekerListener implements Listener {

    private final LoggerAPI plugin;

    public SculkShriekerListener(LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShriek(BlockReceiveGameEvent event) {
        if (event.getEvent() != GameEvent.SHRIEK) return;
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        if (PermissionManager.isExempt(player)) return;

        Block block = event.getBlock();
        // Warning level is not available in BlockReceiveGameEvent, so we skip it or fetch from block state if possible.
        // But getting block state is async-unsafe if this event is async? 
        // Usually game events are sync.
        // We'll just log the location.

        Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("x", String.valueOf(block.getX()));
        placeholders.put("y", String.valueOf(block.getY()));
        placeholders.put("z", String.valueOf(block.getZ()));
        placeholders.put("level", "Unknown"); // Warning level not available here

        plugin.getLoggerManager().logEvent(LogType.PLAYER_SCULK_SHRIEKER, player, placeholders);
    }
}
