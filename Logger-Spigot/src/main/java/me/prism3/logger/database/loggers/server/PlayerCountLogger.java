package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;


/**
 * Logs player count events to the database.
 */
public class PlayerCountLogger extends AbstractLogger implements DatabaseServerLogger {

    public PlayerCountLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs the player count event to the database.
     *
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Map<String, String> placeholders) {

        final String sql = "INSERT INTO server_player_count (date, server_name) VALUES (?, ?)";

        executeUpdate(sql, this::setCommonFields);
    }
}
