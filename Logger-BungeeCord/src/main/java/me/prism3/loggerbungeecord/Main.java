package me.prism3.loggerbungeecord;

import com.carpour.loggercore.database.DataSourceInterface;
import me.prism3.loggerbungeecord.commands.Reload;
import me.prism3.loggerbungeecord.discord.Discord;
import me.prism3.loggerbungeecord.discord.DiscordFile;
import me.prism3.loggerbungeecord.serverside.Start;
import me.prism3.loggerbungeecord.serverside.Stop;
import me.prism3.loggerbungeecord.utils.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

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

        if (isLogToFiles && isSqlite)
            this.getLogger().warning("File and SQLite logging are both enabled, this might impact your Server's Performance!");

        this.getProxy().getPluginManager().registerCommand(this, new Reload());

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
        data.eventInitializer();

    }

    private void databaseSetup() {
//TODO DB Hna
    }

    public static Main getInstance() { return instance; }

    public ConfigManager getConfig() { return this.cm; }

    public Messages getMessages() { return this.messages; }

    public DiscordFile getDiscordFile() { return this.discordFile; }

    public Discord getDiscord() { return this.discord; }

    public DataSourceInterface getDatabase() { return this.database; }

    public DataSourceInterface getSqLite() { return this.sqLite; }
}
