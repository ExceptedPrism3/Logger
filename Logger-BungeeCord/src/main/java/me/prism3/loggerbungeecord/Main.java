package me.prism3.loggerbungeecord;

import me.prism3.loggerbungeecord.api.LiteBansUtil;
import me.prism3.loggerbungeecord.api.PartyAndFriendsUtil;
import me.prism3.loggerbungeecord.commands.Reload;
import me.prism3.loggerbungeecord.discord.Discord;
import me.prism3.loggerbungeecord.discord.DiscordFile;
import me.prism3.loggerbungeecord.events.OnChat;
import me.prism3.loggerbungeecord.events.OnLeave;
import me.prism3.loggerbungeecord.events.OnLogin;
import me.prism3.loggerbungeecord.events.oncommands.OnCommand;
import me.prism3.loggerbungeecord.events.plugindependent.litebans.OnLiteBanEvents;
import me.prism3.loggerbungeecord.events.plugindependent.litebans.OnPartyAndFriends;
import me.prism3.loggerbungeecord.serverside.OnReload;
import me.prism3.loggerbungeecord.serverside.RAM;
import me.prism3.loggerbungeecord.serverside.Start;
import me.prism3.loggerbungeecord.serverside.Stop;
import me.prism3.loggerbungeecord.utils.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

import static me.prism3.loggerbungeecord.utils.Data.*;

public final class Main extends Plugin {

    private static Main instance;

    private ConfigManager cm;

    private Messages messages;

    private DiscordFile discordFile;
    private Discord discord;

    @Override
    public void onEnable() {

        instance = this;

        this.cm = new ConfigManager();
        this.cm.init();

        this.messages = new Messages();
        this.messages.init();

        this.discordFile = new DiscordFile();
        this.discordFile.init();

        this.discord = new Discord();
        this.discord.run();
        
        final FileHandler fileHandler = new FileHandler(getDataFolder());
        fileHandler.deleteFiles();

        this.initializer(new Data());

        this.databaseSetup();

        if (isLogToFiles && isSqlite) {

            this.getLogger().warning("File and SQLite logging are both enabled, this might impact your Server's Performance!");

        }

        this.getProxy().getPluginManager().registerListener(this, new OnChat());
        this.getProxy().getPluginManager().registerListener(this, new OnLogin());
        this.getProxy().getPluginManager().registerListener(this, new OnLeave());
        this.getProxy().getPluginManager().registerListener(this, new OnReload());
        this.getProxy().getPluginManager().registerListener(this, new OnCommand());

        this.getProxy().getScheduler().schedule(this, new RAM(), 200L, ramChecker / 20, TimeUnit.SECONDS);

        this.getProxy().getPluginManager().registerCommand(this, new Reload());

        this.loadPluginDepends();

        new ASCIIArt().art();

        // bStats
        new Metrics(this, 12036);

        // Update Checker
        if (isUpdateChecker) new UpdateChecker().checkUpdates();

        this.getLogger().info(ChatColor.GOLD + "Thanks to everyone's contributions that helped made this project possible!");

        this.getLogger().info("has been Enabled!");

        new Start().run();
    }

    @Override
    public void onDisable() {

        new Stop().run();

//        if (isExternal && this.external.isConnected()) this.external.disconnect();

//        if (isSqlite && this.sqLite.isConnected()) this.sqLite.disconnect();

        this.discord.disconnect();

        this.getLogger().info("has been Disabled!");
    }

    public void initializer(Data data) {

        data.initializeDateFormatter();
        data.initializeStrings();
        data.initializeListOfStrings();
        data.initializeIntegers();
        data.initializeBoolean();
        data.initializePermissionStrings();

    }

    private void databaseSetup() {
//TODO DB Hna
    }

    private void loadPluginDepends() {

        if (LiteBansUtil.getLiteBansAPI() != null) {

            this.getProxy().getScheduler().schedule(this, new OnLiteBanEvents(), 5L, 0, TimeUnit.SECONDS);

            this.getLogger().info("LiteBans Plugin Detected!");

        }

        if (PartyAndFriendsUtil.getPartyAndFriendsAPI() != null) {

            this.getProxy().getPluginManager().registerListener(this, new OnPartyAndFriends());
            this.getLogger().info("PartyAndFriends Plugin Detected!");

        }
    }

    public static Main getInstance() { return instance; }

    public ConfigManager getConfig() { return this.cm; }

    public Messages getMessages() { return this.messages; }

    public DiscordFile getDiscordFile() { return this.discordFile; }

    public Discord getDiscord() { return this.discord; }
}
