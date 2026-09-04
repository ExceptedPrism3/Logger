package me.prism3.loggervelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.prism3.logger_core.database.DatabaseConfig;
import me.prism3.logger_core.database.DatabaseManager;
import me.prism3.logger_core.discord.DiscordManager;
import me.prism3.logger_core.platform.LoggerPlatform;
import me.prism3.loggervelocity.commands.LoggerProxyCommands;
import me.prism3.loggervelocity.events.*;
import me.prism3.loggervelocity.events.oncommands.OnCommand;
import me.prism3.loggervelocity.managers.LogManager;
import me.prism3.loggervelocity.serverside.RAM;
import me.prism3.loggervelocity.serverside.Start;
import me.prism3.loggervelocity.serverside.Stop;
import me.prism3.loggervelocity.utils.ConfigManager;
import me.prism3.loggervelocity.utils.Data;
import me.prism3.loggervelocity.utils.FileHandler;
import me.prism3.loggervelocity.utils.Messages;
import org.bstats.velocity.Metrics;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static me.prism3.loggervelocity.utils.Data.ramChecker;

@Plugin(id = "logger-velocity", name = "Logger", version = "1.8.3", authors = { "prism3 & thelooter & sidna" })
public class Logger implements LoggerPlatform {

    private static ProxyServer server;
    private final org.slf4j.Logger logger;
    private final Metrics.Factory metricsFactory;

    private static Logger instance;
    private ConfigManager config;

    private Messages messages;

    private DiscordManager discordManager;
    private DatabaseManager databaseManager;
    private LogManager logManager;

    @Inject
    @DataDirectory
    private Path folder;

    @Inject
    public Logger(ProxyServer server, org.slf4j.Logger logger, Metrics.Factory metricsFactory) {

        Logger.server = server;
        this.logger = logger;
        this.metricsFactory = metricsFactory;

    }

    @Subscribe
    public void onEnable(ProxyInitializeEvent event) {

        instance = this;

        this.config = new ConfigManager();
        this.messages = new Messages();

        this.initializer(new Data());

        final FileHandler fileHandler = new FileHandler(this.folder.toFile());
        fileHandler.deleteFiles();

        // bStats
        this.metricsFactory.make(this, 12036);

        // Core Database Initialization
        this.initDatabase();

        // Manager Initialization
        this.logManager = new LogManager(this);

        server.getEventManager().register(this, new OnChat());
        server.getEventManager().register(this, new OnCommand());
        server.getEventManager().register(this, new OnLogin());
        server.getEventManager().register(this, new OnLeave());
        server.getEventManager().register(this, new OnSwitch());
        server.getEventManager().register(this, new OnKick());

        server.getScheduler().buildTask(this, new RAM()).repeat(ramChecker, TimeUnit.SECONDS)
                .delay(10, TimeUnit.SECONDS).schedule();

        server.getCommandManager().register("loggerproxy", new LoggerProxyCommands());

        new Start().run();

        this.logger.info("Thanks to everyone's contributions that helped made this project possible!");
        this.logger.info("Plugin has been enabled");

    }

    @Subscribe
    public void onDisable(final ProxyShutdownEvent event) {

        new Stop().run();

        if (this.discordManager != null)
            this.discordManager.shutdown();
        if (this.databaseManager != null)
            this.databaseManager.shutdown();

        this.logger.info("Plugin has been disabled");
    }

    public void initDatabase() {
        if (this.databaseManager != null) {
            this.databaseManager.shutdown();
            this.databaseManager = null;
        }

        if (this.config.getBoolean("Database.Enable")) {
            DatabaseConfig dbConfig = new DatabaseConfig(
                    true,
                    this.config.getString("Database.Type"),
                    this.config.getString("Database.Host"),
                    this.config.getInt("Database.Port"),
                    this.config.getString("Database.Database"),
                    this.config.getString("Database.Username"),
                    this.config.getString("Database.Password"),
                    "logger_",
                    this.config.getInt("Database.Data-Deletion"));
            this.databaseManager = new DatabaseManager(this, dbConfig);
            this.databaseManager.initialize();
        } else if (this.config.getBoolean("SQLite.Enable")) {
            DatabaseConfig dbConfig = new DatabaseConfig(
                    true,
                    "sqlite",
                    "localhost",
                    3306,
                    "logger",
                    "root",
                    "",
                    "logger_",
                    this.config.getInt("SQLite.Data-Deletion"));
            this.databaseManager = new DatabaseManager(this, dbConfig);
            this.databaseManager.initialize();
        }
    }

    public void reload() {
        this.config.reload();
        this.messages.reload();

        if (this.discordManager != null) {
            this.discordManager.reload();
        }

        this.initDatabase();
    }

    private void initializer(Data data) {
        data.initializeDateFormatter();
        data.initializeStrings();
        data.initializeListOfStrings();
        data.initializeIntegers();
        data.initializeLongs();
        data.initializeBoolean();
        data.initializePermissionStrings();
    }

    public static Logger getInstance() {
        return instance;
    }

    public static ProxyServer getServer() {
        return server;
    }

    @Override
    public java.util.logging.Logger getLogger() {
        return java.util.logging.Logger.getLogger("Logger");
    }

    public org.slf4j.Logger getSLF4JLogger() {
        return this.logger;
    }

    public Path getFolder() {
        return this.folder;
    }

    @Override
    public java.io.File getDataFolder() {
        return this.folder.toFile();
    }

    public ConfigManager getConfig() {
        return this.config;
    }

    public void setDiscordManager(DiscordManager discordManager) {
        this.discordManager = discordManager;
    }

    public DiscordManager getDiscordManager() {
        return this.discordManager;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public LogManager getLogManager() {
        return this.logManager;
    }

    public Messages getMessages() {
        return this.messages;
    }

    @Override
    public void runAsync(Runnable runnable) {
        server.getScheduler().buildTask(this, runnable).schedule();
    }

    @Override
    public boolean isDebug() {
        return false;
    }
}
