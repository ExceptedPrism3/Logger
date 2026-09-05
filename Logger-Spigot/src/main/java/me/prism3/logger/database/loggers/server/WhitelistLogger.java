package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;

/**
 * WhitelistLogger is responsible for logging whitelist events to the database.
 */
public class WhitelistLogger extends AbstractLogger implements DatabaseServerLogger {

    public WhitelistLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a whitelist event to the database.
     *
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Map<String, String> placeholders) {
        final String executor = placeholders.getOrDefault("executor", "Unknown");
        final String action = placeholders.getOrDefault("action", "unknown");
        final String target = placeholders.getOrDefault("target", "Server");

        final String sql = "INSERT INTO server_whitelist (date, server_name, executor, action, target_player) VALUES (?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setCommonFields(ps);
            ps.setString(3, executor);
            ps.setString(4, action);
            ps.setString(5, target);
        });
    }
}
