package me.prism3.logger.v1_21;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.VersionAdapter;
import org.bukkit.plugin.Plugin; // Changed from JavaPlugin to Plugin
import org.bukkit.plugin.java.JavaPlugin; // Keep this if other parts of the file use it, otherwise remove. For this change, it's not explicitly removed.

public class VersionAdapterImpl implements VersionAdapter {
    @Override
    public void registerListeners(JavaPlugin plugin) {
        // Register 1.21 specific listeners here
        if (plugin instanceof LoggerAPI) {
            LoggerAPI api = (LoggerAPI) plugin;
            plugin.getServer().getPluginManager().registerEvents(new me.prism3.logger.v1_21.listeners.VaultListener(api), plugin);
            plugin.getServer().getPluginManager().registerEvents(new me.prism3.logger.v1_21.listeners.MaceListener(api), plugin);
            plugin.getServer().getPluginManager().registerEvents(new me.prism3.logger.v1_21.listeners.CrafterListener(api), plugin);
            plugin.getServer().getPluginManager().registerEvents(new me.prism3.logger.v1_21.listeners.SculkShriekerListener(api), plugin);
        }
        plugin.getLogger().info("Loaded 1.21 Adapter!");
    }
}
