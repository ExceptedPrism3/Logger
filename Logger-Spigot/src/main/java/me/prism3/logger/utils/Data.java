package me.prism3.logger.utils;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * Data class for the LoggerAPI plugin.
 * <p>
 * This class handles the loading and storing of all configuration data
 * for the LoggerAPI plugin. It provides getters for all settings, including
 * player and server settings, database settings, and file deletion settings.
 */
public class Data {

    private final LoggerAPI plugin;

    // Simple, always‑present settings
    private String pluginPrefix;
    private String serverName;
    private String language;
    private String timeFormatter;
    private String gameModeToLog;
    private String pluginVersion;
    private boolean logToFile;
    private boolean showIP;
    private int levelToLog;
    private long checkerIntervalRAM;
    private long checkerIntervalTPS;
    private long checkerIntervalPlayerCount;
    private int tpsToLog;
    private int ramToLog;
    private int playerCountToLog;
    private List<String> commandsToLog;
    private List<String> itemsToLog;
    private List<String> entitiesToLog;

    // Lazily initialized sub‑objects. Cleared on reload().
    private DatabaseSettings databaseSettings;
    private FileDeletionSettings fileDeletionSettings;

    /**
     * Creates a new Data object.
     * <p>
     * This constructor will automatically call reload() to load the initial state.
     *
     * @param plugin The LoggerAPI plugin instance
     */
    public Data(final LoggerAPI plugin) {
        this.plugin = plugin;
        this.reload();
    }

    /**
     * Sets up the data manager.
     * 
     * @return true if setup was successful
     */
    public boolean setup() {
        return true;
    }

    /**
     * Fully reloads everything from disk.
     * Clears all cached sub‑objects so that getters will rebuild them.
     */
    public void reload() {

        // Always re‐read the raw config
        this.plugin.reloadConfig();
        final FileConfiguration cfg = this.plugin.getConfig();

        // Clear cached objects
        this.databaseSettings = null;
        this.fileDeletionSettings = null;

        // Load primitives
        this.pluginPrefix = cfg.getString("Plugin-Prefix", "&bLogger &8&l|&r ");
        this.serverName = cfg.getString("Server-Name", this.plugin.getServer().getName());
        this.language = cfg.getString("Language", "en_en");
        this.timeFormatter = cfg.getString("Time-Formatter", "yyyy-MM-dd HH:mm:ss");
        this.gameModeToLog = cfg.getString("Game-Mode", "CREATIVE");
        this.logToFile = cfg.getBoolean("Log-to-Files", true);
        this.showIP = cfg.getBoolean("Player-Join-IP", false);
        this.levelToLog = cfg.getInt("Player-Level", 100);
        this.tpsToLog = cfg.getInt("TPS", 15);
        this.ramToLog = cfg.getInt("Used-RAM", 6144);
        this.playerCountToLog = cfg.getInt("Player-Count", 5);
        this.checkerIntervalRAM = cfg.getLong("Checker-Interval.RAM", 5) * 60 * 20;
        this.checkerIntervalTPS = cfg.getLong("Checker-Interval.TPS", 5) * 60 * 20;
        this.checkerIntervalPlayerCount = cfg.getLong("Checker-Interval.Player-Count", 5) * 60 * 20;

        // Commands list — must exist
        this.commandsToLog = cfg.getStringList("Player-Commands.Commands-to-Log");
        this.itemsToLog = cfg.getStringList("Crafted-Items");
        this.entitiesToLog = cfg.getStringList("Entities");

        // Always refresh pluginVersion
        this.pluginVersion = this.plugin.getDescription().getVersion();
    }

