package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs lever interaction events to the database.
 */
public class LeverInteractionLogger extends AbstractLogger implements DatabasePlayerLogger {

    public LeverInteractionLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a lever interaction event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_lever_interaction (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, lever_x, lever_y, lever_z, lever_state, is_staff)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setInt(9, Integer.parseInt(placeholders.get("lever_x")));
            ps.setInt(10, Integer.parseInt(placeholders.get("lever_y")));
            ps.setInt(11, Integer.parseInt(placeholders.get("lever_z")));
            ps.setString(12, placeholders.get("lever_state"));
            ps.setBoolean(13, PermissionManager.isStaff(player));
        });
    }
}
