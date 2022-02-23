package com.carpour.loggerbungeecord.Utils;

import com.carpour.loggerbungeecord.Main;

public class Data {

    private final Main main = Main.getInstance();
    private final ConfigManager config = main.getConfig();

    public static String pluginVersion;

    public static int ramChecker;
    public static int fileDeletion;

    public static boolean isExternal;
    public static boolean isSqlite;
    public static boolean isLogToFiles;
    public static boolean isStaffEnabled;

    public static String loggerStaff;
    public static String loggerReload;

    public void initializeStrings(){

        pluginVersion = main.getDescription().getVersion();

    }

    public void initializeIntegers(){

        ramChecker = this.config.getInt("RAM.Checker");
        fileDeletion = this.config.getInt("File-Deletion");

    }

    public void initializeBoolean(){

        isExternal = this.config.getBoolean("Database.Enable");
        isSqlite = this.config.getBoolean("SQLite.Enable");
        isLogToFiles = this.config.getBoolean("Log-to-Files");
        isStaffEnabled = this.config.getBoolean("Staff.Enabled");

    }

    public void initializePermissionStrings(){

        loggerStaff = "loggerproxy.staff";
        loggerReload = "loggerproxy.reload";

    }
}
