package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.events.plugindependent.OnAdvancedBan;
import me.prism3.logger.utils.Log;
import org.bukkit.plugin.Plugin;
import java.util.Optional;

import static me.prism3.logger.utils.Data.options;

public class AdvancedBanUtil {

    private AdvancedBanUtil() {}

    public static boolean isAllowed = false;
    private static final String ADVANCED_BAN_NAME = "AdvancedBan";

    public static void getAdvancedBanHook() {

        final Optional<Plugin> advancedBanAPI = Optional.ofNullable(Main.getInstance().getServer().getPluginManager().getPlugin(ADVANCED_BAN_NAME));
        advancedBanAPI.ifPresent(api -> {
            Main.getInstance().getServer().getPluginManager().registerEvents(new OnAdvancedBan(), Main.getInstance());

            Log.info("AdvancedBan Plugin Detected!");

            options.setAdvancedBanEnabled(true);
            isAllowed = true;
        });
    }
}
