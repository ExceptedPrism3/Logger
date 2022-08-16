package me.prism3.logger.api;

import me.prism3.logger.Main;
import org.bukkit.plugin.Plugin;

public class PlaceHolderAPIUtil {

    private PlaceHolderAPIUtil() {}

    public static Plugin getPlaceHolderAPI() {
        return Main.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI");
    }
}
