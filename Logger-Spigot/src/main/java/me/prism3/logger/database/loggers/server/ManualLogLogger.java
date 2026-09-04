package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;


/**
 * Log server events that are manually triggered by the console.
 * <p>
 * This is used for logging manual commands executed by the console.
 */
public class ManualLogLogger extends AbstractLogger implements DatabaseServerLogger {

    public ManualLogLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Log a server event that is manually triggered by the console.
     *
     * @param placeholders a map of placeholders to be replaced in the log message
     */
    @Override
    public void logEvent(Map<String, String> placeholders) {

        final String log = placeholders.get("log");

        final String sql = "INSERT INTO server_manual_log (date, server_name, log) VALUES (?, ?, ?)";

        executeUpdate(sql, ps -> {
            setCommonFields(ps);
            ps.setString(3, log);
        });
    }
}
