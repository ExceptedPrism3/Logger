package me.prism3.logger.api;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import org.bukkit.Bukkit;

public class EssentialsUtil {

    private EssentialsUtil() {}

    public static Essentials getEssentialsAPI() {

        final IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

        if (essentials instanceof Essentials) {

            return (Essentials) essentials;

        }else return null;
    }
}
