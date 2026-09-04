package me.prism3.logger_core.platform;

import java.io.File;
import java.util.logging.Logger;

/**
 * Interface that defines platform-independent behavior.
 * Implemented by Spigot and Bungee modules.
 */
public interface LoggerPlatform {
    
    /**
     * Helper to get the plugin's data folder for configs/logs
     */
    File getDataFolder();
    
    /**
     * Helper to get the Java Logger instance
     */
    Logger getLogger();

    /**
     * Platform-specific scheduling (run async)
     */
    void runAsync(Runnable runnable);
    
    /**
     * Check if debug mode is enabled in config
     */
    boolean isDebug();
    
    /**
     * Print debug message if enabled
     */
    default void debug(String message) {
        if (isDebug()) {
            getLogger().info("[DEBUG] " + message);
        }
    }
}
