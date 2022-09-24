package me.prism3.logger.hooks;

import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class GeyserUtil {

    private GeyserUtil() {}

    public static void getGeyserHook() {

        if (getGeyserAPI() != null && FloodGateUtil.getFloodGateAPI()) {
            Log.info("Geyser & FloodGate Plugins Detected!");
            Log.warning("Geyser & FloodGate are not fully supported! If any errors occurs, contact the authors.");
        }
    }

    private static Plugin getGeyserAPI() {
        return Bukkit.getPluginManager().getPlugin("geyser-spigot");
    }
}