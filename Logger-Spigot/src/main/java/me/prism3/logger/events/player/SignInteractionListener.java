package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Map;


/**
 * Listens for sign interaction events and logs them.
 */
public class SignInteractionListener implements Listener {

    private final LoggerAPI plugin;

    public SignInteractionListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Logs sign interactions.
     *
     * @param event the sign change event
     */
    @EventHandler
    public void onSignChange(final SignChangeEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        placeholders.put("sign_x", String.valueOf(event.getBlock().getLocation().getBlockX()));
        placeholders.put("sign_y", String.valueOf(event.getBlock().getLocation().getBlockY()));
        placeholders.put("sign_z", String.valueOf(event.getBlock().getLocation().getBlockZ()));
        placeholders.put("lines", String.join(" | ", event.getLines()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_SIGN_INTERACTION, player, placeholders);
    }
}
