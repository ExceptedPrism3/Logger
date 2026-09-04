package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

import java.util.Map;


/**
 * Listens for player bucket fill events and logs the details.
 * This class implements the Listener interface to handle events.
 */
public class BucketFillListener implements Listener {

    private final LoggerAPI plugin;

    public BucketFillListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles the PlayerBucketFillEvent.
     * Logs the bucket fill event details if the player is not exempt from logging.
     *
     * @param event the PlayerBucketFillEvent to handle
     */
    @EventHandler
    public void onBucketFill(final PlayerBucketFillEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player)) return;

        if (event.getItemStack() == null)
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);
        final String bucketType = event.getItemStack().getType().name();

        placeholders.put("bucket", bucketType);
        placeholders.put("bucket_x", String.valueOf(event.getBlockClicked().getX()));
        placeholders.put("bucket_y", String.valueOf(event.getBlockClicked().getY()));
        placeholders.put("bucket_z", String.valueOf(event.getBlockClicked().getZ()));

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_BUCKET_FILL, player, placeholders);
    }
}
