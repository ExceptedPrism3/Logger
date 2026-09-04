package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player advancement unlock events to the database.
 */
public class AdvancementUnlockLogger extends AbstractLogger implements DatabasePlayerLogger {

    public AdvancementUnlockLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a player advancement unlock event to the database.
     *
     * @param player      The player who unlocked the advancement.
     * @param placeholders A map of placeholders to be used in the SQL query.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_advancement_unlock (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, advancement, is_staff)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, placeholders.get("advancement"));
            ps.setBoolean(10, PermissionManager.isStaff(player));
        });
    }
}
