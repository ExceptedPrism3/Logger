package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player item enchantment events to the database.
 */
public class ItemEnchantLogger extends AbstractLogger implements DatabasePlayerLogger {

    public ItemEnchantLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs an enchant event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String enchantments = placeholders.get("enchants");
        final String item = placeholders.get("item");
        final String levelCost = placeholders.get("level");

        final String sql = "INSERT INTO player_item_enchant (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, item, enchantments, level_cost, enchanting_table_x, enchanting_table_y, enchanting_table_z, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, item);
            ps.setString(10, enchantments);
            ps.setString(11, levelCost);
            ps.setInt(12, Integer.parseInt(placeholders.get("enchanting_table_x")));
            ps.setInt(13, Integer.parseInt(placeholders.get("enchanting_table_y")));
            ps.setInt(14, Integer.parseInt(placeholders.get("enchanting_table_z")));
            ps.setBoolean(15, PermissionManager.isStaff(player));
        });
    }
}
