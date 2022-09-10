package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import org.bukkit.plugin.Plugin;

public class LiteBansUtil {

    private LiteBansUtil() {}

    public static Plugin getLiteBansAPI() { return Main.getInstance().getServer().getPluginManager().getPlugin("LiteBans"); }
}
