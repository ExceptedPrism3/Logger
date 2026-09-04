package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;


/**
 * StartLogger is responsible for logging server start events to the database.
 */
public class StartLogger extends AbstractLogger implements DatabaseServerLogger {

    public StartLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a server start event to the database.
     *
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Map<String, String> placeholders) {

        final String sql = "INSERT INTO server_start (date, server_name) VALUES (?, ?)";

        executeUpdate(sql, this::setCommonFields);
    }
}
