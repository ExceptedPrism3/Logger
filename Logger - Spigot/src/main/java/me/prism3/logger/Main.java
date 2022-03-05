package me.prism3.logger;

import me.prism3.logger.API.AuthMeUtil;
import me.prism3.logger.API.EssentialsUtil;
import me.prism3.logger.API.VaultUtil;
import me.prism3.logger.Commands.OnLogger;
import me.prism3.logger.Database.External.External;
import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Discord.DiscordFile;
import me.prism3.logger.Events.OnCommands.OnCommand;
import me.prism3.logger.Events.OnInventories.OnCraft;
import me.prism3.logger.Events.OnInventories.OnFurnace;
import me.prism3.logger.Events.PluginDependent.OnAFK;
import me.prism3.logger.Events.PluginDependent.OnAuthMePassword;
import me.prism3.logger.Events.PluginDependent.OnVault;
import me.prism3.logger.Database.SQLite.SQLite;
import me.prism3.logger.Database.SQLite.SQLiteData;
import de.jeff_media.updatechecker.UpdateChecker;
import me.prism3.logger.Events.*;
import me.prism3.logger.ServerSide.*;
import me.prism3.logger.Utils.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {

    private static Main instance;

    private External external;

    private SQLite sqLite;

    private Discord discord;

    @Override
    public void onEnable() {

        instance = this;

        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults();

        this.initializer(new Data());

        Messages.Setup();
        Messages.get().options().copyDefaults(true);

        DiscordFile.Setup();
        DiscordFile.get().options().copyDefaults(true);

        this.discord = new Discord();
        this.discord.run();

        this.databaseSetup();

        if (Data.isLogToFiles && Data.isSqlite){

            this.getLogger().warning("Logging to Files and SQLite are both enabled, this might impact your Server's Performance!");

        }

        FileHandler fileHandler = new FileHandler(this.getDataFolder());
        fileHandler.deleteFiles();

        getServer().getPluginManager().registerEvents(new OnPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new OnCommand(), this);
        getServer().getPluginManager().registerEvents(new Console(), this);
        getServer().getPluginManager().registerEvents(new OnSign(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerKick(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLevel(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new PortalCreation(), this);
        getServer().getPluginManager().registerEvents(new OnBucketFill(), this);
        getServer().getPluginManager().registerEvents(new OnBucketEmpty(), this);
        getServer().getPluginManager().registerEvents(new OnAnvil(), this);
        getServer().getPluginManager().registerEvents(new OnItemPickup(), this);
        getServer().getPluginManager().registerEvents(new OnItemDrop(), this);
        getServer().getPluginManager().registerEvents(new OnEnchant(), this);
        getServer().getPluginManager().registerEvents(new OnBook(), this);
        getServer().getPluginManager().registerEvents(new RCON(), this);
        getServer().getPluginManager().registerEvents(new OnGameMode(), this);

        getServer().getPluginManager().registerEvents(new OnFurnace(), this);
        getServer().getPluginManager().registerEvents(new OnCraft(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 300L, Data.ramTpsChecker);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new RAM(), 300L, Data.ramTpsChecker);

        Objects.requireNonNull(getCommand("logger")).setExecutor(new OnLogger());

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

        new Stop().run();

        if (Data.isExternal && this.external.isConnected()) this.external.disconnect();

        if (Data.isSqlite && this.sqLite.isConnected()) this.sqLite.disconnect();

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

        if (Data.isExternal) {

            this.external = new External();
            this.external.connect();
            ExternalData externalData = new ExternalData(this);
            if (this.external.isConnected()) {
                externalData.createTable();
                externalData.emptyTable();
            }
        }

        if (Data.isSqlite) {

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

        if (EssentialsUtil.getEssentialsAPI() != null){

            this.getServer().getPluginManager().registerEvents(new OnAFK(), this);

            this.getLogger().info("Essentials Plugin Detected!");

        }

        if (AuthMeUtil.getAuthMeAPI() != null){

            this.getServer().getPluginManager().registerEvents(new OnAuthMePassword(), this);

            this.getLogger().info("AuthMe Plugin Detected!");

        }

        if (VaultUtil.getVaultAPI()){

            if (VaultUtil.getVault().isEnabled()) {

                OnVault vault = new OnVault();
                this.getServer().getPluginManager().registerEvents(vault, this);
                this.getServer().getScheduler().scheduleSyncRepeatingTask(this, vault, 10L, Data.vaultChecker);
            }

            this.getLogger().info("Vault Plugin Detected!");

        }
    }

    public static Main getInstance() { return instance; }

    public External getExternal() { return this.external; }

    public SQLite getSqLite() { return this.sqLite; }
}