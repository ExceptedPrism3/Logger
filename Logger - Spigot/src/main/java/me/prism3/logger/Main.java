package me.prism3.logger;

import me.prism3.logger.API.AuthMeUtil;
import me.prism3.logger.API.EssentialsUtil;
import me.prism3.logger.API.VaultUtil;
import me.prism3.logger.Commands.CommandManager;
import me.prism3.logger.Commands.Dump;
import me.prism3.logger.Commands.Getting.Chat;
import me.prism3.logger.Commands.SubCommands.PlayerInventoryCommand;
import me.prism3.logger.Database.External.External;
import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Database.External.ExternalUpdater;
import me.prism3.logger.Database.SQLite.Registration.SQLiteDataRegistration;
import me.prism3.logger.Database.SQLite.Registration.SQLiteRegistration;
import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Discord.DiscordFile;
import me.prism3.logger.Events.Misc.OnSpawnEgg;
import me.prism3.logger.Events.Misc.OnPrimedTNT;
import me.prism3.logger.Events.OnCommands.OnCommand;
import me.prism3.logger.Events.OnInventories.OnCraft;
import me.prism3.logger.Events.OnInventories.OnFurnace;
import me.prism3.logger.Events.OnPlayerDeath;
import me.prism3.logger.Events.PluginDependent.OnAFK;
import me.prism3.logger.Events.PluginDependent.OnAuthMePassword;
import me.prism3.logger.Events.PluginDependent.OnVault;
import me.prism3.logger.Database.SQLite.Global.SQLite;
import me.prism3.logger.Database.SQLite.Global.SQLiteData;
import de.jeff_media.updatechecker.UpdateChecker;
import me.prism3.logger.Events.*;
import me.prism3.logger.ServerSide.*;
import me.prism3.logger.Utils.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import static me.prism3.logger.Utils.Data.*;

public class Main extends JavaPlugin {

    private static Main instance;

    public ConfigChecker cF;
    private Messages mS;

    private External external;

    private SQLite sqLite;
    private SQLiteRegistration sqLiteReg;

    private Discord discord;

