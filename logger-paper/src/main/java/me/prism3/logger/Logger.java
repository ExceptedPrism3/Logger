package me.prism3.logger;

import me.prism3.logger.events.server.StopListener;
import me.prism3.logger.managers.*;
import me.prism3.logger.utils.ASCIIArt;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileLogger;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Logger extends LoggerAPI {

    private Data data;
    private FileLogger fileLogger;
    private MessageManager messageManager;
    private LoggerManager loggerManager;

    private static Logger plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Log.setup(getLogger());
        LoggerAPI.setInstance(this); // Set the singleton instance

        // Plugin startup logic
        this.saveDefaultConfig();
        this.data = new Data(this);
        if (!this.data.setup()) {
            getLogger().severe("Could not setup data management! Plugin disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load Version Adapter
        loadVersionAdapter();

        // Initialize file logger (always needed if file logging is enabled)
        this.fileLogger = new FileLogger(this);

        // Initialize MessageManager
        this.messageManager = new MessageManager(this);
        this.getCommand("logger").setExecutor(new CommandManager(this));

        // Initialize database manager only if logToDatabase is enabled
        if (this.data.getDatabaseSettings().enabled) {
            me.prism3.logger.utils.Data.DatabaseSettings db = this.data.getDatabaseSettings();
            me.prism3.logger_core.database.DatabaseConfig dbConfig = new me.prism3.logger_core.database.DatabaseConfig(
                    db.enabled, db.type, db.host, db.port, db.name, db.username, db.password, db.tablePrefix,
                    db.dataDeletion);
            setDatabaseManager(new me.prism3.logger_core.database.DatabaseManager(this, dbConfig));
            getDatabaseManager().initialize();
        }

        this.loggerManager = new LoggerManager(this);

        // Register events dynamically based on configuration
        EventManager.registerEvents(this);

        new ASCIIArt(this);

        getLogger().info("Logger has been enabled!");
    }

    private void loadVersionAdapter() {
        String version = getServer().getBukkitVersion();
        getLogger().info("Detected server version: " + version);

        if (isSupportedVersion(version)) {
            try {
                Class<?> clazz = Class.forName("me.prism3.logger.v1_21.VersionAdapterImpl");
                me.prism3.logger.utils.VersionAdapter adapter = (me.prism3.logger.utils.VersionAdapter) clazz
                        .newInstance();
                adapter.registerListeners(this);
                getLogger().info("Successfully loaded 1.21+ adapter.");
            } catch (Exception e) {
                getLogger().warning("Could not load 1.21+ adapter: " + e.getMessage());
            }
        }
    }

    private boolean isSupportedVersion(String version) {
        if (version == null) return false;
        if (version.contains("1.21") || version.contains("1.22") || version.contains("1.23") || version.contains("26.")) {
            return true;
        }
        try {
            String cleanVersion = version.split("-")[0];
            String[] parts = cleanVersion.split("\\.");
            if (parts.length > 0) {
                int first = Integer.parseInt(parts[0]);
                if (first > 1) {
                    return true;
                }
                if (first == 1 && parts.length > 1) {
                    int second = Integer.parseInt(parts[1]);
                    return second >= 21;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    @Override
    public void onDisable() {
        if (this.data != null && this.data.isEnabled(LogType.SERVER_STOP)) {
            new StopListener(this);
        }
        getLogger().info("Logger has been disabled!");
        if (getDatabaseManager() != null)
            getDatabaseManager().shutdown();
        if (this.loggerManager != null)
            this.loggerManager.shutdown();
        me.prism3.logger.utils.SchedulerAdapter.cancelAllTasks(this);
    }

    public static Player[] getOnlinePlayers() {
        return plugin.getServer().getOnlinePlayers().toArray(new Player[0]);
    }

    public static Logger getPlugin() {
        return plugin;
    }

    @Override
    public Data getData() {
        return this.data;
    }

    @Override
    public MessageManager getMessageManager() {
        return this.messageManager;
    }

    @Override
    public LoggerManager getLoggerManager() {
        return this.loggerManager;
    }

    @Override
    public FileLogger getFileLogger() {
        return this.fileLogger;
    }

    @Override
    public boolean isLogToFile() {
        return this.data.isLogToFile();
    }

}
