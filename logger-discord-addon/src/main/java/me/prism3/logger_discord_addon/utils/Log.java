package me.prism3.logger_discord_addon.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Log class is a utility class that provides a simple interface for logging messages using the Java Logging API.
 * The class is final and contains only static methods, which cannot be overridden or instantiated,
 * making it impossible to create instances of it.
 */
public final class Log {

    private Log() {}

    private static Logger logger;

    /**
     * Sets up the logger to be used by the Log class.
     *
     * @param log The logger to be used.
     */
    public static void setup(final Logger log) { Log.logger = log; }

    private static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("LoggerDiscordAddon");
        }
        return logger;
    }

    /**
     * Logs an info level message.
     *
     * @param message The message to be logged.
     */
    public static void info(final String message) { getLogger().info(message); }

    /**
     * Logs a warning level message.
     *
     * @param message The message to be logged.
     */
    public static void warning(final String message) { getLogger().warning(message); }

    /**
     * Logs a severe level message.
     *
     * @param message The message to be logged.
     */
    public static void severe(final String message) { getLogger().severe(message); }

    /**
     * Logs a severe level message.
     *
     * @param message The message to be logged.
     * @param thrown the exception message.
     */
    public static void severe(final String message, final Throwable thrown) {
        getLogger().log(Level.SEVERE, message, thrown);
    }
}
