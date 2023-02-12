package me.prism3.logger.utils;

import me.prism3.logger.Main;
import me.prism3.logger.commands.CommandManager;
import me.prism3.logger.commands.subcommands.PlayerInventory;
import me.prism3.logger.events.*;
import me.prism3.logger.events.commands.OnCommand;
import me.prism3.logger.events.inventories.OnChestInteraction;
import me.prism3.logger.events.inventories.OnCraft;
import me.prism3.logger.events.inventories.OnFurnace;
import me.prism3.logger.events.misc.*;
import me.prism3.logger.events.spy.OnAnvilSpy;
import me.prism3.logger.events.spy.OnBookSpy;
import me.prism3.logger.events.spy.OnCommandSpy;
import me.prism3.logger.events.spy.OnSignSpy;
import me.prism3.logger.events.versioncompatibility.OnTotemUse;
import me.prism3.logger.events.versioncompatibility.OnWoodStripping;
import me.prism3.logger.hooks.*;
import me.prism3.logger.serverside.*;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.loggercore.database.data.Options;
import me.prism3.loggercore.database.data.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    private final Main main = Main.getInstance();

    // Date Format
    public static DateTimeFormatter dateTimeFormatter;

    // Version Related
    public static String pluginVersion;
    public static String configVersion;
    public static NmsVersions version;
    public static boolean isNewVersion;
    public static Material type;

    // String
    public static String serverName;
    public static String gameModeConf;
    public static String langPath;
    public static String fileType;
    public static String selectedLang;
    public static String discordSupportServer;
    public static String pluginPrefix;
    public static String resourceAPIChecker;
    public static String resourceLink;

    // List<String>
    public static List<String> commandsToBlock;
    public static List<String> commandsToLog;
    public static List<String> consoleCommandsToBlock;
    public static List<String> worldGuardRegions;

    // Integer
    public static int resource_ID;
    public static int ramTpsChecker;
    public static int vaultChecker;
    public static int abovePlayerLevel;
    public static int ramPercent;
    public static int tpsMedium;
    public static int tpsCritical;
    public static int playerCountNumber;
    public static int playerCountChecker;
    public static int sqliteDataDel;
    public static int allowedBackups;

    // Long
    public static long fileDeletion;

    // Boolean
    public static boolean isLogToFiles;
    public static boolean isUpdateChecker;
    public static boolean isExternal;
    public static boolean isSqlite;
    public static boolean isStaffEnabled;
    public static boolean isWhitelisted;
    public static boolean isBlacklisted;
    public static boolean isCommandsToBlock;
    public static boolean isCommandsToLog;
    public static boolean isPlayerIP;
    public static boolean isConsoleCommands;
    public static boolean isRegistration;
    public static boolean isPlayerDeathBackup;
    public static boolean isViaVersion;

    // Permission String
    public static String loggerExempt;
    public static String loggerExemptDiscord;
    public static String loggerStaff;
    public static String loggerStaffLog;
    public static String loggerSpyBypass;
    public static String loggerSpy;
    public static String loggerReload;


    public static final Options options = new Options();
    public static Settings databaseCredentials;

    public void initializeDateFormatter() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(this.main.getConfig().getString("Time-Formatter"));
    }

    public void initializeStrings() {

        serverName = this.main.getConfig().getString("Server-Name");
        gameModeConf = this.main.getConfig().getString("Game-Mode");
        langPath = "messages";
        fileType = ".yml";
        selectedLang = this.main.getConfig().getString("Language");
        discordSupportServer = "https://discord.gg/MfR5mcpVfX";
        pluginPrefix = this.main.getConfig().getString("Plugin-Prefix");
        resourceAPIChecker = "https://api.spigotmc.org/legacy/update.php?resource=94236/";
        resourceLink = "https://www.spigotmc.org/resources/logger-1-7-1-19.94236/";
    }

    public void initializeDatabaseCredentials() {
        Data.databaseCredentials = new Settings(
                this.main.getConfig().getString("Database.Type"),
                this.main.getConfig().getString("Database.Host"),
                this.main.getConfig().getString("Database.Username"),
                this.main.getConfig().getString("Database.Password"),
                this.main.getConfig().getString("Database.Database"),
                this.main.getConfig().getInt("Database.Port"),
                this.main.getConfig().getBoolean("Database.Enable")

        );
    }

    public void initializeOptions() {

        final Map<String, Object> optionS = new HashMap<>(30);

        optionS.putAll(this.main.getConfig().getConfigurationSection("Log-Player").getValues(false));
        optionS.putAll(this.main.getConfig().getConfigurationSection("Log-Server").getValues(false));
        optionS.putAll(this.main.getConfig().getConfigurationSection("Log-Extras").getValues(false));
        options.setDataDelete(this.main.getConfig().getInt("Database.Data-Deletion"));
        options.setPlayerIPEnabled(this.main.getConfig().getBoolean("Player-Join.Player-IP"));
//        options.putAll(main.getConfig().getConfigurationSection("Log-Version-Exceptions").getValues(false));
        options.setEnabledLogs(optionS);
    }

    public void initializeListOfStrings() {

        commandsToBlock = this.main.getConfig().getStringList("Player-Commands.Commands-to-Block");
        commandsToLog = this.main.getConfig().getStringList("Player-Commands.Commands-to-Log");
        consoleCommandsToBlock = this.main.getConfig().getStringList("Console-Commands.Commands-to-Block");
        worldGuardRegions = this.main.getConfig().getStringList("WorldGuard-Regions");
    }

    public void initializeIntegers() {

        resource_ID = 94236;
        ramTpsChecker = this.main.getConfig().getInt("Checkers.RAM-TPS");
        vaultChecker = this.main.getConfig().getInt("Checkers.Vault");
        abovePlayerLevel = this.main.getConfig().getInt("Player-Level");
        ramPercent = this.main.getConfig().getInt("RAM");
        tpsMedium = this.main.getConfig().getInt("TPS.Medium");
        tpsCritical = this.main.getConfig().getInt("TPS.Critical");
        sqliteDataDel = this.main.getConfig().getInt("SQLite.Data-Deletion");
        allowedBackups = this.main.getConfig().getInt("Player-Death-Backup.Max-Backup");
        playerCountNumber = this.main.getConfig().getInt("Player-Count");
        playerCountChecker = this.main.getConfig().getInt("Checkers.Player-Count");
    }

    public void initializeLongs() { fileDeletion = this.main.getConfig().getLong("File-Deletion"); }

    public void initializeBooleans() {

        isLogToFiles = this.main.getConfig().getBoolean("Log-to-Files");
        isUpdateChecker = this.main.getConfig().getBoolean("Update-Checker");
        isExternal = this.main.getConfig().getBoolean("Database.Enable");
        isSqlite = this.main.getConfig().getBoolean("SQLite.Enable");
        isStaffEnabled = this.main.getConfig().getBoolean("Staff.Enabled");
        isWhitelisted = this.main.getConfig().getBoolean("Player-Commands.Whitelist-Commands");
        isBlacklisted = this.main.getConfig().getBoolean("Player-Commands.Blacklist-Commands");
        isCommandsToBlock = this.main.getConfig().getBoolean("Player-Commands.Blacklist-Commands");
        isCommandsToLog = this.main.getConfig().getBoolean("Player-Commands.Whitelist-Commands");
        isPlayerIP = this.main.getConfig().getBoolean("Player-Join.Player-IP");
        isConsoleCommands = this.main.getConfig().getBoolean("Console-Commands.Blacklist-Commands");
        isRegistration = this.main.getConfig().getBoolean("Log-Player.Registration");
        isPlayerDeathBackup = this.main.getConfig().getBoolean("Player-Death-Backup.Enabled");
        isViaVersion = this.main.getConfig().getBoolean("Log-Extras.ViaVersion");
    }

    public void initializePermissionStrings() {

        loggerExempt = "logger.exempt";
        loggerExemptDiscord = "logger.exempt.discord";
        loggerStaff = "logger.staff";
        loggerStaffLog = "logger.staff.log";
        loggerSpyBypass = "logger.spy.bypass";
        loggerSpy = "logger.spy";
        loggerReload = "logger.reload";
    }

    public void initializeCommands() {

        this.main.getCommand("logger").setExecutor(new CommandManager());
        //this.main.getCommand("loggerget").setExecutor(new Chat());
    }

    public void initializeEvents() {

        if (this.main.getConfig().getBoolean("Log-Player.Chat"))
            this.main.getServer().getPluginManager().registerEvents(new OnPlayerChat(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Commands"))
            this.main.getServer().getPluginManager().registerEvents(new OnCommand(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Sign-Text"))
            this.main.getServer().getPluginManager().registerEvents(new OnSign(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Join"))
            this.main.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Leave"))
            this.main.getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Kick"))
            this.main.getServer().getPluginManager().registerEvents(new OnPlayerKick(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Death")) {
            this.main.getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this.main);
            this.main.getServer().getPluginManager().registerEvents(new PlayerInventory(), this.main);
        }

        if (this.main.getConfig().getBoolean("Log-Player.Teleport"))
            this.main.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Level"))
            this.main.getServer().getPluginManager().registerEvents(new OnPlayerLevel(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Block-Place"))
            this.main.getServer().getPluginManager().registerEvents(new OnBlockPlace(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Block-Break"))
            this.main.getServer().getPluginManager().registerEvents(new OnBlockBreak(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Bucket-Fill"))
            this.main.getServer().getPluginManager().registerEvents(new OnBucketFill(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Bucket-Empty"))
            this.main.getServer().getPluginManager().registerEvents(new OnBucketEmpty(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Anvil"))
            this.main.getServer().getPluginManager().registerEvents(new OnAnvil(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Item-Pickup"))
            this.main.getServer().getPluginManager().registerEvents(new OnItemPickup(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Item-Drop"))
            this.main.getServer().getPluginManager().registerEvents(new OnItemDrop(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Enchanting"))
            this.main.getServer().getPluginManager().registerEvents(new OnEnchant(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Book-Editing"))
            this.main.getServer().getPluginManager().registerEvents(new OnBook(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Game-Mode"))
            this.main.getServer().getPluginManager().registerEvents(new OnGameMode(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Primed-TNT"))
            this.main.getServer().getPluginManager().registerEvents(new PrimedTNT(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Spawn-Egg"))
            this.main.getServer().getPluginManager().registerEvents(new SpawnEgg(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Entity-Death"))
            this.main.getServer().getPluginManager().registerEvents(new OnEntityDeath(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Furnace"))
            this.main.getServer().getPluginManager().registerEvents(new OnFurnace(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Craft"))
            this.main.getServer().getPluginManager().registerEvents(new OnCraft(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Chest-Interaction"))
            this.main.getServer().getPluginManager().registerEvents(new OnChestInteraction(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Item-Frame-Place"))
            this.main.getServer().getPluginManager().registerEvents(new ItemFramePlace(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Item-Frame-Break"))
            this.main.getServer().getPluginManager().registerEvents(new ItemFrameBreak(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.ArmorStand-Place")
                || this.main.getConfig().getBoolean("Log-Player.EndCrystal-Place"))
            this.main.getServer().getPluginManager().registerEvents(new ArmorStandEndCrystalPlace(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.ArmorStand-Break")
                || this.main.getConfig().getBoolean("Log-Player.EndCrystal-Break"))
            this.main.getServer().getPluginManager().registerEvents(new ArmorStandEndCrystalBreak(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.Lever-Interaction"))
            this.main.getServer().getPluginManager().registerEvents(new LeverInteraction(), this.main);

        if (this.main.getConfig().getBoolean("Log-Player.ArmorStand-Interaction"))
            this.main.getServer().getPluginManager().registerEvents(new ArmorStandInteraction(), this.main);

        if (this.main.getConfig().getBoolean("Log-Server.Advancements"))
            this.main.getServer().getPluginManager().registerEvents(new OnAdvancement(), this.main);

        // Server Side
        if (this.main.getConfig().getBoolean("Log-Server.Console-Commands"))
            this.main.getServer().getPluginManager().registerEvents(new Console(), this.main);

        if (this.main.getConfig().getBoolean("Log-Server.RAM"))
            this.main.getServer().getScheduler().scheduleSyncRepeatingTask(this.main, new RAM(), 300L, ramTpsChecker);

        if (this.main.getConfig().getBoolean("Log-Server.TPS"))
            this.main.getServer().getScheduler().scheduleSyncRepeatingTask(this.main, new TPS(), 300L, ramTpsChecker);

        if (this.main.getConfig().getBoolean("Log-Server.Portal-Creation"))
            this.main.getServer().getPluginManager().registerEvents(new PortalCreation(), this.main);

        if (this.main.getConfig().getBoolean("Log-Server.RCON"))
            this.main.getServer().getPluginManager().registerEvents(new RCON(), this.main);

        if (this.main.getConfig().getBoolean("Log-Server.Command-Block"))
            this.main.getServer().getPluginManager().registerEvents(new CommandBlock(), this.main);

        if (this.main.getConfig().getBoolean("Log-Server.Player-Count"))
            this.main.getServer().getScheduler().scheduleSyncRepeatingTask(this.main, new PlayerCount(), 300L, playerCountChecker);

        if (this.main.getConfig().getBoolean("Log-Server.Server-Address"))
            this.main.getServer().getPluginManager().registerEvents(new ServerAddress(), this.main);

        // Version Exceptions
        if (version.isAtLeast(NmsVersions.v1_13_R1) && this.main.getConfig().getBoolean("Log-Version-Exceptions.Wood-Stripping"))
                this.main.getServer().getPluginManager().registerEvents(new OnWoodStripping(), this.main);

        if (version.isAtLeast(NmsVersions.v1_9_R1) && this.main.getConfig().getBoolean("Log-Version-Exceptions.Totem-of-Undying"))
            this.main.getServer().getPluginManager().registerEvents(new OnTotemUse(), this.main);

        // Spy Features
        if (this.main.getConfig().getBoolean("Spy-Features.Commands-Spy.Enable"))
            this.main.getServer().getPluginManager().registerEvents(new OnCommandSpy(), this.main);

        if (this.main.getConfig().getBoolean("Spy-Features.Anvil-Spy.Enable"))
            this.main.getServer().getPluginManager().registerEvents(new OnAnvilSpy(), this.main);

        if (this.main.getConfig().getBoolean("Spy-Features.Book-Spy.Enable"))
            this.main.getServer().getPluginManager().registerEvents(new OnBookSpy(), this.main);

        if (this.main.getConfig().getBoolean("Spy-Features.Sign-Spy.Enable"))
            this.main.getServer().getPluginManager().registerEvents(new OnSignSpy(), this.main);

        this.initializeDependentEvents();
    }

    private void initializeDependentEvents() {

        EssentialsUtil.getEssentialsHook();

        AuthMeUtil.getAuthMeHook();

        VaultUtil.getVaultHook();

        LiteBanUtil.getLiteBanHook();

        AdvancedBanUtil.getAdvancedBanHook();

        PlaceHolderAPIUtil.getPlaceHolderHook();

        GeyserUtil.getGeyserHook();

        ViaVersionUtil.getViaVersionHook();

//        WorldGuardUtil.getWorldGuardHook();
    }

    public void initializeVersionOptions() {

        pluginVersion = this.main.getDescription().getVersion();

        configVersion = this.main.getConfig().getString("Config-Version", "");

        version = NmsVersions.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]);

        if (version.isAtLeast(NmsVersions.v1_13_R1)) {

            type = Material.PLAYER_HEAD;
            isNewVersion = true;
        } else {
            type = Material.matchMaterial("SKULL_ITEM");
            isNewVersion = false;
        }
    }
}
