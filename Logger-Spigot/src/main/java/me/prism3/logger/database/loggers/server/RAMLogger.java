package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;


/**
 * RAMLogger is responsible for logging RAM usage events to the database.
 */
public class RAMLogger extends AbstractLogger implements DatabaseServerLogger {

    public RAMLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a RAM usage event to the database.
     *
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Map<String, String> placeholders) {

        final long totalMemory = Long.parseLong(placeholders.get("total"));
        final long usedMemory = Long.parseLong(placeholders.get("used"));
        final long freeMemory = Long.parseLong(placeholders.get("free"));

        final String sql = "INSERT INTO server_ram (date, server_name, total_ram, used_ram, free_ram) VALUES (?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setCommonFields(ps);
            ps.setLong(3, totalMemory);
            ps.setLong(4, usedMemory);
            ps.setLong(5, freeMemory);
        });
    }
}
