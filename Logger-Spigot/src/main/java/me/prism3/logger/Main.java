package me.prism3.logger;

import me.prism3.logger.commands.CommandManager;
import me.prism3.logger.commands.getting.Chat;
import me.prism3.logger.database.sqlite.global.registration.SQLiteDataRegistration;
import me.prism3.logger.database.sqlite.global.registration.SQLiteRegistration;
import me.prism3.logger.discord.Discord;
import me.prism3.logger.serverside.Start;
import me.prism3.logger.serverside.Stop;
import me.prism3.logger.utils.*;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.logger.utils.manager.ConfigManager;
import me.prism3.logger.utils.manager.DiscordManager;
import me.prism3.loggercore.database.DataSourceInterface;
import me.prism3.loggercore.database.datasource.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import static me.prism3.logger.utils.Data.*;

public class Main extends JavaPlugin {

    private static Main instance;

    private Messages messages;

    private DataSourceInterface database;

    private DataSourceInterface sqLite;
    private SQLiteRegistration sqLiteReg;

    private Discord discord;
    private DiscordManager discordFile;

    private final NmsVersions version = NmsVersions.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]);

    @Override
    public void onEnable() {

        instance = this;

        Log.setup(this.getLogger());

        this.initializer(new Data());

        new ConfigManager(this);

        this.discordFile = new DiscordManager(this);

        this.discord = new Discord();
        this.discord.run();

        if (!this.langChecker()) return;

        if (isLogToFiles && isSqlite)
            Log.warning("File and SQLite logging are both enabled, this might impact your Server's Performance!");

        final FileHandler fileHandler = new FileHandler(this.getDataFolder());
        fileHandler.deleteFiles();

        this.loadPluginDepends();

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

        if (!this.messages.getIsValid()) return;

        new Stop().run();

        if (isRegistration && this.sqLiteReg.isConnected()) this.sqLiteReg.disconnect();

        this.disconnectDatabase();
        this.discord.disconnect();

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
        data.eventInitializer();
        data.commandsInitializer();
    }

    private void databaseSetup() {

        try {
            this.database = new Database(databaseCredentials, Data.options);
            this.sqLite = null;

            if (isRegistration) {

                this.sqLiteReg = new SQLiteRegistration();
                this.sqLiteReg.connect();
                final SQLiteDataRegistration sqLiteDataRegistration = new SQLiteDataRegistration();
                if (this.sqLiteReg.isConnected()) sqLiteDataRegistration.createTable();
            }

        } catch (final Exception e) { Log.severe(e.getMessage()); }
    }

    private void disconnectDatabase() {
        this.database.disconnect();
    }

    private void loadPluginDepends() {//TODO 7ta tgad m3k had joj m3rftch ach ndir lihom
        Data.options.setDataDelete(this.getConfig().getInt("Database.Data-Deletion"));
        Data.options.setPlayerIPEnabled(this.getConfig().getBoolean("Player-Join.Player-IP"));
    }

    private boolean langChecker() {

        this.messages = new Messages();

        if (!this.messages.getIsValid()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    public static Main getInstance() { return instance; }

    public SQLiteRegistration getSqLiteReg() { return this.sqLiteReg; }

    public NmsVersions getVersion() { return this.version; }

    public Discord getDiscord() { return this.discord; }

    public FileConfiguration getDiscordFile() { return this.discordFile.get(); }

    public Messages getMessages() { return this.messages; }

    public DataSourceInterface getDatabase() { return this.database; }

    public DataSourceInterface getSqLite() { return this.sqLite; }
}