package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.VersionUtil;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Switch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;

import java.util.Map;

/**
 * Listens for player lever interactions and logs them.
 */
public class LeverInteractionListener implements Listener {

    private final LoggerAPI plugin;
    private final boolean legacy;

    public LeverInteractionListener(final LoggerAPI plugin) {
        this.plugin = plugin;
        // detect legacy (<1.13) or modern
        this.legacy = VersionUtil.CURRENT.isLegacy();
    }

    /**
     * Handles the player lever interaction event.
     *
     * @param event The PlayerInteractEvent to handle.
     */
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {

        if (event.isCancelled())
            return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        final Block block = event.getClickedBlock();

        if (block == null)
            return;

        // must be a lever
        if (block.getType() != Material.LEVER)
            return;

        if (PermissionManager.isExempt(event.getPlayer()))
            return;

        final boolean isPowered;

        // check if the block is a lever
        if (this.legacy) {
            // old API
            final BlockState state = block.getState();
            final MaterialData md = state.getData();

            if (!(md instanceof Lever))
                return;

            final Lever leverData = (Lever) md;
            isPowered = leverData.isPowered();

        } else {
            // modern API
            final BlockData bd = block.getBlockData();

            if (!(bd instanceof Switch))
                return;

            final Switch sw = (Switch) bd;
            isPowered = sw.isPowered();
        }

        final org.bukkit.entity.Player player = event.getPlayer();
        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        placeholders.put("lever_state", isPowered ? "ON" : "OFF");
        placeholders.put("lever_x", String.valueOf(block.getX()));
        placeholders.put("lever_y", String.valueOf(block.getY()));
        placeholders.put("lever_z", String.valueOf(block.getZ()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_LEVER_INTERACTION, player, placeholders);
    }
}
