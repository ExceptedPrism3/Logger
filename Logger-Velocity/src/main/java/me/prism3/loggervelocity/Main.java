package me.prism3.loggervelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.prism3.loggervelocity.api.LiteBansUtil;
import me.prism3.loggervelocity.commands.DiscordCMD;
import me.prism3.loggervelocity.commands.Reload;
import me.prism3.loggervelocity.database.external.External;
import me.prism3.loggervelocity.database.external.ExternalData;
import me.prism3.loggervelocity.database.external.ExternalUpdater;
import me.prism3.loggervelocity.database.sqlite.SQLite;
import me.prism3.loggervelocity.database.sqlite.SQLiteData;
import me.prism3.loggervelocity.discord.Discord;
import me.prism3.loggervelocity.discord.DiscordFile;
import me.prism3.loggervelocity.events.OnChat;
import me.prism3.loggervelocity.events.OnLeave;
import me.prism3.loggervelocity.events.OnLogin;
import me.prism3.loggervelocity.events.oncommands.OnCommand;
import me.prism3.loggervelocity.events.plugindependent.litebans.OnLiteBanEvents;
import me.prism3.loggervelocity.serverside.RAM;
import me.prism3.loggervelocity.serverside.Start;
import me.prism3.loggervelocity.serverside.Stop;
import me.prism3.loggervelocity.utils.ConfigManager;
import me.prism3.loggervelocity.utils.Data;
import me.prism3.loggervelocity.utils.FileHandler;
import me.prism3.loggervelocity.utils.Messages;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static me.prism3.loggervelocity.utils.Data.*;

@Plugin(id = "logger-velocity", name = "Logger", version = "1.7.5", authors = {"prism3 & thelooter & sidna"})
public class Main{

    private static ProxyServer server;
    private final Logger logger;
    private final Metrics.Factory metricsFactory;

    private static Main instance;
    private ConfigManager config;

    private Messages messages;

    private Discord discord;
    private DiscordFile discordFile;

    private External external;

    private SQLite sqLite;

    @Inject
    @DataDirectory
    private Path folder;

    @Inject
    public Main(ProxyServer server, Logger logger, Metrics.Factory metricsFactory) {

        Main.server = server;
        this.logger = logger;
        this.metricsFactory = metricsFactory;

    }

    @Subscribe
    public void onEnable(ProxyInitializeEvent event) {

        instance = this;

        this.config = new ConfigManager();

        this.messages = new Messages();

        this.discordFile = new DiscordFile();

        this.discord = new Discord();
        this.discord.run();

        this.initializer(new Data());

        final FileHandler fileHandler = new FileHandler(this.folder.toFile());
        fileHandler.deleteFiles();

        // bStats
        this.metricsFactory.make(this, 12036);

        server.getEventManager().register(this, new OnChat());
        server.getEventManager().register(this, new OnCommand());
        server.getEventManager().register(this, new OnLogin());
        server.getEventManager().register(this, new OnLeave());

        server.getScheduler().buildTask(this, new RAM()).repeat(ramChecker, TimeUnit.SECONDS).delay(10, TimeUnit.SECONDS).schedule();

        server.getCommandManager().register("loggerproxy", new Reload());
        server.getCommandManager().register("loggerproxy", new DiscordCMD());

        this.dataBaseSetup();

        this.loadPluginDependent();

        new Start().run();

//        new ASCIIArt().Art();

        this.logger.info("Thanks to everyone's contributions that helped made this project possible!");

        this.logger.info("Plugin has been enabled");

    }

    @Subscribe
    public void onDisable(final ProxyShutdownEvent event) {

        new Stop().run();

        if (isExternal && this.external.isConnected()) this.external.disconnect();

        if (isSqlite && this.sqLite.isConnected()) this.sqLite.disconnect();

        this.discord.disconnect();

        this.logger.info("Plugin has been disabled");
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

    private void dataBaseSetup() {

        if (isExternal) {

            this.external = new External();
            this.external.connect();
            final ExternalData externalData = new ExternalData();
            if (this.external.isConnected()) {
                ExternalUpdater.updater();
                externalData.createTable();
                externalData.emptyTable();
            }
        }

        if (isSqlite) {

            this.sqLite = new SQLite();
            this.sqLite.connect();
            final SQLiteData sqLiteData = new SQLiteData();
            if (this.sqLite.isConnected()) {
                sqLiteData.createTable();
                sqLiteData.emptyTable();
            }
        }
    }

    private void loadPluginDependent() {

        if (LiteBansUtil.getLiteBansAPI().isPresent()) {

            server.getScheduler().buildTask(this, new OnLiteBanEvents()).delay(5, TimeUnit.SECONDS).schedule();

            this.logger.info("LiteBans Plugin Detected!");

        }
    }

    public static Main getInstance() { return instance; }

    public static ProxyServer getServer() { return server; }

    public Logger getLogger() { return this.logger; }

    public Path getFolder() { return this.folder; }

    public ConfigManager getConfig() { return this.config; }

    public DiscordFile getDiscordFile() { return this.discordFile; }

    public Discord getDiscord() { return this.discord; }

    public Messages getMessages() { return this.messages; }

    public External getExternal() { return this.external; }

    public SQLite getSqLite() { return this.sqLite; }
}
