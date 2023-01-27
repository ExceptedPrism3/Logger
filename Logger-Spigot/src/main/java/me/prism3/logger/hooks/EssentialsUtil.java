package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.events.plugindependent.OnAFK;
import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import static me.prism3.logger.utils.Data.options;

public class EssentialsUtil {

    private EssentialsUtil() {}

    public static boolean isAllowed = false;

    public static void getEssentialsHook() {

        final Plugin essentials = Bukkit.getServer().getPluginManager().getPlugin("Essentials");

        if (essentials != null && Main.getInstance().getConfig().getBoolean("Log-Extras.Essentials-AFK")) {

            Main.getInstance().getServer().getPluginManager().registerEvents(new OnAFK(), Main.getInstance());
            Log.info("Essentials PluginUpdater Detected!");
            isAllowed = true;
            options.setEssentialsEnabled(true);
        }
    }
}
