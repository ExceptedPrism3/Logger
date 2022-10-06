package me.prism3.loggerbungeecord;

import me.prism3.loggercore.database.DataSourceInterface;
import me.prism3.loggerbungeecord.discord.Discord;
import me.prism3.loggerbungeecord.discord.DiscordFile;
import me.prism3.loggerbungeecord.serverside.Start;
import me.prism3.loggerbungeecord.serverside.Stop;
import me.prism3.loggerbungeecord.utils.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;

import static me.prism3.loggerbungeecord.utils.Data.*;

public final class Main extends Plugin {

    private static Main instance;

    private ConfigManager cm;

    private Messages messages;

    private DiscordFile discordFile;
    private Discord discord;

    private DataSourceInterface database;
    private DataSourceInterface sqLite;

    @Override
    public void onEnable() {

        instance = this;

        Log.setup(this.getLogger());

//        this.cm = new ConfigManager();
//        this.cm.init();

        loadConfiguration();

        this.messages = new Messages();
        this.messages.init();

        this.discordFile = new DiscordFile();
        this.discordFile.init();

        this.discord = new Discord();
        this.discord.run();
        
        final FileHandler fileHandler = new FileHandler(this.getDataFolder());
        fileHandler.deleteFiles(this.getDataFolder());

        this.initializer(new Data());

//        this.databaseSetup();//TODO DB

        if (isLogToFiles && isSqlite)
            Log.warning("File and SQLite logging are both enabled, this might impact your Server's Performance!");

        new ASCIIArt().art();

        // bStats
        new Metrics(this, 12036);

        // Update Checker
        if (isUpdateChecker) new UpdateChecker().checkUpdates();

        Log.info(ChatColor.GOLD + "Thanks to everyone's contributions that helped made this project possible!");

        Log.info("has been Enabled!");

        new Start().run();
    }

    private ConfigLoader config;

    private void loadConfiguration() {
        try {
            config = new me.prism3.loggerbungeecord.utils.ConfigLoader(new File(Main.getInstance().getDataFolder(), "config - Bungee.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

        new Stop().run();

//        if (isExternal && this.external.isConnected()) this.external.disconnect();

//        if (isSqlite && this.sqLite.isConnected()) this.sqLite.disconnect();

        this.discord.disconnect();

        Log.info("has been Disabled!");
    }

    public void initializer(Data data) {

        data.initializeDateFormatter();
        data.initializeStrings();
        data.initializeListOfStrings();
        data.initializeIntegers();
        data.initializeBoolean();
        data.initializePermissionStrings();
        data.eventInitializer();
    }

    public static Main getInstance() { return instance; }

    public ConfigManager getConfig() { return this.cm; }

    public Messages getMessages() { return this.messages; }

    public DiscordFile getDiscordFile() { return this.discordFile; }

    public Discord getDiscord() { return this.discord; }

    public DataSourceInterface getDatabase() { return this.database; }

    public DataSourceInterface getSqLite() { return this.sqLite; }
}
