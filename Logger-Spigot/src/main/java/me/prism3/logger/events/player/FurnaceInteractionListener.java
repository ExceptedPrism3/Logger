package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.util.Map;


/**
 * This class listens for furnace extraction events and logs them.
 */
public class FurnaceInteractionListener implements Listener {

    private final LoggerAPI plugin;

    public FurnaceInteractionListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * This method is called when a player extracts items from a furnace.
     *
     * @param event The FurnaceExtractEvent that was triggered.
     */
    @EventHandler
    public void onFurnaceExtract(final FurnaceExtractEvent event) {

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("item_type", event.getItemType().name());
        placeholders.put("amount", String.valueOf(event.getItemAmount()));

        final Location loc = event.getBlock().getLocation();
        placeholders.put("furnace_x", String.valueOf(loc.getBlockX()));
        placeholders.put("furnace_y", String.valueOf(loc.getBlockY()));
        placeholders.put("furnace_z", String.valueOf(loc.getBlockZ()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_FURNACE_INTERACTION, player, placeholders);
    }
}
