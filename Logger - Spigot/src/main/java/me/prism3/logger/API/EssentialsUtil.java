package me.prism3.logger.API;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import org.bukkit.Bukkit;

public class EssentialsUtil {

    public static Essentials getEssentialsAPI(){

        final IEssentials essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

        if (essentials instanceof Essentials){

            return (Essentials) essentials;

        }else return null;
    }
}
