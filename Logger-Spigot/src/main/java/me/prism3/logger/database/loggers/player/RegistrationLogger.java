package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player advancement unlock events to the database.
 * <p>
 * This class extends AbstractLogger and implements DatabasePlayerLogger.
 * It is responsible for logging player advancement unlock events to the database.
 */
public class RegistrationLogger extends AbstractLogger implements DatabasePlayerLogger {

    public RegistrationLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a player advancement unlock event to the database.
     *
     * @param player      The player who unlocked the advancement.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_registration (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, is_staff)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setBoolean(9, PermissionManager.isStaff(player));
        });
    }
}
