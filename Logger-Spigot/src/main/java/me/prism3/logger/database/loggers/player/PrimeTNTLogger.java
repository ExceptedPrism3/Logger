package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player Prime TNT events to the database.
 */
public class PrimeTNTLogger extends AbstractLogger implements DatabasePlayerLogger {

    public PrimeTNTLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a Prime TNT event for a player.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String tntLocationX = placeholders.get("tnt_location_x");
        final String tntLocationY = placeholders.get("tnt_location_y");
        final String tntLocationZ = placeholders.get("tnt_location_z");

        final String sql = "INSERT INTO player_prime_tnt (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, tnt_location_x, tnt_location_y, tnt_location_z, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setDouble(9, this.parseInt(tntLocationX));
            ps.setDouble(10, this.parseInt(tntLocationY));
            ps.setDouble(11, this.parseInt(tntLocationZ));
            ps.setBoolean(12, PermissionManager.isStaff(player));
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
