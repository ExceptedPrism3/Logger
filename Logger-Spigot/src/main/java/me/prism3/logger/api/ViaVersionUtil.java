package me.prism3.logger.api;

import me.prism3.logger.Main;
import org.bukkit.plugin.Plugin;

public class ViaVersionUtil {

    private ViaVersionUtil() {}

    public static Plugin getViaVersionAPI() {

        return Main.getInstance().getServer().getPluginManager().getPlugin("ViaVersion");
    }
}
