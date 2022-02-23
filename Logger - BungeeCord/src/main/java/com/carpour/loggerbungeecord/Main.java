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

import static com.carpour.loggerbungeecord.Utils.Data.*;

public final class Main extends Plugin {

    private static Main instance;

    private ConfigManager cm;

    private External external;

    private SQLite sqLite;

    private Discord discord;

    @Override
    public void onEnable() {

        instance = this;

        this.cm = new ConfigManager();
        this.cm.init();

        initializer(new Data());
        
        new Messages().init();
        
        new DiscordFile().init();
        
        this.discord = new Discord();
        this.discord.run();
        
        FileHandler fileHandler = new FileHandler(getDataFolder());
        fileHandler.deleteFiles();

        databaseSetup();

        if (isLogToFiles && isSqlite){

            getLogger().warning("Logging to Files and SQLite are both enabled, this might impact your Server's Performance!");

        }

        getProxy().getPluginManager().registerListener(this, new OnChat());
        getProxy().getPluginManager().registerListener(this, new OnLogin());
        getProxy().getPluginManager().registerListener(this, new OnLeave());
        getProxy().getPluginManager().registerListener(this, new OnReload());
        getProxy().getPluginManager().registerListener(this, new OnCommand());

        getProxy().getScheduler().schedule(this, new RAM(), 200L, ramChecker / 20, TimeUnit.SECONDS);

        getProxy().getPluginManager().registerCommand(this, new Reload());

        loadPluginDepends();

        new ASCIIArt().Art();

        // bstats
        new Metrics(this, 12036);

        // Update Checker
        new UpdateChecker().checkUpdates();

        getLogger().info("has been Enabled!");

        new Start().run();
    }

    @Override
    public void onDisable() {

        new Stop().run();

        if (isExternal && this.external.isConnected()) this.external.disconnect();

        if (isSqlite && this.sqLite.isConnected()) this.sqLite.disconnect();

        this.discord.disconnect();

        getLogger().info("has been Disabled!");
    }

    private void initializer(Data data){

        data.initializeBoolean();

    }

    private void databaseSetup(){

        if (isExternal) {

            this.external = new External();
            this.external.connect();
            ExternalData externalData = new ExternalData(this);
            if (this.external.isConnected()) {
                externalData.createTable();
                externalData.emptyTable();
            }
        }

        if (isSqlite) {

            this.sqLite = new SQLite();
            this.sqLite.connect();
            SQLiteData sqLiteData = new SQLiteData(this);
            if (this.sqLite.isConnected()) {
                sqLiteData.createTable();
                sqLiteData.emptyTable();
            }
        }
    }

    private void loadPluginDepends(){

        if (LiteBansUtil.getLiteBansAPI() != null){

            getProxy().getScheduler().schedule(this, new OnLiteBanEvents(), 5L, 0, TimeUnit.SECONDS);

            getLogger().info("LiteBans Plugin Detected!");

        }
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfig() { return this.cm; }

    public External getExternal() { return this.external; }

    public SQLite getSqLite() { return this.sqLite; }
}
