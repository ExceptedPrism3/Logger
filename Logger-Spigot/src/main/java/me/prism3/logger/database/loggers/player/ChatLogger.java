package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player chat messages to the database.
 */
public class ChatLogger extends AbstractLogger implements DatabasePlayerLogger {

    public ChatLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a chat event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String message = placeholders.get("message");

        final String sql = "INSERT INTO player_chat (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, message, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, message);
            ps.setBoolean(10, PermissionManager.isStaff(player));
        });
    }
}
