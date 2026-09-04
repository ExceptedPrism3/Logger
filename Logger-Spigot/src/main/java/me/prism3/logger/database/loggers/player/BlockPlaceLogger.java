package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs block placement events to the database.
 */
public class BlockPlaceLogger extends AbstractLogger implements DatabasePlayerLogger {

    /**
     * Constructor for BlockPlaceLogger.
     *
     * @param plugin The LoggerAPI plugin instance.
     */
    public BlockPlaceLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs the block placement event to the database.
     *
     * @param player      The player who placed the block.
     * @param placeholders A map containing placeholders for block type and coordinates.
     */
    @Override
    public void logEvent(Player player, Map<String, String> placeholders) {

        final String blockType = placeholders.get("block");
        final String blockX = placeholders.get("block_x");
        final String blockY = placeholders.get("block_y");
        final String blockZ = placeholders.get("block_z");

        final String sql = "INSERT INTO player_block_place (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, block, block_x, block_y, block_z, is_staff) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString(9, blockType);
            ps.setInt(10, this.parseInt(blockX));
            ps.setInt(11, this.parseInt(blockY));
            ps.setInt(12, this.parseInt(blockZ));
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
