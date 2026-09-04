package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player entity death events to the database.
 */
public class EntityDeathLogger extends AbstractLogger implements DatabasePlayerLogger {

    public EntityDeathLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a entity death event to the database.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_entity_death " +
                "(date, server_name, player_uuid, player_name, world_name, " +
                "location_x, location_y, location_z, entity_type, entity_x, entity_y, entity_z, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, placeholders.get("entity_type"));
            ps.setInt(10, Integer.parseInt(placeholders.get("entity_x")));
            ps.setInt(11, Integer.parseInt(placeholders.get("entity_y")));
            ps.setInt(12, Integer.parseInt(placeholders.get("entity_z")));
            ps.setBoolean(13, PermissionManager.isStaff(player));
        });
    }
}
