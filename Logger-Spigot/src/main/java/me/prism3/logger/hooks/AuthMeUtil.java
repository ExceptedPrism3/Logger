package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.events.plugindependent.OnAuthMePassword;
import me.prism3.logger.utils.Log;
import org.bukkit.Bukkit;

import static me.prism3.logger.utils.Data.options;

public class AuthMeUtil {

    private AuthMeUtil() {}

    public static boolean isAllowed = false;

    public static void getAuthMeHook() {

        if (Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {

            Log.info("AuthMe Plugin Detected!");

            final OnAuthMePassword authMePassword = new OnAuthMePassword();
            Main.getInstance().getServer().getPluginManager().registerEvents(authMePassword, Main.getInstance());

            options.setAuthMeEnabled(true);
            isAllowed = true;
        }
    }
}
