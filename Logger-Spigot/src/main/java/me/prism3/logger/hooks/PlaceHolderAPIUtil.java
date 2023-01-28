package me.prism3.logger.hooks;

import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PlaceHolderAPIUtil {

    private PlaceHolderAPIUtil() {}

    public static void getPlaceHolderHook() {

        final Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (plugin != null)
            Log.info("PlaceHolderAPI Plugin Detected!");
    }
}
