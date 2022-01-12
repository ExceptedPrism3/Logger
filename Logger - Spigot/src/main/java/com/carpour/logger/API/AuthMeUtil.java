package com.carpour.logger.API;

import fr.xephi.authme.AuthMe;
import org.bukkit.Bukkit;

public class AuthMeUtil {

    public static AuthMe getAuthMeAPI(){

        return (AuthMe) Bukkit.getPluginManager().getPlugin("AuthMe");
    }
}
