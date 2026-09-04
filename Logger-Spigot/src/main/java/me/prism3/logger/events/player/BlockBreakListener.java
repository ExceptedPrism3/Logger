package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;


/**
 * BlockBreakListener listens for block break events and logs them.
 * It checks if the player is exempt from logging and populates placeholders
 * with block information before logging the event.
 */
public class BlockBreakListener implements Listener {

    private final LoggerAPI plugin;

    /**
     * Constructor for BlockBreakListener.
     *
     * @param plugin The Logger instance.
     */
    public BlockBreakListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the block break event.
     *
     * @param event The BlockBreakEvent to handle.
     */
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("block", event.getBlock().getType().toString());
        placeholders.put("block_x", String.valueOf(event.getBlock().getLocation().getBlockX()));
        placeholders.put("block_y", String.valueOf(event.getBlock().getLocation().getBlockY()));
        placeholders.put("block_z", String.valueOf(event.getBlock().getLocation().getBlockZ()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_BLOCK_BREAK, player, placeholders);
    }
}
