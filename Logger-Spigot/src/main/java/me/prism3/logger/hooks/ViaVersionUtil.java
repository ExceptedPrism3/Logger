package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

import static me.prism3.logger.utils.Data.options;

public class ViaVersionUtil {

    private ViaVersionUtil() {}

    public static boolean isAllowed = false;

    public static void getViaVersionHook() {

        final Optional<Plugin> viaVersionAPI = Optional.ofNullable(Main.getInstance().getServer().getPluginManager().getPlugin("ViaVersion"));

        viaVersionAPI.filter(p -> Main.getInstance().getConfig().getBoolean("Log-Extras.ViaVersion"))
                .ifPresent(p -> {
                    Log.info("ViaVersion Plugin Detected!");
                    options.setViaVersion(true);
                });
    }
}
