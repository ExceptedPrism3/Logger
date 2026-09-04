package me.prism3.logger_discord_addon.bungee;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_discord_addon.managers.DiscordManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class LoggerDiscordAddonBungee extends Plugin {

    private static LoggerDiscordAddonBungee instance;
    private DiscordManager discordManager;
    private File activeConfigFile;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Initializing Logger Discord Addon for BungeeCord...");

        Plugin host = getProxy().getPluginManager().getPlugin("LoggerBungee");
        if (host == null) {
            host = getProxy().getPluginManager().getPlugin("Logger");
        }

        if (host == null || !(host instanceof LoggerBungee)) {
            getLogger().severe("Failed to initialize: LoggerBungee plugin not found or not enabled!");
            return;
        }

        LoggerBungee loggerBungee = (LoggerBungee) host;

        File addonConfig = new File(getDataFolder(), "discord.yml");
        File hostConfig = new File(loggerBungee.getDataFolder(), "discord.yml");

        if (addonConfig.exists()) {
            this.activeConfigFile = addonConfig;
        } else if (hostConfig.exists()) {
            this.activeConfigFile = hostConfig;
        } else {
            getDataFolder().mkdirs();
            this.activeConfigFile = addonConfig;
            InputStream in = getResourceAsStream("bungee-discord.yml");
            if (in == null) in = getResourceAsStream("discord.yml");
            if (in != null) {
                try (InputStream input = in) {
                    Files.copy(input, this.activeConfigFile.toPath());
                } catch (Exception ignored) {}
            }
        }

        this.initManager();
    }

    private void initManager() {
        if (this.discordManager != null) {
            this.discordManager.shutdown();
        }

        this.discordManager = new DiscordManager(this.activeConfigFile);
        this.discordManager.init();

        // Always register the DiscordManager instance to the host plugin so reloads can trigger it
        if (LoggerBungee.getInstance() != null) {
            LoggerBungee.getInstance().setDiscordManager(this.discordManager);
        }
    }

    public void reload() {
        this.initManager();
    }

    @Override
    public void onDisable() {
        if (this.discordManager != null) {
            this.discordManager.shutdown();
        }
        getLogger().info("Logger Discord Addon disabled on BungeeCord.");
    }

    public static LoggerDiscordAddonBungee getInstance() {
        return instance;
    }

    public DiscordManager getDiscordManager() {
        return this.discordManager;
    }
}
