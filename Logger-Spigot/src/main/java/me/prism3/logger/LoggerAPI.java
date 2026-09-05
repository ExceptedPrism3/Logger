package me.prism3.logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import me.prism3.logger.managers.MessageManager;
import me.prism3.logger.managers.LoggerManager;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileLogger;

public abstract class LoggerAPI extends JavaPlugin implements me.prism3.logger_core.platform.LoggerPlatform {
    public abstract Data getData();

    public abstract MessageManager getMessageManager();

    public abstract LoggerManager getLoggerManager();

    private me.prism3.logger_core.database.DatabaseManager databaseManager;

    public void setDatabaseManager(me.prism3.logger_core.database.DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public me.prism3.logger_core.database.DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public abstract FileLogger getFileLogger();

    public abstract boolean isLogToFile();

    private me.prism3.logger_core.discord.DiscordManager discordManager;

    public void setDiscordManager(me.prism3.logger_core.discord.DiscordManager discordManager) {
        this.discordManager = discordManager;
        if (this.databaseManager != null) {
            String serverName = getData() != null ? getData().getServerName() : "default";
            String version = getDescription() != null ? getDescription().getVersion() : "1.8.4";
            boolean isDiscordActive = discordManager != null && discordManager.isEnabled();
            this.databaseManager.updateServerStatus(serverName, version, isDiscordActive);
        }
    }

    public me.prism3.logger_core.discord.DiscordManager getDiscordManager() {
        return this.discordManager;
    }

    private static LoggerAPI instance;

    public static LoggerAPI getInstance() {
        return instance;
    }

    protected static void setInstance(LoggerAPI api) {
        instance = api;
    }

    @Override
    public void runAsync(Runnable runnable) {
        me.prism3.logger.utils.SchedulerAdapter.runAsync(this, runnable);
    }

    @Override
    public boolean isDebug() {
        return getConfig().getBoolean("Debug", false);
    }
}
