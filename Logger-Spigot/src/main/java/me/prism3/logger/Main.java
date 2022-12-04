package me.prism3.logger;

import me.prism3.logger.api.*;
import me.prism3.logger.commands.CommandManager;
import me.prism3.logger.commands.subcommands.PlayerInventory;
import me.prism3.logger.database.external.External;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.external.ExternalUpdater;
import me.prism3.logger.database.sqlite.registration.SQLiteDataRegistration;
import me.prism3.logger.database.sqlite.registration.SQLiteRegistration;
import me.prism3.logger.discord.Discord;
import me.prism3.logger.discord.DiscordFile;
import me.prism3.logger.events.misc.OnPrimedTNT;
import me.prism3.logger.events.oncommands.OnCommand;
import me.prism3.logger.events.oninventories.OnChestInteraction;
import me.prism3.logger.events.oninventories.OnCraft;
import me.prism3.logger.events.oninventories.OnFurnace;
import me.prism3.logger.events.OnPlayerDeath;
import me.prism3.logger.events.onversioncompatibility.OnWoodStripping;
import me.prism3.logger.events.plugindependent.*;
import me.prism3.logger.database.sqlite.global.SQLite;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import de.jeff_media.updatechecker.UpdateChecker;
import me.prism3.logger.events.*;
import me.prism3.logger.serverside.*;
import me.prism3.logger.utils.*;
import me.prism3.logger.utils.enums.NmsVersions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import static me.prism3.logger.utils.Data.*;

public class Main extends JavaPlugin {

    private static Main instance;

    private Messages messages;

    private External external;

    private SQLite sqLite;
    private SQLiteRegistration sqLiteReg;

    private Discord discord;
    private DiscordFile discordFile;

