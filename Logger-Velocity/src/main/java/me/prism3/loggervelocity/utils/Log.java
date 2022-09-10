package me.prism3.loggervelocity.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    private Log() {}

    private static Logger logger;

    public static void setup(final Logger log) {
        Log.logger = log;
    }

    public static void info(final String message) {
        logger.info(message);
    }

    public static void error(final String message) {
        logger.severe(message);
    }

    public static void error(final String message, final Throwable thrown) {
        logger.log(Level.SEVERE, message, thrown);
    }

    public static void warn(final String message) {
        logger.warning(message);
    }

    public static void warn(final String message, final Throwable thrown) {
        logger.log(Level.WARNING, message, thrown);
    }
}
