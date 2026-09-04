package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


public class WoodStripLogger extends AbstractLogger implements DatabasePlayerLogger { //TODO TO TEST

    public WoodStripLogger(final LoggerAPI plugin) { super(plugin); }

    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_wood_strip " +
                "(date, server_name, player_uuid, player_name, world_name, " +
                "location_x, location_y, location_z, block_before, block_after, used_tool, block_x, block_y, block_z, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, placeholders.get("block_before"));
            ps.setString(10, placeholders.get("block_after"));
            ps.setString(11, placeholders.get("used_tool"));
            ps.setInt(12, Integer.parseInt(placeholders.get("block_x")));
            ps.setInt(13, Integer.parseInt(placeholders.get("block_y")));
            ps.setInt(14, Integer.parseInt(placeholders.get("block_z")));
            ps.setBoolean(15, PermissionManager.isStaff(player));
        });
    }
}
