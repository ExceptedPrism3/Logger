package me.prism3.loggerbungeecord;

import me.prism3.loggerbungeecord.API.LiteBansUtil;
import me.prism3.loggerbungeecord.Events.OnChat;
import me.prism3.loggerbungeecord.Events.OnCommands.OnCommand;
import me.prism3.loggerbungeecord.Events.OnLeave;
import me.prism3.loggerbungeecord.Events.OnLogin;
import me.prism3.loggerbungeecord.Events.PluginDependent.LiteBans.OnLiteBanEvents;
import me.prism3.loggerbungeecord.ServerSide.RAM;
import me.prism3.loggerbungeecord.Database.SQLite.SQLite;
import me.prism3.loggerbungeecord.Database.SQLite.SQLiteData;
import me.prism3.loggerbungeecord.Commands.Reload;
import me.prism3.loggerbungeecord.Database.External.External;
import me.prism3.loggerbungeecord.Database.External.ExternalData;
import me.prism3.loggerbungeecord.Discord.Discord;
import me.prism3.loggerbungeecord.Discord.DiscordFile;
import me.prism3.loggerbungeecord.ServerSide.OnReload;
import me.prism3.loggerbungeecord.ServerSide.Start;
import me.prism3.loggerbungeecord.ServerSide.Stop;
import me.prism3.loggerbungeecord.Utils.*;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

import static me.prism3.loggerbungeecord.Utils.Data.*;

public final class Main extends Plugin {

    private static Main instance;

    private ConfigManager cm;

    private External external;

    private SQLite sqLite;

    @Override
    public void onEnable() {

        instance = this;

        this.cm = new ConfigManager();
        this.cm.init();

        new Messages().init();

        new DiscordFile().init();

        new Discord().run();
        
        FileHandler fileHandler = new FileHandler(getDataFolder());
        fileHandler.deleteFiles();

        this.initializer(new Data());

        this.databaseSetup();

        if (isLogToFiles && isSqlite){

            this.getLogger().warning("Logging to Files and SQLite are both enabled, this might impact your Server's Performance!");

        }

        this.getProxy().getPluginManager().registerListener(this, new OnChat());
        this.getProxy().getPluginManager().registerListener(this, new OnLogin());
        this.getProxy().getPluginManager().registerListener(this, new OnLeave());
        this.getProxy().getPluginManager().registerListener(this, new OnReload());
        this.getProxy().getPluginManager().registerListener(this, new OnCommand());

        this.getProxy().getScheduler().schedule(this, new RAM(), 200L, ramChecker / 20, TimeUnit.SECONDS);

        this.getProxy().getPluginManager().registerCommand(this, new Reload());

        this.loadPluginDepends();

        new ASCIIArt().Art();

        // bStats
        new Metrics(this, 12036);

        // Update Checker
        if (isUpdateChecker) new UpdateChecker().checkUpdates();

        getLogger().info("has been Enabled!");

        new Start().run();
    }

    @Override
    public void onDisable() {

        new Stop().run();

        if (isExternal && this.external.isConnected()) this.external.disconnect();

        if (isSqlite && this.sqLite.isConnected()) this.sqLite.disconnect();

        new Discord().disconnect();

        this.getLogger().info("has been Disabled!");
    }

    private void initializer(Data data){

        data.initializeDateFormatter();
        data.initializeStrings();
        data.initializeListOfStrings();
        data.initializeIntegers();
        data.initializeBoolean();
        data.initializePermissionStrings();

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

            this.getProxy().getScheduler().schedule(this, new OnLiteBanEvents(), 5L, 0, TimeUnit.SECONDS);

            this.getLogger().info("LiteBans Plugin Detected!");

        }
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfig() { return this.cm; }

    public External getExternal() { return this.external; }

    public SQLite getSqLite() { return this.sqLite; }
}
