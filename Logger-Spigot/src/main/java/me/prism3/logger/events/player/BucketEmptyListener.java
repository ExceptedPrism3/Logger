package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.Map;


/**
 * Listens for player bucket empty events and logs the details.
 * This class implements the Listener interface from Bukkit.
 */
public class BucketEmptyListener implements Listener {

    private final LoggerAPI plugin;

    public BucketEmptyListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the PlayerBucketEmptyEvent.
     * Logs the event details if the player is not exempt from logging.
     *
     * @param event the PlayerBucketEmptyEvent to handle
     */
    @EventHandler
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player)) return;

        final Material bucketType = event.getBucket();

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        placeholders.put("bucket", bucketType.toString());
        placeholders.put("bucket_x", String.valueOf(event.getBlockClicked().getX()));
        placeholders.put("bucket_y", String.valueOf(event.getBlockClicked().getY()));
        placeholders.put("bucket_z", String.valueOf(event.getBlockClicked().getZ()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_BUCKET_EMPTY, player, placeholders);
    }
}
