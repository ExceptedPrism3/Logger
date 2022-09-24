package me.prism3.logger.hooks;

import fr.xephi.authme.AuthMe;
import me.prism3.logger.Main;
import me.prism3.logger.events.plugindependent.OnAuthMePassword;
import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;

import static me.prism3.logger.utils.Data.options;

public class AuthMeUtil {

    private AuthMeUtil() {}

    public static boolean isAllowed = false;

    public static void getAuthMeHook() {

        if (getAuthMe() != null && Main.getInstance().getConfig().getBoolean("Log-Extras.AuthMe-Wrong-Password")) {

            Main.getInstance().getServer().getPluginManager().registerEvents(new OnAuthMePassword(), Main.getInstance());

            Log.info("AuthMe Plugin Detected!");

            options.setAuthMeEnabled(true);
            isAllowed = true;
        }
    }

    private static AuthMe getAuthMe() {
        return (AuthMe) Bukkit.getPluginManager().getPlugin("AuthMe");
    }
}
