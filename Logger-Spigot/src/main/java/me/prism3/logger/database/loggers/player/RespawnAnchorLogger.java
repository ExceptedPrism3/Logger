package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class RespawnAnchorLogger extends AbstractLogger implements DatabasePlayerLogger {

    public RespawnAnchorLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {
        final String action = placeholders.get("action");
        final String charges = placeholders.get("charges");

        final String sql = "INSERT INTO player_respawn_anchor (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, action, charges, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, action);
            ps.setInt(10, Integer.parseInt(charges));
            ps.setBoolean(11, PermissionManager.isStaff(player));
        });
    }
}
