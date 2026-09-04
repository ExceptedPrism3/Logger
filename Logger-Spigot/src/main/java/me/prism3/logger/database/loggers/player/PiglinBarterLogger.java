package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class PiglinBarterLogger extends AbstractLogger implements DatabasePlayerLogger {

    public PiglinBarterLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {
        final String input = placeholders.get("input");
        final String output = placeholders.get("output");
        final String world = placeholders.get("world");
        final String x = placeholders.get("x");
        final String y = placeholders.get("y");
        final String z = placeholders.get("z");

        final String sql = "INSERT INTO player_piglin_barter (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, input, output, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setCommonFields(ps);
            // Player fields manually since player is likely null
            ps.setString(3, "00000000-0000-0000-0000-000000000000"); // UUID
            ps.setString(4, "Piglin"); // Name
            ps.setString(5, world);
            ps.setDouble(6, Double.parseDouble(x));
            ps.setDouble(7, Double.parseDouble(y));
            ps.setDouble(8, Double.parseDouble(z));
            
            ps.setString(9, input);
            ps.setString(10, output);
            ps.setBoolean(11, false); // Not staff
        });
    }
}
