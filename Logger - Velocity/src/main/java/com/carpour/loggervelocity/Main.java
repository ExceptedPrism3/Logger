package com.carpour.loggervelocity;

import com.carpour.loggervelocity.API.LiteBansUtil;
import com.carpour.loggervelocity.Commands.Reload;
import com.carpour.loggervelocity.Database.MySQL.MySQL;
import com.carpour.loggervelocity.Database.MySQL.MySQLData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Events.*;
import com.carpour.loggervelocity.Events.OnCommands.OnCommand;
import com.carpour.loggervelocity.Events.PluginDependent.LiteBans.OnLiteBanEvents;
import com.carpour.loggervelocity.ServerSide.RAM;
import com.carpour.loggervelocity.ServerSide.Start;
import com.carpour.loggervelocity.ServerSide.Stop;
import com.carpour.loggervelocity.Utils.*;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(id = "logger-velocity", name = "Logger", version = "1.7.1", authors = {"prism3, thelooter"})
public class Main{

    private static ProxyServer server;
    private final Logger logger;
    private final Metrics.Factory metricsFactory;

    private static Main instance;
    private ConfigManager config;

    private Discord discord;

    public MySQL mySQL;

    public SQLite sqLite;

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

        config = new ConfigManager();
        new Messages();
        discord = new Discord();
        discord.run();

        ConfigManager config = new ConfigManager();

        FileHandler fileHandler = new FileHandler(folder.toFile());
        fileHandler.deleteFiles();

        // bstats
        metricsFactory.make(this, Bstats.pluginID);

        server.getEventManager().register(this, new OnChat());
        server.getEventManager().register(this, new OnCommand());
        server.getEventManager().register(this, new OnLogin());
        server.getEventManager().register(this, new OnLeave());

        long timeRAM = new ConfigManager().getLong("RAM.Checker");
        server.getScheduler().buildTask(this, new RAM()).repeat(timeRAM, TimeUnit.SECONDS).delay(10, TimeUnit.SECONDS).schedule();

        server.getCommandManager().register("loggerproxy", new Reload());

        if (config.getBoolean("MySQL.Enable")) {

            mySQL = new MySQL();
            mySQL.connect();
            MySQLData mySQLData = new MySQLData(this);
            if (MySQL.isConnected()) {
                mySQLData.createTable();
                mySQLData.emptyTable();
            }
        }

        if (config.getBoolean("SQLite.Enable")) {

            sqLite = new SQLite();
            sqLite.connect();
            SQLiteData sqLiteData = new SQLiteData(this);
            if (SQLite.isConnected()) {
                sqLiteData.createTable();
                sqLiteData.emptyTable();
            }
        }

        if (LiteBansUtil.getLiteBansAPI().isPresent()){

            server.getScheduler().buildTask(this, new OnLiteBanEvents()).delay(5, TimeUnit.SECONDS).schedule();

            getLogger().info("LiteBans Plugin Detected!");

        }

        new Start().run();

//        new ASCIIArt().Art();

        logger.info("Plugin has been enabled");

    }

    @Subscribe
    public void onDisable(ProxyShutdownEvent event){

        ConfigManager config = new ConfigManager();

        new Stop().run();

        if (config.getBoolean("MySQL.Enable") && MySQL.isConnected()) mySQL.disconnect();

        if (config.getBoolean("SQLite.Enable") && SQLite.isConnected()) sqLite.disconnect();

        discord.disconnect();

        logger.info("Plugin has been disabled");
    }

    public static Main getInstance() { return instance; }

    public static ProxyServer getServer() { return server; }

    public Logger getLogger(){ return logger; }

    public Path getFolder() { return folder; }

    public ConfigManager getConfig() { return config; }
}
