package me.prism3.logger.hooks;

import fr.xephi.authme.AuthMe;
import org.bukkit.Bukkit;

public class AuthMeUtil {

    private AuthMeUtil() {}

    public static AuthMe getAuthMeAPI() {
        return (AuthMe) Bukkit.getPluginManager().getPlugin("AuthMe");
    }
}
