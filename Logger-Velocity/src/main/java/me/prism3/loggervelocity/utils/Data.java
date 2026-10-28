package me.prism3.loggervelocity.utils;

import me.prism3.loggervelocity.Logger;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Data {

    private final Logger main = Logger.getInstance();

    // Date Format
    public static DateTimeFormatter dateTimeFormatter;

    // String
    public static String serverName;
    public static String dbType;
    public static String dbHost;
    public static String dbUserName;
    public static String dbPassword;
    public static String dbName;
    public static String discordSupportServer;
    public static String pluginPrefix;

    // List<String>
    public static List<String> commandsToBlock;
    public static List<String> commandsToLog;

    // Integer
    public static int fileDeletion;
    public static int ramPercent;
    public static int externalDataDel;
    public static int sqliteDataDel;
    public static int dbPort;

    // Long
    public static long ramChecker;

    // Boolean
    public static boolean isExternal;
    public static boolean isSqlite;
    public static boolean isLogToFiles;
    public static boolean isStaffEnabled;
    public static boolean isWhitelisted;
    public static boolean isBlacklisted;
    public static boolean isCommandsToBlock;
    public static boolean isCommandsToLog;
    public static boolean isPlayerIP;
    public static boolean isServerSwitch;
    public static boolean isKick;


    // Permission String
    public static String loggerStaff;
    public static String loggerReload;
    public static String loggerExempt;
    public static String loggerExemptDiscord;
    public static String loggerStaffLog;

    public void initializeDateFormatter() {

        dateTimeFormatter = DateTimeFormatter.ofPattern(Objects.requireNonNull(this.main.getConfig().getString("Time-Formatter")));

    }

    public void initializeStrings() {

        serverName = this.main.getConfig().getString("Server-Name");
        discordSupportServer = "https://discord.gg/MfR5mcpVfX";
        pluginPrefix = this.main.getConfig().getString("Plugin-Prefix");

    }

    public void initializeListOfStrings() {

        commandsToBlock = this.main.getConfig().getStringList("Player-Command.Commands-to-Block");
        commandsToLog = this.main.getConfig().getStringList("Player-Command.Commands-to-Log");
        dbType = this.main.getConfig().getString("Database.Type");
        dbHost = this.main.getConfig().getString("Database.Host");
        dbUserName = this.main.getConfig().getString("Database.Username");
        dbPassword = this.main.getConfig().getString("Database.Password");
        dbName = this.main.getConfig().getString("Database.Database");

    }

    public void initializeIntegers() {

        fileDeletion = this.main.getConfig().get("File-Deletion") != null
                ? this.main.getConfig().getInt("File-Deletion")
                : (this.main.getConfig().get("Delete-Old-Logs") != null ? this.main.getConfig().getInt("Delete-Old-Logs") : 30);
        ramPercent = this.main.getConfig().get("RAM.Percent") != null
                ? this.main.getConfig().getInt("RAM.Percent")
                : (this.main.getConfig().get("RAM-Checker.Threshold") != null ? this.main.getConfig().getInt("RAM-Checker.Threshold") : 90);
        dbPort = this.main.getConfig().getInt("Database.Port");
        externalDataDel = this.main.getConfig().getInt("Database.Data-Deletion");
        sqliteDataDel = this.main.getConfig().getInt("SQLite.Data-Deletion");

    }

    public void initializeLongs() {

        long interval = this.main.getConfig().get("RAM.Checker") != null
                ? this.main.getConfig().getLong("RAM.Checker")
                : (this.main.getConfig().get("RAM-Checker.Interval") != null ? this.main.getConfig().getLong("RAM-Checker.Interval") : 60L);
        ramChecker = interval > 0 ? interval : 60L;

    }

    public void initializeBoolean() {

        isExternal = this.main.getConfig().getBoolean("Database.Enable");
        isSqlite = this.main.getConfig().getBoolean("SQLite.Enable");
        if (this.main.getConfig().get("Files.Enabled") != null) {
            isLogToFiles = this.main.getConfig().getBoolean("Files.Enabled", true);
        } else if (this.main.getConfig().get("Log-to-Files") != null) {
            isLogToFiles = this.main.getConfig().getBoolean("Log-to-Files", true);
        } else {
            isLogToFiles = true;
        }
        isStaffEnabled = this.main.getConfig().getBoolean("Staff.Enabled");
        isWhitelisted = this.main.getConfig().getBoolean("Player-Command.Whitelist-Commands");
        isBlacklisted = this.main.getConfig().getBoolean("Player-Command.Blacklist-Commands");
        isCommandsToBlock = this.main.getConfig().getBoolean("Player-Command.Blacklist-Commands");
        isCommandsToLog = this.main.getConfig().getBoolean("Player-Command.Whitelist-Commands");
        isPlayerIP = this.main.getConfig().getBoolean("Player-Login.Player-IP");
        isServerSwitch = this.main.getConfig().get("Log-Player.Server-Switch") != null
                ? this.main.getConfig().getBoolean("Log-Player.Server-Switch")
                : this.main.getConfig().getBoolean("Log-Player.Switch", true);
        isKick = this.main.getConfig().getBoolean("Log-Player.Kick", true);

    }

    public void initializePermissionStrings() {

        loggerStaff = "loggerproxy.staff";
        loggerReload = "loggerproxy.reload";
        loggerExempt = "loggerproxy.exempt";
        loggerExemptDiscord = "loggerproxy.exempt.discord";
        loggerStaffLog = "loggerproxy.staff.log";

    }
}