    private final NmsVersions version = NmsVersions.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3]);

    @Override
    public void onEnable() {

        instance = this;

        this.saveDefaultConfig();

        this.initializer(new Data());

        if (!this.langChecker()) return;

        new FileUpdater(this.getDataFolder());

        this.discordFile = new DiscordFile();
        this.discordFile.setup();
        this.discordFile.get().options().copyDefaults(true);

        this.discord = new Discord();
        this.discord.run();

        if (isLogToFiles && isSqlite)
            this.getLogger().warning("File and SQLite logging are both enabled, this might impact your Server's Performance!");

        final FileHandler fileHandler = new FileHandler(this.getDataFolder());
        fileHandler.deleteFiles();

        this.databaseSetup();

        this.eventsInitializer();

        this.commandsInitializer();

        new ASCIIArt().art();

        // bStats
        new Metrics(this, 12036);

        // Update Checker
        /*if (Data.isUpdateChecker) {

            UpdateChecker.init(this, Data.resource_ID)
                    .checkEveryXHours(2)
                    .setChangelogLink(Data.resource_ID)
                    .setNotifyByPermissionOnJoin(Data.loggerUpdate)
                    .checkNow();
        }*/

        this.loadPluginDepends();

        this.getLogger().info(ChatColor.GOLD + "Thanks to everyone's contributions that helped made this project possible!");

        this.getLogger().info("Plugin Enabled!");

        new Start().run();
    }

    @Override
    public void onDisable() {

        if (!this.messages.getIsValid()) return;

        new Stop().run();

        if (isExternal && this.external.isConnected()) this.external.disconnect();

        if (isSqlite && this.sqLite.isConnected()) this.sqLite.disconnect();

        if (isRegistration && this.sqLiteReg.isConnected()) this.sqLiteReg.disconnect();

        this.discord.disconnect();

        this.getLogger().info("Plugin Disabled!");

    }

    public void initializer(Data data) {

        data.initializeDateFormatter();
        data.initializeStrings();
        data.initializeListOfStrings();
        data.initializeIntegers();
        data.initializeLongs();
        data.initializeBooleans();
        data.initializePermissionStrings();

    }

    private void eventsInitializer() {

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
//        this.getServer().getPluginManager().registerEvents(new OnSpawnEgg(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInventory(), this);
        this.getServer().getPluginManager().registerEvents(new OnCommandBlock(), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityDeath(), this);

        this.getServer().getPluginManager().registerEvents(new OnFurnace(), this);
        this.getServer().getPluginManager().registerEvents(new OnCraft(), this);
        this.getServer().getPluginManager().registerEvents(new OnChestInteraction(), this);

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPS(), 300L, Data.ramTpsChecker);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new RAM(), 300L, Data.ramTpsChecker);

        // Version Exceptions
        if (this.getVersion().isAtLeast(NmsVersions.v1_13_R1)) {

            this.getServer().getPluginManager().registerEvents(new OnWoodStripping(), this);

        }
    }

    private void commandsInitializer() {

        this.getCommand("logger").setExecutor(new CommandManager());
//        this.getCommand("loggerget").setExecutor(new Chat());

    }

    private void databaseSetup() {

        if (isExternal) {

            this.external = new External();
            this.external.connect();
            final ExternalData externalData = new ExternalData();
            if (this.external.isConnected()) {
                ExternalUpdater.updater();
                externalData.createTable();
                externalData.emptyTable();
            }
        }

        if (isSqlite) {

            this.sqLite = new SQLite();
            this.sqLite.connect();
            final SQLiteData sqLiteData = new SQLiteData();
            if (this.sqLite.isConnected()) {
                sqLiteData.createTable();
                sqLiteData.emptyTable();
            }
        }

        if (isRegistration) {

            this.sqLiteReg = new SQLiteRegistration();
            this.sqLiteReg.connect();
            final SQLiteDataRegistration sqLiteDataRegistration = new SQLiteDataRegistration();
            if (this.sqLiteReg.isConnected()) sqLiteDataRegistration.createTable();
        }
    }

    private void loadPluginDepends() {

        if (EssentialsUtil.getEssentialsAPI() != null) {

            this.getServer().getPluginManager().registerEvents(new OnAFK(), this);

            this.getLogger().info("Essentials Plugin Detected!");

        }

        if (AuthMeUtil.getAuthMeAPI() != null) {

            this.getServer().getPluginManager().registerEvents(new OnAuthMePassword(), this);

            this.getLogger().info("AuthMe Plugin Detected!");

        }

        if (VaultUtil.getVaultAPI()) {

            if (VaultUtil.getVault() != null) {

                final OnVault vault = new OnVault();
                this.getServer().getPluginManager().registerEvents(vault, this);
                this.getServer().getScheduler().scheduleSyncRepeatingTask(this, vault, 10L, Data.vaultChecker);
            }
            this.getLogger().info("Vault Plugin Detected!");
        }

        if (LiteBansUtil.getLiteBansAPI() != null) {

            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new OnLiteBanEvents(), 10L);

            this.getLogger().info("LiteBans Plugin Detected!");
        }

        if (AdvancedBanUtil.getAdvancedBanAPI() != null) {

            this.getServer().getPluginManager().registerEvents(new OnAdvancedBan(), this);

            this.getLogger().info("AdvancedBan Plugin Detected!");
        }

        if (PlaceHolderAPIUtil.getPlaceHolderAPI() != null) {

            this.getLogger().info("PlaceHolderAPI Plugin Detected!");
        }

        if (GeyserUtil.getGeyserAPI() != null && FloodGateUtil.getFloodGateAPI()) {

            this.getLogger().info("Geyser & FloodGate Plugins Detected!");
            this.getLogger().warning("Geyser & FloodGate are not fully supported! If any errors occurs, contact the authors.");
        }
    }

    private boolean langChecker() {

        this.messages = new Messages();

        if (!this.messages.getIsValid()) {

            this.getServer().getPluginManager().disablePlugin(this);
            return false;
        } return true;
    }

    public static Main getInstance() { return instance; }

    public External getExternal() { return this.external; }

    public SQLite getSqLite() { return this.sqLite; }

    public SQLiteRegistration getSqLiteReg() { return this.sqLiteReg; }

    public NmsVersions getVersion() { return this.version; }

    public Discord getDiscord() { return this.discord; }

    public DiscordFile getDiscordFile() { return this.discordFile; }

    public Messages getMessages() { return this.messages; }
}