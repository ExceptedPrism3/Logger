package me.prism3.logger_discord_addon.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.prism3.logger_discord_addon.managers.DiscordManager;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
    id = "loggerdiscordaddon",
    name = "LoggerDiscordAddon",
    version = "1.8.4",
    authors = {"Prism3"},
    dependencies = {
        @Dependency(id = "logger-velocity", optional = true),
        @Dependency(id = "loggervelocity", optional = true),
        @Dependency(id = "logger", optional = true)
    }
)
public class LoggerDiscordAddonVelocity {

    private static LoggerDiscordAddonVelocity instance;
    private final ProxyServer server;
    private final org.slf4j.Logger logger;
    private final Path dataDirectory;
    private DiscordManager discordManager;
    private File activeConfigFile;

    @Inject
    public LoggerDiscordAddonVelocity(ProxyServer server, org.slf4j.Logger logger, @DataDirectory Path dataDirectory) {
        instance = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent event) {
        this.logger.info("Initializing Logger Discord Addon for Velocity...");

        me.prism3.loggervelocity.Logger host = me.prism3.loggervelocity.Logger.getInstance();
        if (host == null) {
            this.logger.error("Failed to initialize: Logger Velocity host instance not found!");
            return;
        }

        File hostConfig = new File(host.getFolder().toFile(), "discord.yml");
        File addonFolder = this.dataDirectory.toFile();
        File addonConfig = new File(addonFolder, "discord.yml");

        if (hostConfig.exists()) {
            this.activeConfigFile = hostConfig;
        } else if (addonConfig.exists()) {
            try {
                host.getFolder().toFile().mkdirs();
                Files.copy(addonConfig.toPath(), hostConfig.toPath());
                this.activeConfigFile = hostConfig;
                this.logger.info("Migrated discord.yml from legacy addon folder to " + hostConfig.getPath());
            } catch (Exception e) {
                this.activeConfigFile = addonConfig;
            }
        } else {
            host.getFolder().toFile().mkdirs();
            this.activeConfigFile = hostConfig;
            InputStream in = getClass().getResourceAsStream("/velocity-discord.yml");
            if (in == null) in = getClass().getResourceAsStream("/discord.yml");
            if (in != null) {
                try (InputStream input = in) {
                    Files.copy(input, this.activeConfigFile.toPath());
                } catch (Exception ignored) {}
            }
        }

        if (this.activeConfigFile.equals(hostConfig)) {
            cleanupLegacyFolder(addonFolder, host.getFolder().toFile());
        }

        this.initManager();
    }

    private void initManager() {
        if (this.discordManager != null) {
            this.discordManager.shutdown();
        }

        this.discordManager = new DiscordManager(this.activeConfigFile);
        this.discordManager.init();

        me.prism3.loggervelocity.Logger host = me.prism3.loggervelocity.Logger.getInstance();
        if (host != null) {
            host.setDiscordManager(this.discordManager);
        }
    }

    public void reload() {
        this.initManager();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (this.discordManager != null) {
            this.discordManager.shutdown();
        }
        this.logger.info("Logger Discord Addon disabled on Velocity.");
    }

    public static LoggerDiscordAddonVelocity getInstance() {
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
                this.logger.info("Removed legacy " + legacyFolder.getName() + " directory.");
            }
        } catch (Exception ignored) {}
    }
}
