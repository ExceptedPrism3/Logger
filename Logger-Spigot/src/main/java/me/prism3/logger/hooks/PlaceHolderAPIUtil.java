package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import org.bukkit.plugin.Plugin;

public class PlaceHolderAPIUtil {

    private PlaceHolderAPIUtil() {}

    public static void getPlaceHolderHook() {
        if (getPlaceHolderAPI() != null)
            Log.info("PlaceHolderAPI Plugin Detected!");
    }

    private static Plugin getPlaceHolderAPI() { return Main.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI"); }
}
