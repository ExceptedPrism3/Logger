package me.prism3.loggerbungeecord.Utils;

import me.prism3.loggerbungeecord.Main;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Data {

    private final Main main = Main.getInstance();
    private final ConfigManager config = main.getConfig();

    // Date Format
    public static DateTimeFormatter dateTimeFormatter;

    // String
    public static String pluginVersion;
    public static String serverName;
    public static String dbType;
    public static String dbHost;
    public static String dbUserName;
    public static String dbPassword;
    public static String dbName;
    public static String configVersion;

    // List<String>
    public static List<String> commandsToBlock;
    public static List<String> commandsToLog;

    // Integer
    public static int ramChecker;
    public static int fileDeletion;
    public static int ramPercent;
    public static int externalDataDel;
    public static int sqliteDataDel;
    public static int dbPort;

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
    public static boolean isLiteBansBan;
    public static boolean isLiteBansTempBan;
    public static boolean isLiteBansIpBan;
    public static boolean isLiteBansTempIpBan;
    public static boolean isLiteBansMute;
    public static boolean isLiteBansTempMute;
    public static boolean isLiteBansKick;
    public static boolean isUpdateChecker;

    // Permission String
    public static String loggerStaff;
    public static String loggerReload;
    public static String loggerExempt;
    public static String loggerExemptDiscord;
    public static String loggerStaffLog;

    public void initializeDateFormatter(){

        dateTimeFormatter = DateTimeFormatter.ofPattern(Objects.requireNonNull(config.getString("Time-Formatter")));

    }

    public void initializeStrings(){

        pluginVersion = this.main.getDescription().getVersion();
        serverName = this.config.getString("Server-Name");

    }

    public void initializeListOfStrings(){

        configVersion = this.config.getString("Config");
        commandsToBlock = this.config.getStringList("Player-Commands.Commands-to-Block");
        commandsToLog = this.config.getStringList("Player-Commands.Commands-to-Log");
        dbType = this.config.getString("Database.Type");
        dbHost = this.config.getString("Database.Host");
        dbUserName = this.config.getString("Database.Username");
        dbPassword = this.config.getString("Database.Password");
        dbName = this.config.getString("Database.Database");

    }

    public void initializeIntegers(){

        ramChecker = this.config.getInt("RAM.Checker");
        fileDeletion = this.config.getInt("File-Deletion");
        ramPercent = this.config.getInt("RAM.Percent");
        dbPort = this.config.getInt("Database.Port");
        externalDataDel = this.config.getInt("Database.Data-Deletion");
        sqliteDataDel = this.config.getInt("SQLite.Data-Deletion");

    }

    public void initializeBoolean(){

        isExternal = this.config.getBoolean("Database.Enable");
        isSqlite = this.config.getBoolean("SQLite.Enable");
        isLogToFiles = this.config.getBoolean("Log-to-Files");
        isStaffEnabled = this.config.getBoolean("Staff.Enabled");
        isWhitelisted = this.config.getBoolean("Player-Commands.Whitelist-Commands");
        isBlacklisted = this.config.getBoolean("Player-Commands.Blacklist-Commands");
        isCommandsToBlock = this.config.getBoolean("Player-Commands.Blacklist-Commands");
        isCommandsToLog = this.config.getBoolean("Player-Commands.Whitelist-Commands");
        isPlayerIP = this.config.getBoolean("Player-Login.Player-IP");
        isLiteBansIpBan = this.config.getBoolean("LiteBans.IP-Ban");
        isLiteBansTempIpBan = this.config.getBoolean("LiteBans.Temp-IP-Ban");
        isLiteBansBan = this.config.getBoolean("LiteBans.Ban");
        isLiteBansTempBan = this.config.getBoolean("LiteBans.Temp-Ban");
        isLiteBansMute = this.config.getBoolean("LiteBans.Mute");
        isLiteBansTempMute = this.config.getBoolean("LiteBans.Temp-Mute");
        isLiteBansKick = this.config.getBoolean("LiteBans.Kick");
        isUpdateChecker = this.config.getBoolean("Update.Checker");

    }

    public void initializePermissionStrings(){

        loggerStaff = "loggerproxy.staff";
        loggerReload = "loggerproxy.reload";
        loggerExempt = "loggerproxy.exempt";
        loggerExemptDiscord = "loggerproxy.exempt.discord";
        loggerStaffLog = "loggerproxy.staff.log";

    }
}
