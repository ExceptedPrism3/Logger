package me.prism3.loggerbungeecord.hooks;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.events.plugindependent.OnAdvancedBan;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.plugin.Plugin;

public class AdvancedBanUtil {//todo on velo

    private AdvancedBanUtil() {}

    public static boolean isAllowed = false;

    public static void getAdvancedBanHook() {

        if (getAdvancedBanAPI() != null && Main.getInstance().getConfig().getBoolean("Log-Extras.AdvancedBan")) {

            Main.getInstance().getProxy().getPluginManager().registerListener(Main.getInstance(), new OnAdvancedBan());

            Log.info("AdvancedBan Plugin Detected!");

            isAllowed = true;
        }
    }

    private static Plugin getAdvancedBanAPI() { return Main.getInstance().getProxy().getPluginManager().getPlugin("AdvancedBan"); }
}
