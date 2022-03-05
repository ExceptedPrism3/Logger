package me.prism3.loggervelocity.API;

import me.prism3.loggervelocity.Main;
import com.velocitypowered.api.plugin.PluginContainer;

import java.util.Optional;

public class LiteBansUtil {

    public static Optional<PluginContainer> getLiteBansAPI(){

        return Main.getServer().getPluginManager().getPlugin("LiteBans");
    }
}
