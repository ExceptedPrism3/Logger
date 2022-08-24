package me.prism3.loggervelocity;

import me.prism3.loggercore.database.DataSourceInterface;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.prism3.loggervelocity.commands.DiscordCMD;
import me.prism3.loggervelocity.commands.Reload;
import me.prism3.loggervelocity.discord.Discord;
import me.prism3.loggervelocity.discord.DiscordFile;
import me.prism3.loggervelocity.serverside.Start;
import me.prism3.loggervelocity.serverside.Stop;
import me.prism3.loggervelocity.utils.ConfigManager;
import me.prism3.loggervelocity.utils.Data;
import me.prism3.loggervelocity.utils.FileHandler;
import me.prism3.loggervelocity.utils.Messages;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.nio.file.Path;

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

    private DataSourceInterface database;
    private DataSourceInterface sqLite;
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
    public void onEnable(final ProxyInitializeEvent event) {

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

        server.getCommandManager().register("loggerv", new Reload());
        server.getCommandManager().register("loggerv", new DiscordCMD());

        this.dataBaseSetup();

        new Start().run();

//        new ASCIIArt().Art();

        this.logger.info("Thanks to everyone's contributions that helped made this project possible!");

        this.logger.info("Plugin has been enabled");

    }

    @Subscribe
    public void onDisable(final ProxyShutdownEvent event) {

        new Stop().run();

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
        data.eventInitializer();

    }

    private void dataBaseSetup() {
//TODO DB Hna

    }

    public static Main getInstance() { return instance; }

    public static ProxyServer getServer() { return server; }

    public Logger getLogger() { return this.logger; }

    public Path getFolder() { return this.folder; }

    public ConfigManager getConfig() { return this.config; }

    public DiscordFile getDiscordFile() { return this.discordFile; }

    public Discord getDiscord() { return this.discord; }

    public Messages getMessages() { return this.messages; }

    public DataSourceInterface getDatabase()
    {
        return this.database;
    }

    public DataSourceInterface getSqLite()
    {
        return this.sqLite;
    }
}
