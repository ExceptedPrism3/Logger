package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player spawn egg usage to the database.
 */
public class SpawnEggLogger extends AbstractLogger implements DatabasePlayerLogger {

    public SpawnEggLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a spawn egg event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String mobType = placeholders.get("mob_type");
        final String mobX = placeholders.get("mob_x");
        final String mobY = placeholders.get("mob_y");
        final String mobZ = placeholders.get("mob_z");

        final String sql = "INSERT INTO player_spawn_egg (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, mob_type, mob_x, mob_y, mob_z, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, mobType);
            ps.setInt(10, this.parseInt(mobX));
            ps.setInt(11, this.parseInt(mobY));
            ps.setInt(12, this.parseInt(mobZ));
            ps.setBoolean(13, PermissionManager.isStaff(player));
        });
    }

    /**
     * Parses a string to an integer, returning 0 if the string is null or invalid.
     *
     * @param value the string to parse
     * @return the parsed integer or 0 if invalid
     */
    private int parseInt(final String value) {
        try {
            return value != null ? Integer.parseInt(value) : 0;
        } catch (final NumberFormatException e) {
            return 0;
        }
    }
}
