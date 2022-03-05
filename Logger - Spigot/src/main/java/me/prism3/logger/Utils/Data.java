package me.prism3.logger.Utils;

import me.prism3.logger.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Data {

    private final Main main = Main.getInstance();
    private final FileConfiguration config = main.getConfig();

    // Date Format
    public static DateTimeFormatter dateTimeFormatter;

    // String
    public static String pluginVersion;
    public static String serverName;
    public static String gameModeConf;
    public static String dbType;
    public static String dbHost;
    public static String dbUserName;
    public static String dbPassword;
    public static String dbName;

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

    // Permission String
    public static String loggerExempt;
    public static String loggerExemptDiscord;
    public static String loggerStaff;
    public static String loggerStaffLog;
    public static String loggerUpdate;
    public static String loggerSpyBypass;
    public static String loggerSpy;
    public static String loggerReload;

    public void initializeDateFormatter(){

        dateTimeFormatter = DateTimeFormatter.ofPattern(Objects.requireNonNull(this.config.getString("Time-Formatter")));

    }

    public void initializeStrings(){

        serverName = this.config.getString("Server-Name");
        gameModeConf = this.config.getString("Game-Mode");
        dbType = this.config.getString("Database.Type");
        dbHost = this.config.getString("Database.Host");
        dbUserName = this.config.getString("Database.Username");
        dbPassword = this.config.getString("Database.Password");
        dbName = this.config.getString("Database.Database");


    }

    public void initializeListOfStrings(){

        commandsToBlock = this.config.getStringList("Player-Commands.Commands-to-Block");
        commandsToLog = this.config.getStringList("Player-Commands.Commands-to-Log");
        consoleCommandsToBlock = this.config.getStringList("Console-Commands.Commands-to-Block");

    }

    public void initializeIntegers(){

        pluginVersion = this.main.getDescription().getVersion();
        resource_ID = 94236;
        ramTpsChecker = this.config.getInt("RAM-TPS-Checker");
        vaultChecker = this.config.getInt("Vault.Checker");
        abovePlayerLevel = this.config.getInt("Player-Level.Log-Above");
        ramPercent = this.config.getInt("RAM.Percent");
        tpsMedium = this.config.getInt("TPS.Value-Medium");
        tpsCritical = this.config.getInt("TPS.Value-Critical");
        dbPort = this.config.getInt("Database.Port");
        externalDataDel = this.config.getInt("Database.Data-Deletion");
        sqliteDataDel = this.config.getInt("SQLite.Data-Deletion");

    }

    public void initializeLongs(){

        fileDeletion = this.config.getLong("File-Deletion");

    }

    public void initializeBooleans(){

        isLogToFiles = this.config.getBoolean("Log-to-Files");
        isUpdateChecker = this.config.getBoolean("Update-Checker");
        isExternal = this.config.getBoolean("Database.Enable");
        isSqlite = this.config.getBoolean("SQLite.Enable");
        isStaffEnabled = this.config.getBoolean("Staff.Enabled");
        isWhitelisted = this.config.getBoolean("Player-Commands.Whitelist-Commands");
        isBlacklisted = this.config.getBoolean("Player-Commands.Blacklist-Commands");
        isCommandsToBlock = this.config.getBoolean("Player-Commands.Blacklist-Commands");
        isCommandsToLog = this.config.getBoolean("Player-Commands.Whitelist-Commands");
        isPlayerIP = this.config.getBoolean("Player-Join.Player-IP");
        isConsoleCommands = this.config.getBoolean("Console-Commands.Blacklist-Commands");

    }

    public void initializePermissionStrings(){

        loggerExempt = "logger.exempt";
        loggerExemptDiscord = "logger.exempt.discord";
        loggerStaff = "logger.staff";
        loggerStaffLog = "logger.staff.log";
        loggerSpyBypass = "logger.spy.bypass";
        loggerSpy = "logger.spy";
        loggerReload = "logger.reload";

    }
}
