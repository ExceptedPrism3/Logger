package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player death events to the database.
 */
public class DeathLogger extends AbstractLogger implements DatabasePlayerLogger {

    public DeathLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a death event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String level = placeholders.get("level");
        final String inventory = placeholders.get("inventory");
        final String armor = placeholders.get("armor");
        final String killer = placeholders.get("killer");
        final String killerWeapon = placeholders.get("killer_weapon");
        final String deathCause = placeholders.get("death_cause");

        final String sql = "INSERT INTO player_death (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, level, inventory, armor, killer, killer_weapon, death_cause, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, level);
            ps.setString(10, inventory);
            ps.setString(11, armor);
            ps.setString(12, killer);
            ps.setString(13, killerWeapon);
            ps.setString(14, deathCause);
            ps.setBoolean(15, PermissionManager.isStaff(player));
        });
    }
}
