package me.prism3.logger.hooks;

import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class GeyserUtil {

    private GeyserUtil() {}

    public static void getGeyserHook() {

        final Optional<Plugin> geyserAPI = Optional.ofNullable(Bukkit.getPluginManager().getPlugin("geyser-spigot"));

        if (geyserAPI.isPresent() && FloodGateUtil.getFloodGateAPI()) {
            Log.info("Geyser & FloodGate Plugins Detected!");
            Log.warning("Geyser & FloodGate are not fully supported! If any errors occurs, contact the authors.");
        }
    }
}
