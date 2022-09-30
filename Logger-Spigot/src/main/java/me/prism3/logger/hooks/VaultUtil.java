package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.events.plugindependent.OnVault;
import me.prism3.logger.utils.Log;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

import static me.prism3.logger.utils.Data.options;
import static me.prism3.logger.utils.Data.vaultChecker;
import static org.bukkit.Bukkit.getServer;

public class VaultUtil {

    private VaultUtil() {}

    public static boolean isAllowed = false;

    private static Economy econ = null;

    public static void getVaultHook() {

        if (getVault() && Main.getInstance().getConfig().getBoolean("Log-Extras.Vault")) {

            if (econ != null) {

                final OnVault vault = new OnVault();
                Main.getInstance().getServer().getPluginManager().registerEvents(vault, Main.getInstance());
                Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), vault, 10L, vaultChecker);
                options.setVaultEnabled(true);
                isAllowed = true;
            }

            Log.info("Vault Plugin Detected!");
        }
    }

    private static boolean getVault() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return false;

        econ = rsp.getProvider();

        return true;
    }

    public static Economy getVaultEcon() { return econ; }
}
