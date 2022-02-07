package com.carpour.loggerbungeecord.API;

import com.carpour.loggerbungeecord.Main;
import net.md_5.bungee.api.plugin.Plugin;

public class LiteBansUtil {

    public static Plugin getLiteBansAPI(){

        return Main.getInstance().getProxy().getPluginManager().getPlugin("LiteBans");
    }
}
