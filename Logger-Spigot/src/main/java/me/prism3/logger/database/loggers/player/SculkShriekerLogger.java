package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class SculkShriekerLogger extends AbstractLogger implements DatabasePlayerLogger {

    public SculkShriekerLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {
        final String level = placeholders.get("level");

        final String sql = "INSERT INTO player_sculk_shrieker (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, level, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, level);
            ps.setBoolean(10, PermissionManager.isStaff(player));
        });
    }
}
