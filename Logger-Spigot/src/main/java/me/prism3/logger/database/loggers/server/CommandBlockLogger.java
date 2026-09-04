package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;


/**
 * CommandBlockLogger is responsible for logging command block events to the database.
 * It extends the AbstractLogger class and implements the DatabaseServerLogger interface.
 */
public class CommandBlockLogger extends AbstractLogger implements DatabaseServerLogger {

    /**
     * Constructor for the CommandBlockLogger class.
     *
     * @param plugin The LoggerAPI plugin instance.
     */
    public CommandBlockLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a command block event to the database.
     *
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Map<String, String> placeholders) {

        final String command = placeholders.get("command");

        final String sql = "INSERT INTO server_command_block (date, server_name, command) VALUES (?, ?, ?)";

        executeUpdate(sql, ps -> {
            setCommonFields(ps);
            ps.setString(3,command);
        });
    }
}
