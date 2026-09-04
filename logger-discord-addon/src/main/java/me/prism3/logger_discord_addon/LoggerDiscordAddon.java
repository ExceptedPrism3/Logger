package me.prism3.logger_discord_addon;

import me.prism3.logger_discord_addon.managers.DiscordManager;
import me.prism3.logger_discord_addon.utils.Log;
import me.prism3.logger_discord_addon.api.LoggerDiscordAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class LoggerDiscordAddon extends JavaPlugin {

    private DiscordManager discordManager;
    private Plugin loggerPlugin;
    private LoggerDiscordAPI api;

    @Override
    public void onEnable() {
        Log.setup(this.getLogger());
        Log.info("Initializing Logger Discord Addon for Spigot/Paper...");

        this.getServer().getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onReload(me.prism3.logger.events.LoggerReloadEvent event) {
                reloadConfig();
            }
        }, this);

        this.loggerPlugin = Bukkit.getServer().getPluginManager().getPlugin("Logger");
        if (this.loggerPlugin == null) {
            Log.severe("Logger plugin isn't present or not enabled.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        File configFile = new File(getDataFolder(), "discord.yml");
        if (!configFile.exists()) {
            File hostDiscord = new File(this.loggerPlugin.getDataFolder(), "discord.yml");
            if (hostDiscord.exists()) {
                configFile = hostDiscord;
            } else {
                getDataFolder().mkdirs();
                try (InputStream in = getResource("discord.yml")) {
                    if (in != null) Files.copy(in, configFile.toPath());
                } catch (Exception ignored) {}
            }
        }

        this.api = new LoggerDiscordAPI(this);
        this.registerAPI();

        this.discordManager = new DiscordManager(configFile);
        this.discordManager.init();

        if (this.loggerPlugin instanceof me.prism3.logger.LoggerAPI) {
            ((me.prism3.logger.LoggerAPI) this.loggerPlugin).setDiscordManager(this.discordManager);
        }
    }

    @Override
    public void onDisable() {
        if (this.discordManager != null) {
            this.discordManager.shutdown();
            if (this.loggerPlugin instanceof me.prism3.logger.LoggerAPI) {
                ((me.prism3.logger.LoggerAPI) this.loggerPlugin).setDiscordManager(null);
            }
        }
        Log.info("Logger Discord Addon disabled.");
    }

    private void registerAPI() {
        Bukkit.getServicesManager().register(LoggerDiscordAPI.class, this.api, this,
                org.bukkit.plugin.ServicePriority.Normal);
    }

    public DiscordManager getDiscordManager() {
        return this.discordManager;
    }

    public LoggerDiscordAPI getAPI() {
        return this.api;
    }

    public Plugin getLoggerPlugin() {
        return this.loggerPlugin;
    }

    public boolean isAddonEnabled() {
        return this.discordManager != null && this.discordManager.isEnabled();
    }

    @Override
    public void reloadConfig() {
        if (this.discordManager != null) {
            this.discordManager.reload();
        }
    }
}
