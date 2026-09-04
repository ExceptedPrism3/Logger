package me.prism3.logger.events.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Bukkit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * TPSListener is responsible for monitoring and logging the server's TPS (Ticks Per Second).
 * It attempts to use the native method to get TPS, and falls back to a custom calculation if necessary.
 */
public class TPSListener implements Runnable {

    private final LoggerAPI plugin;
    private final Method getTpsMethod;
    private final long logIntervalTicks;

    // For fallback measurement
    private long fallbackStart = System.currentTimeMillis();
    private long tickCounter = 0;
    private long runCounter = 0;

    /**
     * Constructor for the TPSListener class.
     *
     * @param plugin           The LoggerAPI plugin instance.
     * @param logIntervalTicks The interval in ticks at which to log TPS.
     */
    public TPSListener(final LoggerAPI plugin, final long logIntervalTicks) {

        this.plugin = plugin;
        this.logIntervalTicks = logIntervalTicks;

        Method m;

        // Try to get the native method for TPS
        try {
            m = Bukkit.getServer().getClass().getMethod("getTPS");
        } catch (NoSuchMethodException e) {
            m = null;
        }

        this.getTpsMethod = m;
    }

    /**
     * Starts the TPS listener.
     */
    @Override
    public void run() {
        // Always count every tick
        tickCounter++;
        runCounter++;

        // Only perform a log check every logIntervalTicks
        if (runCounter < logIntervalTicks)
            return;

        runCounter = 0;  // reset for next interval

        double tps = -1;

        // 1) try native
        if (getTpsMethod != null) {
            try {
                final double[] arr = (double[]) getTpsMethod.invoke(Bukkit.getServer());
                tps = Math.round(arr[0]);  // 1-min TPS
            } catch (final Exception e) {
                Log.severe("Error invoking native TPS", e);
            }
        }

        // 2) fallback if native unavailable or errored
        if (tps < 0) {
            final long now = System.currentTimeMillis();
            final double elapsedSec = (now - fallbackStart) / 1000.0;
            // TPS = ticks / seconds, capped at 20
            tps = Math.min(20, Math.round(tickCounter / elapsedSec));
            // reset
            fallbackStart = now;
            tickCounter = 0;
        }

        final int intTps = (int) tps;

        // Log the TPS if it is below the threshold set in the config
        if (intTps <= this.plugin.getData().getTpsToLog()) {

            final Map<String,String> placeholders = new HashMap<>();
            placeholders.put("tps", String.valueOf(intTps));

            this.plugin.getLoggerManager().logEvent(LogType.SERVER_TPS, null, placeholders);
        }
    }
}
