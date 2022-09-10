package me.prism3.loggervelocity.hooks;

import com.velocitypowered.api.plugin.PluginContainer;
import me.prism3.loggervelocity.Main;

import java.util.Optional;

public class LiteBanUtil {

    private LiteBanUtil() {}

    public static Optional<PluginContainer> getLiteBansAPI() { return Main.getServer().getPluginManager().getPlugin("LiteBans"); }
}
