package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import org.bukkit.plugin.Plugin;

public class WorldGuardUtil {

    private WorldGuardUtil() {}

    public static void getWorldGuardHook() {
        if (getWorldGuardAPI() != null)
            Log.info("WorldGuard Plugin Detected!");
    }

    private static Plugin getWorldGuardAPI() { return Main.getInstance().getServer().getPluginManager().getPlugin("WorldGuard"); }
}
