package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Logs player horse / mount interactions to the database.
 */
public class HorseInteractionLogger extends AbstractLogger implements DatabasePlayerLogger {

    public HorseInteractionLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {
        final String sql = "INSERT INTO player_horse_interaction (date, server_name, player_uuid, player_name, world_name, " +
                "location_x, location_y, location_z, action, horse_type, horse_uuid, horse_name, horse_owner_uuid, " +
                "horse_owner_name, is_tamed, has_saddle, armor_type, details, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, placeholders.getOrDefault("action", "INTERACT"));
            ps.setString(10, placeholders.getOrDefault("horse_type", "HORSE"));
            ps.setString(11, placeholders.getOrDefault("horse_uuid", "UNKNOWN"));
            ps.setString(12, placeholders.getOrDefault("horse_name", "None"));
            ps.setString(13, placeholders.getOrDefault("owner_uuid", "None"));
            ps.setString(14, placeholders.getOrDefault("owner", "None"));
            ps.setBoolean(15, "Yes".equalsIgnoreCase(placeholders.get("tamed")));
            ps.setBoolean(16, "Yes".equalsIgnoreCase(placeholders.get("saddle")));
            ps.setString(17, placeholders.getOrDefault("armor", "None"));
            ps.setString(18, placeholders.getOrDefault("details", ""));
            ps.setBoolean(19, PermissionManager.isStaff(player));
        });
    }
}
