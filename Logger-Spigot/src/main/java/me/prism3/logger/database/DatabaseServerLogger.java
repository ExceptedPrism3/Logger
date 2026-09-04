package me.prism3.logger.database;

import java.util.Map;


/**
 * Interface for logging server events to a database.
 * <p>
 * This interface defines the method for logging server-related events, such as memory usage,
 * CPU load, and other server metrics. Implementations of this interface should handle the
 * actual database interactions.
 */
public interface DatabaseServerLogger {
    void logEvent(final Map<String, String> placeholders);
}
