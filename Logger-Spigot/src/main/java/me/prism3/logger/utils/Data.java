package me.prism3.logger.utils;

import com.carpour.loggercore.database.data.DatabaseCredentials;
import com.carpour.loggercore.database.data.Options;
import me.prism3.logger.Main;
import me.prism3.logger.api.*;
import me.prism3.logger.commands.subcommands.PlayerInventory;
import me.prism3.logger.events.*;
import me.prism3.logger.events.misc.ItemFrameBreak;
import me.prism3.logger.events.misc.ItemFramePlace;
import me.prism3.logger.events.misc.OnPrimedTNT;
import me.prism3.logger.events.oncommands.OnCommand;
import me.prism3.logger.events.oninventories.OnChestInteraction;
import me.prism3.logger.events.oninventories.OnCraft;
import me.prism3.logger.events.oninventories.OnFurnace;
import me.prism3.logger.events.onversioncompatibility.OnWoodStripping;
import me.prism3.logger.events.plugindependent.*;
import me.prism3.logger.serverside.*;
import me.prism3.logger.utils.enums.NmsVersions;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    private final Main main = Main.getInstance();

    // Date Format
    public static DateTimeFormatter dateTimeFormatter;

    // String
    public static String configVersion;
    public static String pluginVersion;
    public static String serverName;
    public static String gameModeConf;

    public static String langPath;
    public static String fileType;
    public static String selectedLang;
    public static String discordSupportServer;
    public static String pluginPrefix;

    // List<String>
    public static List<String> commandsToBlock;
    public static List<String> commandsToLog;
    public static List<String> consoleCommandsToBlock;

    // Integer
    public static int resource_ID;
    public static int ramTpsChecker;
    public static int vaultChecker;
    public static int abovePlayerLevel;
    public static int ramPercent;
    public static int tpsMedium;
    public static int tpsCritical;

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

    // Permission String
    public static String loggerExempt;
    public static String loggerExemptDiscord;
    public static String loggerStaff;
    public static String loggerStaffLog;
    public static String loggerUpdate;
    public static String loggerSpyBypass;
    public static String loggerSpy;
    public static String loggerReload;
    public static Options options = new Options();
    public static DatabaseCredentials databaseCredentials;

    public void initializeDateFormatter() {

        dateTimeFormatter = DateTimeFormatter.ofPattern(this.main.getConfig().getString("Time-Formatter"));

    }

    public void initializeStrings() {

        configVersion = this.main.getConfig().getString("Config");
        serverName = this.main.getConfig().getString("Server-Name");
        gameModeConf = this.main.getConfig().getString("Game-Mode");
        langPath = "messages";
        fileType = ".yml";
        selectedLang = this.main.getConfig().getString("Language");
        discordSupportServer = "https://discord.gg/MfR5mcpVfX";
        pluginPrefix = this.main.getConfig().getString("Plugin-Prefix");

    }

    public void initializeDatabaseCredentials() {
        Data.databaseCredentials = new DatabaseCredentials(
                this.main.getConfig().getString("Database.Type"),
                this.main.getConfig().getString("Database.Host"),
                this.main.getConfig().getString("Database.Username"),
                this.main.getConfig().getString("Database.Password"),
                this.main.getConfig().getString("Database.Database"),
                this.main.getConfig().getInt("Database.Port"),
                true
        );
    }

    public void initializeOptions() {

        final Map<String, Object> optionS = new HashMap<>(30);

        optionS.putAll(main.getConfig().getConfigurationSection("Log-Player").getValues(false));
        optionS.putAll(main.getConfig().getConfigurationSection("Log-Server").getValues(false));
        optionS.putAll(main.getConfig().getConfigurationSection("Log-Extras").getValues(false));
//        options.putAll(main.getConfig().getConfigurationSection("Log-Version-Exceptions").getValues(false));
        System.out.println(optionS);
        options.setEnabledLogs(optionS);

    }

    public void initializeListOfStrings() {

        commandsToBlock = this.main.getConfig().getStringList("Player-Commands.Commands-to-Block");
        commandsToLog = this.main.getConfig().getStringList("Player-Commands.Commands-to-Log");
        consoleCommandsToBlock = this.main.getConfig().getStringList("Console-Commands.Commands-to-Block");

    }

    public void initializeIntegers() {

        pluginVersion = this.main.getDescription().getVersion();
        resource_ID = 94236;
        ramTpsChecker = this.main.getConfig().getInt("RAM-TPS-Checker");
        vaultChecker = this.main.getConfig().getInt("Vault-Checker");
        abovePlayerLevel = this.main.getConfig().getInt("Player-Level.Log-Above");
        ramPercent = this.main.getConfig().getInt("RAM.Percent");
        tpsMedium = this.main.getConfig().getInt("TPS.Value-Medium");
        tpsCritical = this.main.getConfig().getInt("TPS.Value-Critical");
        sqliteDataDel = this.main.getConfig().getInt("SQLite.Data-Deletion");
        allowedBackups = this.main.getConfig().getInt("Player-Death-Backup.Max-Backup");

    }

    public void initializeLongs() {

        fileDeletion = this.main.getConfig().getLong("File-Deletion");

    }

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

    public void eventInitializer() {

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
            this.main.getServer().getPluginManager().registerEvents(new OnPrimedTNT(), this.main);

//        if (this.main.getConfig().getBoolean("Log-Player.Chat"))
//        this.main.getServer().getPluginManager().registerEvents(new OnSpawnEgg(), this.main);

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
            this.main.getServer().getPluginManager().registerEvents(new OnCommandBlock(), this.main);

        // Version Exceptions
        if (this.main.getVersion().isAtLeast(NmsVersions.v1_13_R1) && this.main.getConfig().getBoolean("Log-Version-Exceptions.Wood-Stripping"))
                this.main.getServer().getPluginManager().registerEvents(new OnWoodStripping(), this.main);

        this.dependentEventInitializer();

    }

    private void dependentEventInitializer() {

        if (EssentialsUtil.getEssentialsAPI() != null && this.main.getConfig().getBoolean("Log-Extras.Essentials-AFK")) {

            this.main.getServer().getPluginManager().registerEvents(new OnAFK(), this.main);

            this.main.getLogger().info("Essentials Plugin Detected!");

            options.setEssentialsEnabled(true);

        }

        if (AuthMeUtil.getAuthMeAPI() != null && this.main.getConfig().getBoolean("Log-Extras.AuthMe-Wrong-Password")) {

            this.main.getServer().getPluginManager().registerEvents(new OnAuthMePassword(), this.main);

            this.main.getLogger().info("AuthMe Plugin Detected!");

            options.setAuthMeEnabled(true);

        }

        if (VaultUtil.getVaultAPI() && this.main.getConfig().getBoolean("Log-Extras.Vault")) {

            if (VaultUtil.getVault() != null) {

                final OnVault vault = new OnVault();
                this.main.getServer().getPluginManager().registerEvents(vault, this.main);
                this.main.getServer().getScheduler().scheduleSyncRepeatingTask(this.main, vault, 10L, vaultChecker);
            }

            this.main.getLogger().info("Vault Plugin Detected!");
            options.setVaultEnabled(true);
        }

        if (LiteBansUtil.getLiteBansAPI() != null && this.main.getConfig().getBoolean("Log-Extras.LiteBans")) {

            this.main.getServer().getScheduler().scheduleSyncDelayedTask(this.main, new OnLiteBanEvents(), 10L);

            this.main.getLogger().info("LiteBans Plugin Detected!");

            options.setLiteBansEnabled(true);
        }

        if (AdvancedBanUtil.getAdvancedBanAPI() != null && this.main.getConfig().getBoolean("Log-Extras.AdvancedBan")) {

            this.main.getServer().getPluginManager().registerEvents(new OnAdvancedBan(), this.main);

            this.main.getLogger().info("AdvancedBan Plugin Detected!");

            options.setAdvancedBanEnabled(true);
        }

        if (PlaceHolderAPIUtil.getPlaceHolderAPI() != null) {

            this.main.getLogger().info("PlaceHolderAPI Plugin Detected!");

        }

        if (GeyserUtil.getGeyserAPI() != null && FloodGateUtil.getFloodGateAPI()) {

            this.main.getLogger().info("Geyser & FloodGate Plugins Detected!");
            this.main.getLogger().warning("Geyser & FloodGate are not fully supported! If any errors occurs, contact the authors.");

        }

        if (ViaVersionUtil.getViaVersionAPI() != null && this.main.getConfig().getBoolean("Log-Extras.ViaVersion")) {

            this.main.getLogger().info("ViaVersion Plugin Detected!");

            options.setViaVersion(true);
        }
    }
}
