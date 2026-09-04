package me.prism3.logger_discord_addon.config;

import me.prism3.logger_discord_addon.LoggerDiscordAddon;
import me.prism3.logger_discord_addon.utils.Log;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Manages the configuration for the Logger Discord Addon.
 * Handles loading, saving, and updating configuration files.
 */
public class ConfigManager {

    private final LoggerDiscordAddon addon;
    private FileConfiguration config;
    private File configFile;
    private final String CONFIG_VERSION;

    /**
     * Constructs a new ConfigManager for the specified addon.
     *
     * @param addon The LoggerDiscordAddon instance.
     */
    public ConfigManager(LoggerDiscordAddon addon) {
        this.addon = addon;
        this.CONFIG_VERSION = this.addon.getDescription().getVersion();
    }

    /**
     * Loads and updates the configuration file.
     */
    public void loadConfig() {

        // Use the main Logger plugin's data folder
        this.configFile = new File(this.addon.getLoggerPlugin().getDataFolder(), "discord.yml");
        
        if (!this.configFile.exists())
            this.saveDefaultConfig();

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.updateConfig();
    }

    /**
     * Updates the configuration file if needed.
     */
    private void updateConfig() {

        final String currentVersion = this.config.getString("Discord-Version", "0.0");
        
        if (!currentVersion.equals(this.CONFIG_VERSION)) {

            // Backup old config
            final File backupFile = new File(this.addon.getLoggerPlugin().getDataFolder(), "discord.yml.backup");
            
            if (this.configFile.exists())
                this.configFile.renameTo(backupFile);
            
            // Save new config
            this.saveDefaultConfig();
            this.config = YamlConfiguration.loadConfiguration(this.configFile);
            
            Log.info("Configuration updated to version " + this.CONFIG_VERSION);
        }
    }

    /**
     * Saves the default configuration file.
     */
    public void saveDefaultConfig() {

        if (!this.configFile.exists()) {

            this.configFile.getParentFile().mkdirs();
            
            // Copy the default config from the addon's resources
            try (final InputStream in = this.addon.getResource("discord.yml");
                 final InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {

                final YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);

                // Set the actual version
                defaultConfig.set("Discord-Version", this.CONFIG_VERSION);
                defaultConfig.save(this.configFile);
            } catch (final Exception e) {
                Log.severe("Could not save default config: " + e.getMessage());
            }
        }
    }

    /**
     * Reloads the configuration.
     */
    public void reloadConfig() { this.loadConfig(); }

    /**
     * Returns the current configuration.
     *
     * @return The FileConfiguration instance.
     */
    public FileConfiguration getConfig() { return this.config; }
}
