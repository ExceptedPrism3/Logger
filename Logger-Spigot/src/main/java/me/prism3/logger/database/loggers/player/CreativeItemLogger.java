package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class CreativeItemLogger extends AbstractLogger implements DatabasePlayerLogger {

    public CreativeItemLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {
        final String sql = "INSERT INTO player_creative_item (date, server_name, player_uuid, player_name, world_name, " +
                "location_x, location_y, location_z, item_type, amount, has_nbt, nbt_data, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, placeholders.getOrDefault("item", "UNKNOWN"));
            ps.setInt(10, parseInt(placeholders.get("amount"), 1));
            ps.setBoolean(11, "Yes".equalsIgnoreCase(placeholders.get("has_nbt")));
            ps.setString(12, placeholders.getOrDefault("nbt_data", "None"));
            ps.setBoolean(13, PermissionManager.isStaff(player));
        });
    }
    
    private int parseInt(String val, int def) {
        if (val == null) return def;
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return def;
        }
    }
}
