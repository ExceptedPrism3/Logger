package me.prism3.loggerbungeecord.hooks;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.events.plugindependent.OnLiteBans;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class LiteBansUtil {

    private LiteBansUtil() {}

    public static boolean isAllowed = false;

    public static void getLiteBansHook() {

        if (getLiteBansAPI() != null && Main.getInstance().getConfig().getBoolean("Log-Extras.LiteBans")) {

            Main.getInstance().getProxy().getScheduler().schedule(Main.getInstance(), new OnLiteBans(), 5L, 0, TimeUnit.SECONDS);
            Log.info("LiteBans Plugin Detected!");

            isAllowed = true;
        }
    }

    private static Plugin getLiteBansAPI() { return Main.getInstance().getProxy().getPluginManager().getPlugin("LiteBans"); }
}
