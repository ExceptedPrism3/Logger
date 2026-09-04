package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player game-mode change  events to the database.
 */
public class GameModeLogger extends AbstractLogger implements DatabasePlayerLogger {

    public GameModeLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a player game-mode change event to the database.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_gamemode (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, is_staff) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setBoolean(9, PermissionManager.canLogStaff(player));
        });
    }
}
