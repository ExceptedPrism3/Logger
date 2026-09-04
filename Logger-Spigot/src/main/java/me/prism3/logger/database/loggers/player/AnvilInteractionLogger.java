package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logger for player anvil events.
 */
public class AnvilInteractionLogger extends AbstractLogger implements DatabasePlayerLogger {

    /**
     * Constructor for the AnvilLogger class.
     *
     * @param plugin The LoggerAPI plugin instance.
     */
    public AnvilInteractionLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs an anvil event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String itemType = placeholders.get("item_type");
        final String action = placeholders.get("action");
        final String oldName = placeholders.get("old_name");
        final String newName = placeholders.get("new_name");

        final String sql = "INSERT INTO player_anvil (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, item_type, action, old_name, new_name, anvil_x, anvil_y, anvil_z, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, itemType);
            ps.setString(10, action);
            ps.setString(11, oldName);
            ps.setString(12, newName);
            ps.setInt(13, Integer.parseInt(placeholders.get("anvil_x")));
            ps.setInt(14, Integer.parseInt(placeholders.get("anvil_y")));
            ps.setInt(15, Integer.parseInt(placeholders.get("anvil_z")));
            ps.setBoolean(16, PermissionManager.isStaff(player));
        });
    }
}
