package me.prism3.logger_bungee.managers;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Log;
import me.prism3.logger_bungee.utils.YamlMigrator;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {

    private final LoggerBungee plugin;
    private Configuration config;

    public ConfigManager(LoggerBungee plugin) {
        this.plugin = plugin;
        this.loadConfig();
    }

    public void loadConfig() {
        try {
            loadResource("config.yml", "bungee-config.yml");
            loadResource("discord.yml", "bungee-discord.yml");
        } catch (IOException e) {
            Log.severe("Failed to load config files: " + e.getMessage());
        }
    }

    private void loadResource(String filename, String altResourceName) throws IOException {
        File file = new File(this.plugin.getDataFolder(), filename);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            InputStream in = this.plugin.getResourceAsStream(altResourceName);
            if (in == null) in = this.plugin.getResourceAsStream(filename);

            if (in != null) {
                try (InputStream input = in) {
                    Files.copy(input, file.toPath());
                }
            } else {
                file.createNewFile();
            }
        } else {
            // Auto-sync missing config keys non-destructively from proxy template only
            InputStream in = this.plugin.getResourceAsStream(altResourceName);
            if (in != null) {
                YamlMigrator.syncDefaults(this.plugin, file, altResourceName);
            } else {
                YamlMigrator.syncDefaults(this.plugin, file, filename);
            }
        }

        if (filename.equals("config.yml")) {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }
    }

    public Configuration getDiscordConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File(this.plugin.getDataFolder(), "discord.yml"));
        } catch (IOException e) {
            return null;
        }
    }

    public void saveConfig() {
        try {
            if (this.config != null) {
                ConfigurationProvider.getProvider(YamlConfiguration.class)
                        .save(this.config, new File(this.plugin.getDataFolder(), "config.yml"));
            }
        } catch (IOException e) {
            Log.severe("Failed to save config.yml: " + e.getMessage());
        }
    }

    public String getServerName() {
        if (this.config != null) {
            return this.config.getString("Server-Name", "BungeeCord");
        }
        return "BungeeCord";
    }

    public Configuration getConfig() {
        return this.config;
    }

    public void reload() {
        this.loadConfig();
    }
}
