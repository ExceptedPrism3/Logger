package me.prism3.logger_bungee.utils;

import me.prism3.logger_bungee.LoggerBungee;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    private Log() {}

    private static Logger logger;
    private static LoggerBungee plugin;

    public static void setup(final Logger log) {
        Log.logger = log;
    }

    public static void setPlugin(final LoggerBungee plugin) {
        Log.plugin = plugin;
        if (plugin != null) {
            Log.logger = plugin.getLogger();
        }
    }

    private static Logger getLogger() {
        if (logger != null) return logger;
        if (plugin != null) return plugin.getLogger();
        return Logger.getLogger("LoggerBungee");
    }

    public static void info(final String message) {
        getLogger().info(message);
    }

    public static void warn(final String message) {
        getLogger().warning(message);
    }

    public static void warning(final String message) {
        getLogger().warning(message);
    }

    public static void severe(final String message) {
        getLogger().severe(message);
    }

    public static void severe(final String message, final Throwable thrown) {
        getLogger().log(Level.SEVERE, message, thrown);
    }

    public static void debug(final String message) {
        if (plugin != null && plugin.isDebug()) {
            getLogger().info("[Logger Debug] " + message);
        }
    }
}
