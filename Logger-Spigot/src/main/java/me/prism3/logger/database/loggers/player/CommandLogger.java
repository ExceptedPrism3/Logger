package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player command usage to the database.
 */
public class CommandLogger extends AbstractLogger implements DatabasePlayerLogger {

    public CommandLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a command event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, Map<String, String> placeholders) {

        final String command = placeholders.get("command");

        final String sql = "INSERT INTO player_command (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, command, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, command);
            ps.setBoolean(10, PermissionManager.isStaff(player));
        });
    }
}
