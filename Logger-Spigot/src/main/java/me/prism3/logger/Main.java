package me.prism3.logger;

import me.prism3.logger.database.sqlite.global.registration.SQLiteDataRegistration;
import me.prism3.logger.database.sqlite.global.registration.SQLiteRegistration;
import me.prism3.logger.discord.Discord;
import me.prism3.logger.serverside.Start;
import me.prism3.logger.serverside.Stop;
import me.prism3.logger.utils.*;
import me.prism3.logger.utils.manager.ConfigManager;
import me.prism3.logger.utils.manager.DiscordManager;
import me.prism3.logger.utils.manager.MessagesManager;
import me.prism3.logger.utils.updater.PluginUpdater;
import me.prism3.loggercore.database.AbstractDataSource;
import me.prism3.loggercore.database.datasource.MySQL;
import me.prism3.loggercore.database.datasource.SQLite;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static me.prism3.logger.utils.Data.*;

public class Main extends JavaPlugin {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static Main instance;
    private AbstractDataSource database;
    private AbstractDataSource sqLite;
    private SQLiteRegistration sqLiteReg;
    private DiscordManager discordFile;

    @Override
    public void onEnable() {

        instance = this;

        Log.setup(this.getLogger());

        this.initializer(new Data());

        new ConfigManager(this);

        this.discordFile = new DiscordManager(this);

        Discord.getInstance().run();

        if (this.langChecker()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (isLogToFiles && isSqlite)
            Log.warning("FileUpdater and SQLite logging are both enabled, this might impact your Server's Performance!");

        new FileHandler(this.getDataFolder());

        new PluginUpdater(pluginVersion).checkForUpdates();

        this.databaseSetup();

        new ASCIIArt().art();

        // bStats
        new Metrics(this, 12036);

        Log.info(ChatColor.GOLD + "Thanks to everyone's contributions that helped made this project possible!");

        Log.info("Plugin Enabled!");

        new Start().run();
    }

    @Override
    public void onDisable() {

        if (MessagesManager.getInstance() == null || MessagesManager.getInstance().get() == null)
            return;

        new Stop().run();

//        if (isRegistration && this.sqLiteReg.isConnected()) this.sqLiteReg.disconnect();

        // This will stop accepting new tasks, but it will wait for running tasks to complete for a maximum of 5 seconds.
        // If tasks are still running after the 5 seconds, it will cancel them using the shutdownNow() method.
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        if (this.database != null)
            this.disconnectDatabase();

        if (Discord.getInstance() != null)
            Discord.getInstance().disconnect();

        Log.info("Plugin Disabled!");
    }

    public void initializer(Data data) {

        data.initializeDateFormatter();
        data.initializeStrings();
        data.initializeListOfStrings();
        data.initializeIntegers();
        data.initializeLongs();
        data.initializeBooleans();
        data.initializePermissionStrings();
        data.initializeDatabaseCredentials();
        data.initializeOptions();
        data.initializeVersionOptions();
        data.initializeEvents();
        data.initializeCommands();
    }

    private void databaseSetup() {

        try {
            if(isSqlite){
                this.sqLite = new SQLite(Data.options, this.getDataFolder());

            }
            if(databaseCredentials.isEnabled()){
                this.database = new MySQL(Data.databaseCredentials, Data.options);


            }


            if (isRegistration) {

                this.sqLiteReg = new SQLiteRegistration();
                this.sqLiteReg.connect();
                final SQLiteDataRegistration sqLiteDataRegistration = new SQLiteDataRegistration();
                if (this.sqLiteReg.isConnected())
                    sqLiteDataRegistration.createTable();
            }

        } catch (final Exception e) { Log.severe(e.getMessage()); }
    }

    public void disconnectDatabase() { this.database.disconnect(); }

    private boolean langChecker() {

        MessagesManager.getInstance().init();

        // Check if the selected Language is Valid | Exists
        return MessagesManager.getInstance().get() == null;
    }

    public static Main getInstance() { return instance; }

    public SQLiteRegistration getSqLiteReg() { return this.sqLiteReg; }

    public Discord getDiscord() { return Discord.getInstance(); }

    public DiscordManager getDiscordFile() { return this.discordFile; }

    public MessagesManager getMessagesFile() { return MessagesManager.getInstance(); }

    public AbstractDataSource getDatabase() { return this.database; }

    public AbstractDataSource getSqLite() { return this.sqLite; }

    public ExecutorService getExecutor() { return this.executor; }
}
