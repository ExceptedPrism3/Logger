package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.database.DatabasePlayerLogger;
import org.bukkit.entity.Player;

import java.util.Map;

import static me.prism3.logger.managers.PermissionManager.isStaff;


/**
 * Logs player bucket empty events to the database.
 */
public class BucketEmptyLogger extends AbstractLogger implements DatabasePlayerLogger {

    public BucketEmptyLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a player bucket empty event to the database.
     *
     * @param player      The player who emptied the bucket.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_bucket_empty " +
                "(date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, bucket_type, bucket_x, bucket_y, bucket_z, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, placeholders.get("bucket"));
            ps.setInt(10, Integer.parseInt(placeholders.get("bucket_x")));
            ps.setInt(11, Integer.parseInt(placeholders.get("bucket_y")));
            ps.setInt(12, Integer.parseInt(placeholders.get("bucket_z")));
            ps.setBoolean(13, isStaff(player));
        });
    }
}
