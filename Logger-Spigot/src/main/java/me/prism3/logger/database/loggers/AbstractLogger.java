package me.prism3.logger.database.loggers;

import me.prism3.logger.LoggerAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


/**
 * AbstractLogger is an abstract class that provides common functionality for logging events
 * related to players in a database. It includes methods for executing SQL updates and setting
 * common fields for player events.
 */
public abstract class AbstractLogger {

    protected final LoggerAPI plugin;

    public AbstractLogger(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Executes an SQL update safely by using try-with-resources to close both the
     * connection and PreparedStatement.
     */
    protected void executeUpdate(final String sql, final StatementSetter setter) {

        try (final Connection connection = this.plugin.getDatabaseManager().getConnection();
             final PreparedStatement ps = connection.prepareStatement(sql)) {

            setter.setValues(ps);
            ps.executeUpdate();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Functional interface for setting values in a PreparedStatement.
     * This allows for a cleaner way to set values in the PreparedStatement
     * without having to create an anonymous class each time.
     */
    @FunctionalInterface
    public interface StatementSetter {
        void setValues(PreparedStatement ps) throws SQLException;
    }

    /**
     * Sets the common fields for all events in the database.
     * This includes the date and server name.
     *
     * @param ps The PreparedStatement to set the values on.
     * @throws SQLException If an SQL error occurs while setting the values.
     */
    protected void setCommonFields(final PreparedStatement ps ) throws SQLException {
        ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        ps.setString(2, this.plugin.getData().getServerName());
    }

    /**
     * Sets the common fields for player-related events in the database.
     * This includes the date, server name, player UUID, player name, world name,
     * and location coordinates (x, y, z).
     *
     * @param ps    The PreparedStatement to set the values on.
     * @param player The Player object containing the player's information.
     * @throws SQLException If an SQL error occurs while setting the values.
     */
    protected void setPlayerFields(final PreparedStatement ps, final Player player) throws SQLException {

        this.setCommonFields(ps);

        ps.setString(3, player.getUniqueId().toString());
        ps.setString(4, player.getName());
        ps.setString(5, player.getWorld().getName());

        final Location loc = player.getLocation();
        ps.setDouble(6, loc.getX());
        ps.setDouble(7, loc.getY());
        ps.setDouble(8, loc.getZ());
    }
}
