package me.prism3.loggerbungeecord.utils;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.hooks.LiteBanUtil;
import me.prism3.loggerbungeecord.hooks.PartyAndFriendsUtil;
import me.prism3.loggerbungeecord.events.OnChat;
import me.prism3.loggerbungeecord.events.OnLeave;
import me.prism3.loggerbungeecord.events.OnLogin;
import me.prism3.loggerbungeecord.events.OnServerSwitch;
import me.prism3.loggerbungeecord.events.oncommands.OnCommand;
import me.prism3.loggerbungeecord.events.plugindependent.OnLiteBan;
import me.prism3.loggerbungeecord.events.plugindependent.paf.OnFriendMessage;
import me.prism3.loggerbungeecord.events.plugindependent.paf.OnPartyMessage;
import me.prism3.loggerbungeecord.serverside.OnReload;
import me.prism3.loggerbungeecord.serverside.RAM;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    public static String discordSupportServer;

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

    public void initializeDateFormatter() {

        dateTimeFormatter = DateTimeFormatter.ofPattern(Objects.requireNonNull(config.getString("Time-Formatter")));

    }

    public void initializeStrings() {

        pluginVersion = this.main.getDescription().getVersion();
        serverName = this.config.getString("Server-Name");
        discordSupportServer = "https://discord.gg/MfR5mcpVfX";

    }

    public void initializeListOfStrings() {

        configVersion = this.config.getString("Config");
        commandsToBlock = this.config.getStringList("Player-Commands.Commands-to-Block");
        commandsToLog = this.config.getStringList("Player-Commands.Commands-to-Log");
        dbType = this.config.getString("Database.Type");
        dbHost = this.config.getString("Database.Host");
        dbUserName = this.config.getString("Database.Username");
        dbPassword = this.config.getString("Database.Password");
        dbName = this.config.getString("Database.Database");

    }

    public void initializeIntegers() {

        ramChecker = this.config.getInt("RAM.Checker");
        fileDeletion = this.config.getInt("File-Deletion");
        ramPercent = this.config.getInt("RAM.Percent");
        dbPort = this.config.getInt("Database.Port");
        externalDataDel = this.config.getInt("Database.Data-Deletion");
        sqliteDataDel = this.config.getInt("SQLite.Data-Deletion");

    }

    public void initializeBoolean() {

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

    public void initializePermissionStrings() {

        loggerStaff = "loggerproxy.staff";
        loggerReload = "loggerproxy.reload";
        loggerExempt = "loggerproxy.exempt";
        loggerExemptDiscord = "loggerproxy.exempt.discord";
        loggerStaffLog = "loggerproxy.staff.log";

    }

    public void eventInitializer() {

        if (this.main.getConfig().getBoolean("Log-Player.Chat"))
            this.main.getProxy().getPluginManager().registerListener(this.main, new OnChat());

        if (this.main.getConfig().getBoolean("Log-Player.Commands"))
            this.main.getProxy().getPluginManager().registerListener(this.main, new OnCommand());

        if (this.main.getConfig().getBoolean("Log-Player.Login"))
            this.main.getProxy().getPluginManager().registerListener(this.main, new OnLogin());

        if (this.main.getConfig().getBoolean("Log-Player.Leave"))
            this.main.getProxy().getPluginManager().registerListener(this.main, new OnLeave());

        if (this.main.getConfig().getBoolean("Log-Player.Server-Switch"))
            this.main.getProxy().getPluginManager().registerListener(this.main, new OnServerSwitch());

        // Server Side
        if (this.main.getConfig().getBoolean("Log-Server.Reload"))
            this.main.getProxy().getPluginManager().registerListener(this.main, new OnReload());

        if (this.main.getConfig().getBoolean("Log-Server.RAM"))
            this.main.getProxy().getScheduler().schedule(this.main, new RAM(), 200L, ramChecker / 20, TimeUnit.SECONDS);

        this.dependentEventInitializer();
    }

    private void dependentEventInitializer() {

        if (LiteBanUtil.getLiteBansAPI() != null && this.main.getConfig().getBoolean("Log-Extras.LiteBans")) {

            this.main.getProxy().getScheduler().schedule(this.main, new OnLiteBan(), 5L, 0, TimeUnit.SECONDS);
            Log.info("LiteBans Plugin Detected!");

        }

        if (PartyAndFriendsUtil.getPartyAndFriendsAPI() != null && this.main.getConfig().getBoolean("Log-Extras.PAF")) {

            if (this.main.getConfig().getBoolean("PAF.Friend-Message"))
                this.main.getProxy().getPluginManager().registerListener(this.main, new OnFriendMessage());

            if (this.main.getConfig().getBoolean("PAF.Party-Message"))
                this.main.getProxy().getPluginManager().registerListener(this.main, new OnPartyMessage());

            Log.info("PartyAndFriends Plugin Detected!");

        }
    }
}
