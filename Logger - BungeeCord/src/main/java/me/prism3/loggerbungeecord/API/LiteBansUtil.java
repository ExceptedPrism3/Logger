package me.prism3.loggerbungeecord.API;

import me.prism3.loggerbungeecord.Main;
import net.md_5.bungee.api.plugin.Plugin;

public class LiteBansUtil {

    public static Plugin getLiteBansAPI(){

        return Main.getInstance().getProxy().getPluginManager().getPlugin("LiteBans");
    }
}
