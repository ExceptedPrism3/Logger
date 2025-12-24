package me.prism3.logger.api;

import me.prism3.logger.Main;
import org.bukkit.plugin.Plugin;

public class SuperiorSkyblockUtil {

    private SuperiorSkyblockUtil() {}

    public static Plugin getSuperiorSkyblockAPI() {

        return Main.getInstance().getServer().getPluginManager().getPlugin("SuperiorSkyblock");
    }
}
