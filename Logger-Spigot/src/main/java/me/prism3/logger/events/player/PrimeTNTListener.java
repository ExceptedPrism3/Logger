package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Map;

/**
 * Listens for TNT priming and explosion events and logs them.
 */
public class PrimeTNTListener implements Listener {

    private final LoggerAPI plugin;

    public PrimeTNTListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for TNT explosion events and logs the player who primed it.
     *
     * @param event The EntityExplodeEvent to handle.
     */
    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent event) {

        if (event.isCancelled() || !(event.getEntity() instanceof TNTPrimed))
            return;

        final TNTPrimed tnt = (TNTPrimed) event.getEntity();
        if (tnt.getSource() == null || !(tnt.getSource() instanceof Player))
            return;

        final Player player = (Player) tnt.getSource();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("tnt_location_x", String.valueOf(event.getLocation().getBlockX()));
        placeholders.put("tnt_location_y", String.valueOf(event.getLocation().getBlockY()));
        placeholders.put("tnt_location_z", String.valueOf(event.getLocation().getBlockZ()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_PRIME_TNT, player, placeholders);
    }
}
