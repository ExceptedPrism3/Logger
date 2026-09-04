package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player teleport events to the database.
 */
public class TeleportLogger extends AbstractLogger implements DatabasePlayerLogger { //TODO TEST

    public TeleportLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a teleport event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_teleport (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, to_x, to_y, to_z, cause, is_staff)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setDouble(9, Double.parseDouble(placeholders.get("to_x")));
            ps.setDouble(10, Double.parseDouble(placeholders.get("to_y")));
            ps.setDouble(11, Double.parseDouble(placeholders.get("to_z")));
            ps.setString(12, placeholders.get("cause"));
            ps.setBoolean(13, PermissionManager.isStaff(player));
        });
    }
}
