package me.prism3.logger.database;

import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Interface for logging player events to a database.
 * Implementations should define how to log the events.
 */
public interface DatabasePlayerLogger {
    void logEvent(final Player player, final Map<String, String> placeholders);
}
