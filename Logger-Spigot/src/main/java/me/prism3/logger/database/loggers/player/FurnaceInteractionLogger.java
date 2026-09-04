package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs furnace interaction events to the database.
 */
public class FurnaceInteractionLogger extends AbstractLogger implements DatabasePlayerLogger {

    public FurnaceInteractionLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a furnace interaction event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_furnace_interaction (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, item_type, amount, furnace_x, furnace_y, furnace_z, is_staff)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString (9,  placeholders.get("item_type"));
            ps.setInt    (10, Integer.parseInt(placeholders.get("amount")));
            ps.setInt    (11, Integer.parseInt(placeholders.get("furnace_x")));
            ps.setInt    (12, Integer.parseInt(placeholders.get("furnace_y")));
            ps.setInt    (13, Integer.parseInt(placeholders.get("furnace_z")));
            ps.setBoolean(14, PermissionManager.canLogStaff(player));
        });
    }
}
