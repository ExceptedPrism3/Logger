package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.events.plugindependent.OnLiteBanEvents;
import me.prism3.logger.utils.Log;
import org.bukkit.plugin.Plugin;

import static me.prism3.logger.utils.Data.options;

public class LiteBanUtil {

    private LiteBanUtil() {}

    public static boolean isAllowed = false;

    public static void getLiteBanHook() {

        if (getLiteBansAPI() != null && Main.getInstance().getConfig().getBoolean("Log-Extras.LiteBans")) {

            Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new OnLiteBanEvents(), 10L);

            Log.info("LiteBans Plugin Detected!");

            options.setLiteBansEnabled(true);
            isAllowed = true;
        }
    }

    private static Plugin getLiteBansAPI() { return Main.getInstance().getServer().getPluginManager().getPlugin("LiteBans"); }
}
