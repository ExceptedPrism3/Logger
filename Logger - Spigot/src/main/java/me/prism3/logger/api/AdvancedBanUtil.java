package me.prism3.logger.api;

import me.prism3.logger.Main;
import org.bukkit.plugin.Plugin;

public class AdvancedBanUtil {

    private AdvancedBanUtil() {}

    public static Plugin getAdvancedBanAPI() {
        return Main.getInstance().getServer().getPluginManager().getPlugin("AdvancedBan");
    }
}
