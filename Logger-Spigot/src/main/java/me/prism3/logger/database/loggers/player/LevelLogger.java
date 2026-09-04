package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player leveling events to the database.
 */
public class LevelLogger extends AbstractLogger implements DatabasePlayerLogger {

    public LevelLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a player leveling event to the database.
     *
     * @param player      The player who leveled up.
     * @param placeholders A map of placeholders to be used in the SQL query.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_level (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, is_staff)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setBoolean(9, PermissionManager.isStaff(player));
        });
    }
}
