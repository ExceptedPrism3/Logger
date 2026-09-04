package me.prism3.logger_core.utils;

import me.prism3.logger_core.platform.LoggerPlatform;

import java.util.logging.Level;

/**
 * Static Logger utility for Core module.
 * Delegates to the registered LoggerPlatform.
 */
public class Log {
    
    private static LoggerPlatform platform;

    public static void init(LoggerPlatform platform) {
        Log.platform = platform;
    }

    public static void info(String message) {
        if (platform != null) platform.getLogger().info(message);
    }

    public static void warn(String message) {
        if (platform != null) platform.getLogger().warning(message);
    }

    public static void severe(String message) {
        if (platform != null) platform.getLogger().severe(message);
    }

    public static void severe(String message, Throwable t) {
        if (platform != null) platform.getLogger().log(Level.SEVERE, message, t);
    }

    public static void debug(String message) {
        if (platform != null) platform.debug(message);
    }
}
