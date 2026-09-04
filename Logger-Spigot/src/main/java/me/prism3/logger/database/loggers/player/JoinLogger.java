package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player join events to the database.
 */
public class JoinLogger extends AbstractLogger implements DatabasePlayerLogger {

    public JoinLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a join event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String playerIp = placeholders.get("ip");

        final String sql = "INSERT INTO player_join (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, ip_address, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, playerIp);
            ps.setBoolean(10, PermissionManager.isStaff(player));
        });
    }
}
