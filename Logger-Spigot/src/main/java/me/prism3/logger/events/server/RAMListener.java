package me.prism3.logger.events.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.enums.LogType;

import java.util.HashMap;
import java.util.Map;


/**
 * Logs the server's RAM usage.
 * <p>
 * This is a scheduled task that runs every X minutes.
 * It checks the current RAM usage and logs it if it exceeds the configured threshold.
 */
public class RAMListener implements Runnable {

    private final LoggerAPI plugin;

    public RAMListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Run the task.
     * <p>
     * This method is called by the Bukkit scheduler.
     * It checks the current RAM usage and logs it if it exceeds the configured threshold.
     */
    @Override
    public void run() {

        final long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);  // Convert to MB
        final long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        final long usedMemory = totalMemory - freeMemory;

        if (usedMemory < this.plugin.getData().getRamToLog())
            return;

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("total", String.valueOf(totalMemory));
        placeholders.put("free", String.valueOf(freeMemory));
        placeholders.put("used", String.valueOf(usedMemory));

        this.plugin.getLoggerManager().logEvent(LogType.SERVER_RAM, null, placeholders);
    }
}
