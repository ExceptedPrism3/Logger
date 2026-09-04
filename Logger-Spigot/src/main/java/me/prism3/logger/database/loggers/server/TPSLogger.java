package me.prism3.logger.database.loggers.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;

import java.util.Map;


/**
 * TPSLogger is responsible for logging TPS (Ticks Per Second) events to the database.
 */
public class TPSLogger extends AbstractLogger implements DatabaseServerLogger {

    public TPSLogger(final LoggerAPI plugin) {
        super(plugin);
    }

    /**
     * Logs a TPS event to the database.
     *
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(Map<String, String> placeholders) {

        final double tps = Double.parseDouble(placeholders.get("tps"));

        final String sql = "INSERT INTO server_tps (date, server_name, tps) VALUES (?, ?, ?)";

        executeUpdate(sql, ps -> {
            setCommonFields(ps);
            ps.setDouble(3, tps);
        });
    }
}
