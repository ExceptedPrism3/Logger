package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs item pickups to the database.
 */
public class ItemPickupLogger extends AbstractLogger implements DatabasePlayerLogger {

    public ItemPickupLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs an item pickup event for a player.
     *
     * @param player      The player who picked up the item.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_item_pickup (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, item_type, item_amount, enchants, is_staff)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, placeholders.get("item"));
            ps.setInt(10, Integer.parseInt(placeholders.get("amount")));
            ps.setString(11, placeholders.get("enchants"));
            ps.setBoolean(12, PermissionManager.isStaff(player));
        });
    }
}
