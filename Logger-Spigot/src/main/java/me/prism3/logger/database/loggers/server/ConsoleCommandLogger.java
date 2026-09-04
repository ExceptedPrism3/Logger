package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;


/**
 * ConsoleCommandLogger is responsible for logging console command events to the database.
 */
public class ConsoleCommandLogger extends AbstractLogger implements DatabaseServerLogger {

    public ConsoleCommandLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a console command event to the database.
     *
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Map<String, String> placeholders) {

        final String command = placeholders.get("command");

        final String sql = "INSERT INTO server_console_command (date, server_name, command) VALUES (?, ?, ?)";

        executeUpdate(sql, ps -> {
            setCommonFields(ps);
            ps.setString(3, command);
        });
    }
}
