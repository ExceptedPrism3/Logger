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
    version = "1.8.3",
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

        File addonConfig = new File(this.dataDirectory.toFile(), "discord.yml");
        File hostConfig = new File(host.getFolder().toFile(), "discord.yml");

        if (addonConfig.exists()) {
            this.activeConfigFile = addonConfig;
        } else if (hostConfig.exists()) {
            this.activeConfigFile = hostConfig;
        } else {
            this.dataDirectory.toFile().mkdirs();
            this.activeConfigFile = addonConfig;
            InputStream in = getClass().getResourceAsStream("/velocity-discord.yml");
            if (in == null) in = getClass().getResourceAsStream("/discord.yml");
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
}
