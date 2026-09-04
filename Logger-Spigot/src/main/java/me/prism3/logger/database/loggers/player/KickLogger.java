package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player Kicking event to the database.
 */
public class KickLogger extends AbstractLogger implements DatabasePlayerLogger {

    public KickLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a kick event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String reason = placeholders.get("reason");

        final String sql = "INSERT INTO player_kick (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, reason, is_staff) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, reason);
            ps.setBoolean(10, PermissionManager.isStaff(player));
        });
    }
}
