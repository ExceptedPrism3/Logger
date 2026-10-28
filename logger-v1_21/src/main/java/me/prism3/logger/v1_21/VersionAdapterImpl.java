package me.prism3.logger.v1_21;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.VersionAdapter;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.plugin.java.JavaPlugin;

public class VersionAdapterImpl implements VersionAdapter {
    @Override
    public void registerListeners(JavaPlugin plugin) {
        if (plugin instanceof LoggerAPI) {
            LoggerAPI api = (LoggerAPI) plugin;
            Data data = api.getData();
            if (data.isEnabled(LogType.PLAYER_VAULT_UNLOCK))
                plugin.getServer().getPluginManager().registerEvents(new me.prism3.logger.v1_21.listeners.VaultListener(api), plugin);
            if (data.isEnabled(LogType.PLAYER_MACE_SMASH))
                plugin.getServer().getPluginManager().registerEvents(new me.prism3.logger.v1_21.listeners.MaceListener(api), plugin);
            if (data.isEnabled(LogType.PLAYER_CRAFTER_CRAFT))
                plugin.getServer().getPluginManager().registerEvents(new me.prism3.logger.v1_21.listeners.CrafterListener(api), plugin);
            if (data.isEnabled(LogType.PLAYER_SCULK_SHRIEKER))
                plugin.getServer().getPluginManager().registerEvents(new me.prism3.logger.v1_21.listeners.SculkShriekerListener(api), plugin);
        }
        plugin.getLogger().info("Loaded 1.21+ / 26.x Adapter!");
    }
}
