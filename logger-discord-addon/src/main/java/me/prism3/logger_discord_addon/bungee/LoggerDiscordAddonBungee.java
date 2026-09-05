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

        File hostConfig = new File(loggerBungee.getDataFolder(), "discord.yml");
        File addonFolder = getDataFolder();
        File addonConfig = new File(addonFolder, "discord.yml");

        if (hostConfig.exists()) {
            this.activeConfigFile = hostConfig;
        } else if (addonConfig.exists()) {
            try {
                loggerBungee.getDataFolder().mkdirs();
                Files.copy(addonConfig.toPath(), hostConfig.toPath());
                this.activeConfigFile = hostConfig;
                getLogger().info("Migrated discord.yml from legacy addon folder to " + hostConfig.getPath());
            } catch (Exception e) {
                this.activeConfigFile = addonConfig;
            }
        } else {
            loggerBungee.getDataFolder().mkdirs();
            this.activeConfigFile = hostConfig;
            InputStream in = getResourceAsStream("bungee-discord.yml");
            if (in == null) in = getResourceAsStream("discord.yml");
            if (in != null) {
                try (InputStream input = in) {
                    Files.copy(input, this.activeConfigFile.toPath());
                } catch (Exception ignored) {}
            }
        }

        if (this.activeConfigFile.equals(hostConfig)) {
            cleanupLegacyFolder(addonFolder, loggerBungee.getDataFolder());
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

    private void cleanupLegacyFolder(File legacyFolder, File hostFolder) {
        if (legacyFolder == null || !legacyFolder.exists()) return;
        try {
            if (legacyFolder.getCanonicalPath().equals(hostFolder.getCanonicalPath())) return;
            if (!legacyFolder.getName().toLowerCase().contains("discord")) return;
            File[] files = legacyFolder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        f.delete();
                    }
                }
            }
            if (legacyFolder.delete()) {
                getLogger().info("Removed legacy " + legacyFolder.getName() + " directory.");
            }
        } catch (Exception ignored) {}
    }
}
