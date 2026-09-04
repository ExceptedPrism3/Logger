package me.prism3.logger_bungee;

import me.prism3.logger_bungee.commands.LoggerCommand;
import me.prism3.logger_bungee.managers.*;
import me.prism3.logger_bungee.utils.Log;
import me.prism3.logger_core.database.DatabaseConfig;
import me.prism3.logger_core.database.DatabaseManager;
import me.prism3.logger_core.platform.LoggerPlatform;
import net.md_5.bungee.api.plugin.Plugin;

public class LoggerBungee extends Plugin implements LoggerPlatform {

    private static LoggerBungee instance;

    private ConfigManager configManager;
    private MessageManager messageManager;
    private FileManager fileManager;
    private EventManager eventManager;
    private LogManager logManager;
    private PermissionManager permissionManager;

    private me.prism3.logger_core.discord.DiscordManager discordManager;
    private me.prism3.logger_core.database.DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        instance = this;
        Log.setup(this.getLogger());
        Log.setPlugin(this);

        // Initialize Managers
        this.configManager = new ConfigManager(this);
        this.messageManager = new MessageManager(this);

        // Initialize Core Database
        this.initDatabase();

        this.fileManager = new FileManager(this);
        this.eventManager = new EventManager(this);
        this.logManager = new LogManager(this);
        this.permissionManager = new PermissionManager(this);

        // Register command
        this.getProxy().getPluginManager().registerCommand(this, new LoggerCommand(this));

        new me.prism3.logger_bungee.utils.ASCIIArt(this);

        // Log Server Start
        if (this.configManager.getConfig().getBoolean("Log-Server.Start", true)) {
            this.logManager.logServerEvent(me.prism3.logger_bungee.utils.Constants.Events.SERVER_START,
                    new java.util.HashMap<>());
        }

        Log.info("LoggerBungee has been enabled!");
    }

    @Override
    public void onDisable() {
        // Log Server Stop
        if (this.logManager != null && this.configManager != null
                && this.configManager.getConfig().getBoolean("Log-Server.Stop", true)) {
            this.logManager.logServerEvent(me.prism3.logger_bungee.utils.Constants.Events.SERVER_STOP,
                    new java.util.HashMap<>());
        }

        if (this.discordManager != null) {
            this.discordManager.shutdown();
        }
        if (this.databaseManager != null) {
            this.databaseManager.shutdown();
        }
        if (this.fileManager != null) {
            this.fileManager.shutdown();
        }

        Log.info("LoggerBungee has been disabled!");
    }

    public void initDatabase() {
        if (this.databaseManager != null) {
            this.databaseManager.shutdown();
            this.databaseManager = null;
        }

        net.md_5.bungee.config.Configuration config = this.configManager.getConfig();
        if (config == null) return;

        boolean dbEnabled = config.getBoolean("Database.Enable", false);
        boolean sqliteEnabled = config.getBoolean("SQLite.Enable", false);

        if (dbEnabled) {
            DatabaseConfig dbConfig = new DatabaseConfig(
                    true,
                    config.getString("Database.Type", "mysql"),
                    config.getString("Database.Host", "localhost"),
                    config.getInt("Database.Port", 3306),
                    config.getString("Database.Database", "logger"),
                    config.getString("Database.Username", "root"),
                    config.getString("Database.Password", ""),
                    config.getString("Database.Table-Prefix", "logger_"),
                    config.getInt("Database.Data-Deletion", 7));
            this.databaseManager = new DatabaseManager(this, dbConfig);
            this.databaseManager.initialize();
        } else if (sqliteEnabled) {
            DatabaseConfig dbConfig = new DatabaseConfig(
                    true,
                    "sqlite",
                    "localhost",
                    3306,
                    "logger",
                    "root",
                    "",
                    config.getString("Database.Table-Prefix", "logger_"),
                    config.getInt("SQLite.Data-Deletion", 7));
            this.databaseManager = new DatabaseManager(this, dbConfig);
            this.databaseManager.initialize();
        }
    }

    public void reload() {
        this.configManager.reload();
        this.messageManager.reload();

        if (this.discordManager != null) {
            this.discordManager.reload();
        }

        this.initDatabase();
    }

    // Getters and Setters
    public static LoggerBungee getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public MessageManager getMessageManager() {
        return this.messageManager;
    }

    public void setDiscordManager(me.prism3.logger_core.discord.DiscordManager discordManager) {
        this.discordManager = discordManager;
    }

    public me.prism3.logger_core.discord.DiscordManager getDiscordManager() {
        return this.discordManager;
    }

    public me.prism3.logger_core.database.DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public LogManager getLogManager() {
        return this.logManager;
    }

    public PermissionManager getPermissionManager() {
        return this.permissionManager;
    }

    @Override
    public void runAsync(Runnable runnable) {
        getProxy().getScheduler().runAsync(this, runnable);
    }

    @Override
    public boolean isDebug() {
        return configManager != null && configManager.getConfig() != null && configManager.getConfig().getBoolean("Debug", false);
    }
}
