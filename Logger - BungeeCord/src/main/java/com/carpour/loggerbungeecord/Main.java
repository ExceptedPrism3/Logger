package com.carpour.loggerbungeecord;

import com.carpour.loggerbungeecord.API.LiteBansUtil;
import com.carpour.loggerbungeecord.Events.OnCommands.OnCommand;
import com.carpour.loggerbungeecord.Events.PluginDependent.LiteBans.OnLiteBanEvents;
import com.carpour.loggerbungeecord.ServerSide.RAM;
import com.carpour.loggerbungeecord.Database.SQLite.SQLite;
import com.carpour.loggerbungeecord.Database.SQLite.SQLiteData;
import com.carpour.loggerbungeecord.Commands.Reload;
import com.carpour.loggerbungeecord.Database.External.External;
import com.carpour.loggerbungeecord.Database.External.ExternalData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Discord.DiscordFile;
import com.carpour.loggerbungeecord.Events.*;
import com.carpour.loggerbungeecord.ServerSide.OnReload;
import com.carpour.loggerbungeecord.ServerSide.Start;
import com.carpour.loggerbungeecord.ServerSide.Stop;
import com.carpour.loggerbungeecord.Utils.*;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public final class Main extends Plugin {

    private static Main instance;

    private static ConfigManager cm;

    public External external;

    private SQLite sqLite;

    private Discord discord;

    @Override
    public void onEnable() {

        instance = this;

        cm = new ConfigManager();
        cm.init();
        
        new Messages().init();
        
        new DiscordFile().init();
        
        discord = new Discord();
        discord.run();
        
        FileHandler fileHandler = new FileHandler(getDataFolder());
        fileHandler.deleteFiles();

        if (getConfig().getBoolean("Log-to-Files") && getConfig().getBoolean("SQLite.Enable")){

            getLogger().warning("Logging to Files and SQLite are both enabled, this might impact your Server's Performance!");

        }

        getProxy().getPluginManager().registerListener(this, new OnChat());
        getProxy().getPluginManager().registerListener(this, new OnLogin());
        getProxy().getPluginManager().registerListener(this, new OnLeave());
        getProxy().getPluginManager().registerListener(this, new OnReload());
        getProxy().getPluginManager().registerListener(this, new OnCommand());

        // Plugin Dependent

        getProxy().getScheduler().schedule(this, new RAM(), 200L, getConfig().getInt("RAM.Checker") / 20, TimeUnit.SECONDS);

        getProxy().getPluginManager().registerCommand(this, new Reload());

        if (getConfig().getBoolean("Database.Enable")) {

            external = new External();
            external.connect();
            ExternalData externalData = new ExternalData(this);
            if (external.isConnected()) {
                externalData.createTable();
                externalData.emptyTable();
            }

        }

        if (getConfig().getBoolean("SQLite.Enable")) {

            sqLite = new SQLite();
            sqLite.connect();
            SQLiteData sqLiteData = new SQLiteData(this);
            if (sqLite.isConnected()) {
                sqLiteData.createTable();
                sqLiteData.emptyTable();
            }

        }

        new ASCIIArt().Art();

        // bstats
        new Metrics(this, 12036);

        // Update Checker
        new UpdateChecker().checkUpdates();

        if (LiteBansUtil.getLiteBansAPI() != null){

            getProxy().getScheduler().schedule(this, new OnLiteBanEvents(), 5L, 0, TimeUnit.SECONDS);

            getLogger().info("LiteBans Plugin Detected!");

        }

        getLogger().info("has been Enabled!");

        new Start().run();
    }

    @Override
    public void onDisable() {

        new Stop().run();

        if (getConfig().getBoolean("Database.Enable") && external.isConnected()) external.disconnect();

        if (getConfig().getBoolean("SQLite.Enable") && sqLite.isConnected()) sqLite.disconnect();

        discord.disconnect();

        getLogger().info("has been Disabled!");
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfig() { return cm; }

    public SQLite getSqLite() { return sqLite; }
}
