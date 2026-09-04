package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class VillagerTradeLogger extends AbstractLogger implements DatabasePlayerLogger {

    public VillagerTradeLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {
        final String profession = placeholders.get("villager_profession");
        final String level = placeholders.get("villager_level");
        final String cost1 = placeholders.get("cost_1");
        final String cost2 = placeholders.get("cost_2");
        final String result = placeholders.get("result");

        final String sql = "INSERT INTO player_villager_trade (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, villager_profession, villager_level, cost_1, cost_2, result, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, profession);
            ps.setInt(10, Integer.parseInt(level));
            ps.setString(11, cost1);
            ps.setString(12, cost2);
            ps.setString(13, result);
            ps.setBoolean(14, PermissionManager.isStaff(player));
        });
    }
}
