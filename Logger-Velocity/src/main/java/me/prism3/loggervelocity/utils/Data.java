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

        fileDeletion = this.main.getConfig().getInt("File-Deletion");
        ramPercent = this.main.getConfig().getInt("RAM.Percent");
        dbPort = this.main.getConfig().getInt("Database.Port");
        externalDataDel = this.main.getConfig().getInt("Database.Data-Deletion");
        sqliteDataDel = this.main.getConfig().getInt("SQLite.Data-Deletion");

    }

    public void initializeLongs() {

        ramChecker = this.main.getConfig().getLong("RAM.Checker");

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
        isServerSwitch = this.main.getConfig().getBoolean("Log-Player.Server-Switch");
        isKick = this.main.getConfig().getBoolean("Log-Player.Kick");


    }

    public void initializePermissionStrings() {

        loggerStaff = "loggerproxy.staff";
        loggerReload = "loggerproxy.reload";
        loggerExempt = "loggerproxy.exempt";
        loggerExemptDiscord = "loggerproxy.exempt.discord";
        loggerStaffLog = "loggerproxy.staff.log";

    }
}
