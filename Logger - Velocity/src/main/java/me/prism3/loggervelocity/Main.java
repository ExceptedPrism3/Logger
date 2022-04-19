package me.prism3.loggervelocity;

import me.prism3.loggervelocity.API.LiteBansUtil;
import me.prism3.loggervelocity.Commands.Reload;
import me.prism3.loggervelocity.Database.External.External;
import me.prism3.loggervelocity.Database.External.ExternalData;
import me.prism3.loggervelocity.Database.External.ExternalUpdater;
import me.prism3.loggervelocity.Database.SQLite.SQLite;
import me.prism3.loggervelocity.Database.SQLite.SQLiteData;
import me.prism3.loggervelocity.Discord.Discord;
import me.prism3.loggervelocity.Events.OnCommands.OnCommand;
import me.prism3.loggervelocity.Events.PluginDependent.LiteBans.OnLiteBanEvents;
import me.prism3.loggervelocity.ServerSide.RAM;
import me.prism3.loggervelocity.ServerSide.Start;
import me.prism3.loggervelocity.ServerSide.Stop;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.prism3.loggervelocity.Events.OnChat;
import me.prism3.loggervelocity.Events.OnLeave;
import me.prism3.loggervelocity.Events.OnLogin;
import me.prism3.loggervelocity.Utils.ConfigManager;
import me.prism3.loggervelocity.Utils.Data;
import me.prism3.loggervelocity.Utils.FileHandler;
import me.prism3.loggervelocity.Utils.Messages;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static me.prism3.loggervelocity.Utils.Data.*;

@Plugin(id = "logger-velocity", name = "Logger", version = "1.7.5", authors = {"prism3 & thelooter"})
public class Main{

    private static ProxyServer server;
    private final Logger logger;
    private final Metrics.Factory metricsFactory;

    private static Main instance;
    private ConfigManager config;

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
    public void onEnable(ProxyInitializeEvent event){

        instance = this;

        this.config = new ConfigManager();
        new Messages();
        new Discord().run();

        this.initializer(new Data());

        FileHandler fileHandler = new FileHandler(folder.toFile());
        fileHandler.deleteFiles();

        // bStats
        this.metricsFactory.make(this, 12036);

        server.getEventManager().register(this, new OnChat());
        server.getEventManager().register(this, new OnCommand());
        server.getEventManager().register(this, new OnLogin());
        server.getEventManager().register(this, new OnLeave());

        server.getScheduler().buildTask(this, new RAM()).repeat(ramChecker, TimeUnit.SECONDS).delay(10, TimeUnit.SECONDS).schedule();

        server.getCommandManager().register("loggerproxy", new Reload());

        this.dataBaseSetup();
        new ExternalUpdater();

        this.loadPluginDependent();

        new Start().run();

//        new ASCIIArt().Art();

        this.logger.info("Plugin has been enabled");

    }

    @Subscribe
    public void onDisable(ProxyShutdownEvent event){

        new Stop().run();

        if (isExternal && External.isConnected()) this.external.disconnect();

        if (isSqlite && SQLite.isConnected()) this.sqLite.disconnect();

        new Discord().disconnect();

        this.logger.info("Plugin has been disabled");
    }

    private void initializer(Data data){

        data.initializeDateFormatter();
        data.initializeStrings();
        data.initializeListOfStrings();
        data.initializeIntegers();
        data.initializeLongs();
        data.initializeBoolean();
        data.initializePermissionStrings();

    }

    private void dataBaseSetup(){

        if (isExternal) {

            this.external = new External();
            this.external.connect();
            final ExternalData externalData = new ExternalData(this);
            if (External.isConnected()) {
                externalData.createTable();
                externalData.emptyTable();
            }
        }

        if (isSqlite) {

            this.sqLite = new SQLite();
            this.sqLite.connect();
            final SQLiteData sqLiteData = new SQLiteData(this);
            if (SQLite.isConnected()) {
                sqLiteData.createTable();
                sqLiteData.emptyTable();
            }
        }
    }

    private void loadPluginDependent(){

        if (LiteBansUtil.getLiteBansAPI().isPresent()){

            server.getScheduler().buildTask(this, new OnLiteBanEvents()).delay(5, TimeUnit.SECONDS).schedule();

            this.logger.info("LiteBans Plugin Detected!");

        }
    }

    public static Main getInstance() { return instance; }

    public static ProxyServer getServer() { return server; }

    public Logger getLogger(){ return this.logger; }

    public Path getFolder() { return this.folder; }

    public ConfigManager getConfig() { return this.config; }

    public External getExternal() { return this.external; }

    public SQLite getSqLite() { return this.sqLite; }
}