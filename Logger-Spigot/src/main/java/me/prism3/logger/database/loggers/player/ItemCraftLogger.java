package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.database.DatabasePlayerLogger;
import org.bukkit.entity.Player;

import java.util.Map;

import static me.prism3.logger.managers.PermissionManager.isStaff;


/**
 * Logs player item craft events to the database.
 */
public class ItemCraftLogger extends AbstractLogger implements DatabasePlayerLogger {

    public ItemCraftLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a item craft event to the database.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_item_craft " +
                "(date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, item_type, item_amount, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, placeholders.get("item_type"));
            ps.setInt(10, Integer.parseInt(placeholders.get("item_amount")));
            ps.setBoolean(11, isStaff(player));
        });
    }
}
