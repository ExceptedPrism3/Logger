package me.prism3.logger.utils;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.NmsVersions;
import org.bukkit.Bukkit;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Data {

    private static final Main main = Main.getInstance();

    // Date Format
    public static DateTimeFormatter dateTimeFormatter;

    // String
    public static String configVersion;
    public static String pluginVersion;
    public static String serverName;
    public static String gameModeConf;
    public static String dbType;
    public static String dbHost;
    public static String dbUserName;
    public static String dbPassword;
    public static String dbName;
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
    public static int dbPort;
    public static int externalDataDel;
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

    public static NmsVersions version;

    public static void initializer() {

        initializeDateFormatter();
        initializeStrings();
        initializeListOfStrings();
        initializeIntegers();
        initializeLongs();
        initializeBooleans();
        initializePermissionStrings();
    }

    private static void initializeDateFormatter() {

        dateTimeFormatter = DateTimeFormatter.ofPattern(Objects.requireNonNull(main.getConfig().getString("Time-Formatter")));
    }

    private static void initializeStrings() {

        configVersion = main.getConfig().getString("Config");
        serverName = main.getConfig().getString("Server-Name");
        gameModeConf = main.getConfig().getString("Game-Mode");
        dbType = main.getConfig().getString("Database.Type");
        dbHost = main.getConfig().getString("Database.Host");
        dbUserName = main.getConfig().getString("Database.Username");
        dbPassword = main.getConfig().getString("Database.Password");
        dbName = main.getConfig().getString("Database.Database");
        langPath = "messages";
        fileType = ".yml";
        selectedLang = main.getConfig().getString("Language");
        discordSupportServer = "https://discord.gg/MfR5mcpVfX";
        pluginPrefix = main.getConfig().getString("Plugin-Prefix");
    }

    private static void initializeListOfStrings() {

        commandsToBlock = main.getConfig().getStringList("Player-Commands.Commands-to-Block");
        commandsToLog = main.getConfig().getStringList("Player-Commands.Commands-to-Log");
        consoleCommandsToBlock = main.getConfig().getStringList("Console-Commands.Commands-to-Block");
    }

    private static void initializeIntegers() {

        pluginVersion = main.getDescription().getVersion();
        resource_ID = 94236;
        ramTpsChecker = main.getConfig().getInt("RAM-TPS-Checker");
        vaultChecker = main.getConfig().getInt("Vault-Checker");
        abovePlayerLevel = main.getConfig().getInt("Player-Level.Log-Above");
        ramPercent = main.getConfig().getInt("RAM.Percent");
        tpsMedium = main.getConfig().getInt("TPS.Value-Medium");
        tpsCritical = main.getConfig().getInt("TPS.Value-Critical");
        dbPort = main.getConfig().getInt("Database.Port");
        externalDataDel = main.getConfig().getInt("Database.Data-Deletion");
        sqliteDataDel = main.getConfig().getInt("SQLite.Data-Deletion");
        allowedBackups = main.getConfig().getInt("Player-Death-Backup.Max-Backup");
        version = versionChecker();
    }

    private static void initializeLongs() {

        fileDeletion = main.getConfig().getLong("File-Deletion");
    }

    private static void initializeBooleans() {

        isLogToFiles = main.getConfig().getBoolean("Log-to-Files");
        isUpdateChecker = main.getConfig().getBoolean("Update-Checker");
        isExternal = main.getConfig().getBoolean("Database.Enable");
        isSqlite = main.getConfig().getBoolean("SQLite.Enable");
        isStaffEnabled = main.getConfig().getBoolean("Staff.Enabled");
        isWhitelisted = main.getConfig().getBoolean("Player-Commands.Whitelist-Commands");
        isBlacklisted = main.getConfig().getBoolean("Player-Commands.Blacklist-Commands");
        isCommandsToBlock = main.getConfig().getBoolean("Player-Commands.Blacklist-Commands");
        isCommandsToLog = main.getConfig().getBoolean("Player-Commands.Whitelist-Commands");
        isPlayerIP = main.getConfig().getBoolean("Player-Join.Player-IP");
        isConsoleCommands = main.getConfig().getBoolean("Console-Commands.Blacklist-Commands");
        isRegistration = main.getConfig().getBoolean("Log-Player.Registration");
        isPlayerDeathBackup = main.getConfig().getBoolean("Player-Death-Backup.Enabled");
    }

    private static void initializePermissionStrings() {

        loggerExempt = "logger.exempt";
        loggerExemptDiscord = "logger.exempt.discord";
        loggerStaff = "logger.staff";
        loggerStaffLog = "logger.staff.log";
        loggerSpyBypass = "logger.spy.bypass";
        loggerSpy = "logger.spy";
        loggerReload = "logger.reload";
    }

    private static NmsVersions versionChecker() {

        try {
            return NmsVersions.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3]);

        } catch (final IllegalArgumentException e) {

            Log.severe("Current version is unknown, using the latest known one.");
            return NmsVersions.v1_21_R1;
        }
    }
}
