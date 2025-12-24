package me.prism3.loggerbungeecord.api;

import me.prism3.loggerbungeecord.Logger;
import net.md_5.bungee.api.plugin.Plugin;

public class LiteBansUtil {

    private LiteBansUtil() {}

    public static Plugin getLiteBansAPI() {

        return Logger.getInstance().getProxy().getPluginManager().getPlugin("LiteBans");
    }
}
