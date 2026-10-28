package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class MaceSmashLogger extends AbstractLogger implements DatabasePlayerLogger {

    public MaceSmashLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {
        final String victim = placeholders.getOrDefault("victim", "Unknown");
        double fallDistance = 0.0;
        double damage = 0.0;
        try {
            fallDistance = Double.parseDouble(placeholders.getOrDefault("fall_distance", "0"));
        } catch (NumberFormatException ignored) {}
        try {
            damage = Double.parseDouble(placeholders.getOrDefault("damage", "0"));
        } catch (NumberFormatException ignored) {}

        final double finalFall = fallDistance;
        final double finalDamage = damage;

        final String sql = "INSERT INTO player_mace_smash (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, victim, fall_distance, damage, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, victim);
            ps.setDouble(10, finalFall);
            ps.setDouble(11, finalDamage);
            ps.setBoolean(12, PermissionManager.isStaff(player));
        });
    }
}
