package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import org.bukkit.plugin.Plugin;

import static me.prism3.logger.utils.Data.options;

public class ViaVersionUtil {

    private ViaVersionUtil() {}

    public static boolean isAllowed = false;
    
    public static void getViaVersionHook() {

        if (getViaVersionAPI() != null && Main.getInstance().getConfig().getBoolean("Log-Extras.ViaVersion")) {

            Log.info("ViaVersion Plugin Detected!");

            options.setViaVersion(true);
            isAllowed = true;
        }
    }

    private static Plugin getViaVersionAPI() { return Main.getInstance().getServer().getPluginManager().getPlugin("ViaVersion"); }
}
