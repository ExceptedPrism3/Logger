package me.prism3.loggervelocity.api;

import com.velocitypowered.api.plugin.PluginContainer;
import me.prism3.loggervelocity.Main;

import java.util.Optional;

public class LiteBansUtil {

    private LiteBansUtil() {}

    public static Optional<PluginContainer> getLiteBansAPI() {
        return Main.getServer().getPluginManager().getPlugin("LiteBans");
    }
}
