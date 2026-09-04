package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player sign events to the database.
 */
public class SignInteractionLogger extends AbstractLogger implements DatabasePlayerLogger {

    public SignInteractionLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a sign event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_sign_interaction (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, sign_x, sign_y, sign_z, sign_text, is_staff)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setInt(9, Integer.parseInt(placeholders.get("sign_x")));
            ps.setInt(10, Integer.parseInt(placeholders.get("sign_y")));
            ps.setInt(11, Integer.parseInt(placeholders.get("sign_z")));
            ps.setString(12, placeholders.get("lines"));
            ps.setBoolean(13, PermissionManager.isStaff(player));
        });
    }
}
