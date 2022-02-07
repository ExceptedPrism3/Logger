package com.carpour.loggervelocity.API;

import com.carpour.loggervelocity.Main;
import com.velocitypowered.api.plugin.PluginContainer;

import java.util.Optional;

public class LiteBansUtil {

    public static Optional<PluginContainer> getLiteBansAPI(){

        return Main.getServer().getPluginManager().getPlugin("LiteBans");
    }
}