    @Override
    public void onEnable() {

        instance = this;

        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults();

        this.initializer(new Data());

        if (!this.configChecker()) return;

        if (!this.langChecker()) return;

        DiscordFile.Setup();
        DiscordFile.get().options().copyDefaults(true);

        this.discord = new Discord();
        this.discord.run();

        if (Data.isLogToFiles && isSqlite){

            this.getLogger().warning("Logging to Files and SQLite are both enabled, this might impact your Server's Performance!");

        }

        FileHandler fileHandler = new FileHandler(this.getDataFolder());
        fileHandler.deleteFiles();
        this.databaseSetup();
        

        this.getServer().getPluginManager().registerEvents(new OnPlayerChat(), this);
        this.getServer().getPluginManager().registerEvents(new OnCommand(), this);
        this.getServer().getPluginManager().registerEvents(new Console(), this);
        this.getServer().getPluginManager().registerEvents(new OnSign(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerKick(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLevel(), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        this.getServer().getPluginManager().registerEvents(new PortalCreation(), this);
        this.getServer().getPluginManager().registerEvents(new OnBucketFill(), this);
        this.getServer().getPluginManager().registerEvents(new OnBucketEmpty(), this);
        this.getServer().getPluginManager().registerEvents(new OnAnvil(), this);
        this.getServer().getPluginManager().registerEvents(new OnItemPickup(), this);
        this.getServer().getPluginManager().registerEvents(new OnItemDrop(), this);
        this.getServer().getPluginManager().registerEvents(new OnEnchant(), this);
        this.getServer().getPluginManager().registerEvents(new OnBook(), this);
        this.getServer().getPluginManager().registerEvents(new RCON(), this);
        this.getServer().getPluginManager().registerEvents(new OnGameMode(), this);
        this.getServer().getPluginManager().registerEvents(new OnPrimedTNT(), this);
        this.getServer().getPluginManager().registerEvents(new OnSpawnEgg(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInventoryCommand(), this);

        this.getServer().getPluginManager().registerEvents(new OnFurnace(), this);
        this.getServer().getPluginManager().registerEvents(new OnCraft(), this);

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 300L, Data.ramTpsChecker);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new RAM(), 300L, Data.ramTpsChecker);

        this.getCommand("logger").setExecutor(new CommandManager());
        this.getCommand("loggerd").setExecutor(new Dump());
        this.getCommand("loggerget").setExecutor(new Chat());

        new ASCIIArt().Art();

        // bStats
        new Metrics(this, 12036);

        // Update Checker
        if (Data.isUpdateChecker) {

            UpdateChecker.init(this, Data.resource_ID)
                    .checkEveryXHours(2)
                    .setChangelogLink(Data.resource_ID)
                    .setNotifyByPermissionOnJoin(Data.loggerUpdate)
                    .checkNow();

        }

        this.loadPluginDepends();

        this.getLogger().info(ChatColor.GOLD + "Thank you " + ChatColor.GREEN + ChatColor.BOLD + "thelooter" + ChatColor.GOLD + " for the Contribution!");

        this.getLogger().info("Plugin Enabled!");

        new Start().run();
    }

    @Override
    public void onDisable() {

        if (this.cF.getIsGood() || this.mS.getIsValid()) return;

        new Stop().run();

        if (isExternal && this.external.isConnected()) this.external.disconnect();

        if (isSqlite && this.sqLite.isConnected()) this.sqLite.disconnect();

        if (isRegistration && this.sqLiteReg.isConnected()) this.sqLiteReg.disconnect();

        this.discord.disconnect();

        this.getLogger().info("Plugin Disabled!");

    }

    private void initializer(Data data){

        data.initializeDateFormatter();
        data.initializeStrings();
        data.initializeListOfStrings();
        data.initializeIntegers();
        data.initializeLongs();
        data.initializeBooleans();
        data.initializePermissionStrings();

    }

    private void databaseSetup(){

        if (isExternal) {

            this.external = new External();
            this.external.connect();   
            ExternalData externalData = new ExternalData(this);
            if (this.external.isConnected()) {
                new ExternalUpdater();
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

        if (isRegistration){

            this.sqLiteReg = new SQLiteRegistration();
            this.sqLiteReg.connect();
            SQLiteDataRegistration sqLiteDataRegistration = new SQLiteDataRegistration(this);
            if (this.sqLiteReg.isConnected()) sqLiteDataRegistration.createTable();
        }
    }

    private void loadPluginDepends(){

        if (EssentialsUtil.getEssentialsAPI() != null){

            this.getServer().getPluginManager().registerEvents(new OnAFK(), this);

            this.getLogger().info("Essentials Plugin Detected!");

        }

        if (AuthMeUtil.getAuthMeAPI() != null){

            this.getServer().getPluginManager().registerEvents(new OnAuthMePassword(), this);

            this.getLogger().info("AuthMe Plugin Detected!");

        }

        if (VaultUtil.getVaultAPI()){

            if (VaultUtil.getVault() != null) {

                OnVault vault = new OnVault();
                this.getServer().getPluginManager().registerEvents(vault, this);
                this.getServer().getScheduler().scheduleSyncRepeatingTask(this, vault, 10L, Data.vaultChecker);
            }
            this.getLogger().info("Vault Plugin Detected!");
        }
    }

    private boolean langChecker(){

        this.mS = new Messages();

        if (!this.mS.getIsValid()) {

            this.getServer().getPluginManager().disablePlugin(this);
            return false;
        } return true;
    }

    private boolean configChecker(){

        this.cF = new ConfigChecker();

        if (!this.cF.getIsGood()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return false;
        } return true;
    }

    public static Main getInstance() { return instance; }

    public External getExternal() { return this.external; }

    public SQLite getSqLite() { return this.sqLite; }

    public SQLiteRegistration getSqLiteReg() { return this.sqLiteReg; }

}