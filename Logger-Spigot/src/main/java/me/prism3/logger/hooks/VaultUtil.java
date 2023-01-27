package me.prism3.logger.hooks;

import me.prism3.logger.Main;
import me.prism3.logger.events.plugindependent.OnVault;
import me.prism3.logger.utils.Log;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import static me.prism3.logger.utils.Data.options;
import static me.prism3.logger.utils.Data.vaultChecker;
import static org.bukkit.Bukkit.getServer;

public class VaultUtil {

    private VaultUtil() {}

    public static boolean isAllowed = false;
    private static Economy econ = null;

    public static void getVaultHook() {

        if (getVault()) {

            final OnVault vault = new OnVault();
            Main.getInstance().getServer().getPluginManager().registerEvents(vault, Main.getInstance());
            Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), vault, 10L, vaultChecker);

            options.setVaultEnabled(true);
            isAllowed = true;
        }
    }

    private static boolean getVault() {

        final Plugin vault = getServer().getPluginManager().getPlugin("Vault");

        if (vault != null && Main.getInstance().getConfig().getBoolean("Log-Extras.Vault")) {

            final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

            if (rsp != null) {
                econ = rsp.getProvider();
                Log.info("Vault PluginUpdater Detected!");
                return true;
            }
        }
        return false;
    }

    public static Economy getVaultEcon() { return econ; }
}
