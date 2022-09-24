package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.events.plugindependent.OnAdvancedBan;
import me.prism3.logger.utils.Log;
import org.bukkit.plugin.Plugin;

import static me.prism3.logger.utils.Data.options;

public class AdvancedBanUtil {

    private AdvancedBanUtil() {}

    public static boolean isAllowed = false;

    public static void getAdvancedBanHook() {

        if (getAdvancedBanAPI() != null && Main.getInstance().getConfig().getBoolean("Log-Extras.AdvancedBan")) {

            Main.getInstance().getServer().getPluginManager().registerEvents(new OnAdvancedBan(), Main.getInstance());

            Log.info("AdvancedBan Plugin Detected!");

            options.setAdvancedBanEnabled(true);
            isAllowed = true;
        }
    }

    private static Plugin getAdvancedBanAPI() { return Main.getInstance().getServer().getPluginManager().getPlugin("AdvancedBan"); }
}
