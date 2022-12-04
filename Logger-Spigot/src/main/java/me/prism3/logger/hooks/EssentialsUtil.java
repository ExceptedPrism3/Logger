package me.prism3.logger.hooks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import me.prism3.logger.Main;
import me.prism3.logger.events.plugindependent.OnAFK;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;

import static me.prism3.logger.utils.Data.options;

public class EssentialsUtil {

    private EssentialsUtil() {}

    public static boolean isAllowed = false;

    public static void getEssentialsHook() {

        if (getEssentials() != null && Main.getInstance().getConfig().getBoolean("Log-Extras.Essentials-AFK")) {

            Data.options.setEssentialsEnabled(true);

            Main.getInstance().getServer().getPluginManager().registerEvents(new OnAFK(), Main.getInstance());

            Log.info("Essentials Plugin Detected!");

            isAllowed = true;
        }
    }

    private static Essentials getEssentials() {

        final IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

        if (essentials instanceof Essentials) {

            return (Essentials) essentials;

        } else return null;
    }
}
