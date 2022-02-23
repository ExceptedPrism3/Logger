package com.carpour.logger.API;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class VaultUtil {

    private static Economy econ = null;

    public static boolean getVaultAPI() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return false;

        econ = rsp.getProvider();

        return true;
    }
    public static Economy getVault() { return econ; }
}