    // Simple getters:
    public String getPluginPrefix() {
        return this.pluginPrefix;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getTimeFormatter() {
        return this.timeFormatter;
    }

    public boolean isLogToFile() {
        return this.logToFile;
    }

    public boolean isShowIP() {
        return this.showIP;
    }

    public int getLevelToLog() {
        return this.levelToLog;
    }

    public int getTpsToLog() {
        return this.tpsToLog;
    }

    public int getPlayerCountToLog() {
        return this.playerCountToLog;
    }

    public List<String> getCommandsToLog() {
        return Collections.unmodifiableList(this.commandsToLog);
    }

    public List<String> getItemsToLog() {
        return Collections.unmodifiableList(itemsToLog);
    }

    public List<String> getEntitiesToLog() {
        return Collections.unmodifiableList(entitiesToLog);
    }

    public String getPluginVersion() {
        return this.pluginVersion;
    }

    public String getGameModeToLog() {
        return this.gameModeToLog;
    }

    public long getCheckerIntervalRAM() {
        return this.checkerIntervalRAM;
    }

    public long getCheckerIntervalTPS() {
        return this.checkerIntervalTPS;
    }

    public long getCheckerIntervalPlayerCount() {
        return this.checkerIntervalPlayerCount;
    }

    public int getRamToLog() {
        return this.ramToLog;
    }

    /** Lazy get; will recreate after reload(). */
    public DatabaseSettings getDatabaseSettings() {

        if (this.databaseSettings == null) {
            this.databaseSettings = new DatabaseSettings(this.plugin.getConfig());
        }

        return this.databaseSettings;
    }

    /** Lazy get; will recreate after reload(). */
    public FileDeletionSettings getFileDeletionSettings() {

        if (this.fileDeletionSettings == null) {
            this.fileDeletionSettings = new FileDeletionSettings(this.plugin.getConfig());
        }

        return this.fileDeletionSettings;
    }

    /** Database connection settings */
    public static class DatabaseSettings {

        public final boolean enabled;
        public final String type, host, name, username, password, tablePrefix;
        public final int port, dataDeletion;

        public DatabaseSettings(final FileConfiguration cfg) {
            this.enabled = cfg.getBoolean("Database.Enable", false);
            this.type = cfg.getString("Database.Type", "mysql");
            this.host = cfg.getString("Database.Host", "localhost");
            this.port = cfg.getInt("Database.Port", 3306);
            this.name = cfg.getString("Database.Database", "logger");
            this.username = cfg.getString("Database.Username", "root");
            this.password = cfg.getString("Database.Password", "meow");
            this.tablePrefix = cfg.getString("Database.Table-Prefix", "logger_");
            this.dataDeletion = cfg.getInt("Database.Data-Deletion", 7);
        }
    }

    /** File deletion settings */
    public static class FileDeletionSettings {

        public final int days;

        public FileDeletionSettings(final FileConfiguration cfg) {
            this.days = cfg.getInt("File-Deletion", 7);
        }
    }

    /**
     * Universal “is logging of this type turned on?” check.
     * Delegates to the config path embedded in the LogType enum.
     */
    public boolean isEnabled(final LogType type) {
        return plugin.getConfig().getBoolean(type.getConfigPath());
    }

    private DiscordSettings discordSettings;

    public DiscordSettings getDiscordSettings() {
        if (this.discordSettings == null) {
            java.io.File file = new java.io.File(plugin.getDataFolder(), "discord.yml");
            if (!file.exists()) {
                plugin.saveResource("discord.yml", false);
            }
            FileConfiguration discordCfg = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(file);
            this.discordSettings = new DiscordSettings(discordCfg);
        }
        return this.discordSettings;
    }

    public static class DiscordSettings {
        public final boolean enabled;
        public final String token, activity, messageType, embedTitle, embedFooter;
        public final java.awt.Color embedColor;
        public final boolean embedTimestamp;
        public final Map<String, String> channels;

        public DiscordSettings(FileConfiguration cfg) {
            this.enabled = cfg.getBoolean("Discord.Enabled", false);
            this.token = cfg.getString("Discord.Bot-Token", "");
            this.activity = cfg.getString("ActivityCycling.Activities", "Playing Minecraft"); // Just a placeholder,
                                                                                              // logic for lists is
                                                                                              // complex
            this.messageType = cfg.getString("Message-Type", "normal");
            this.embedTitle = cfg.getString("Embed-Settings.Title", "Server Notification");
            this.embedFooter = cfg.getString("Embed-Settings.Footer", "Sent by Logger");
            this.embedTimestamp = cfg.getBoolean("Embed-Settings.Timestamp", true);
            this.embedColor = java.awt.Color.decode(cfg.getString("Embed-Settings.Color", "#FF5733"));

            this.channels = new HashMap<>();

            String mode = cfg.getString("Discord.Mode", "BOT").toUpperCase();

            for (LogType type : LogType.values()) {
                String path = type.getDiscordPath();
                if (path == null)
                    continue;

                String val = null;
                if ("BOT".equals(mode)) {
                    val = cfg.getString(path + ".Channel-ID");
                } else {
                    val = cfg.getString(path + ".Webhook");
                }

                if (val != null && !val.isEmpty() && !val.equals("CHANNEL_ID")
                        && !val.startsWith("https://discord.com/api/webhooks/XXXX")) {
                    this.channels.put(type.name(), val);
                }
            }
        }
    }
}
