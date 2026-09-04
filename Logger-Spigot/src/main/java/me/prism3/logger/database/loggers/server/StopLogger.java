package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;


/**
 * Log server stop into the database.
 */
public class StopLogger extends AbstractLogger implements DatabaseServerLogger {

    public StopLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a server stop event to the database.
     *
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Map<String, String> placeholders) {

        final String sql = "INSERT INTO server_stop (date, server_name) VALUES (?, ?)";

        executeUpdate(sql, this::setCommonFields);
    }
}
