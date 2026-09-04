package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player container interactions events to the database.
 */
public class ContainerInteractionLogger extends AbstractLogger implements DatabasePlayerLogger {

    public ContainerInteractionLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a player container interaction event to the database.
     *
     * @param player      The player who interacted with the container.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_container_interaction " +
                "(date, server_name, player_uuid, player_name, world_name, " +
                "location_x, location_y, location_z, container_type, container_x, container_y, container_z, added_items, removed_items, original_content, modified_content, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, placeholders.get("container"));
            ps.setInt(10, Integer.parseInt(placeholders.get("container_x")));
            ps.setInt(11, Integer.parseInt(placeholders.get("container_y")));
            ps.setInt(12, Integer.parseInt(placeholders.get("container_z")));
            ps.setString(13, placeholders.get("added_items"));
            ps.setString(14, placeholders.get("removed_items"));
            ps.setString(15, placeholders.get("original"));
            ps.setString(16, placeholders.get("modified"));
            ps.setBoolean(17, PermissionManager.isStaff(player));
        });
    }
}
