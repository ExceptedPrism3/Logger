package me.prism3.logger.utils.manager;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.config.Config;
import me.prism3.logger.utils.updater.FileUpdater;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

import static me.prism3.logger.utils.Data.configVersion;

public class DiscordManager {

    private final Main plugin;
    private final File discordFile;
    private Config discord;

    public DiscordManager(final Main plugin) {
        this.plugin = plugin;
        this.discordFile = new File(plugin.getDataFolder(), "discord.yml");
        this.loadConfig();
    }

    private void loadConfig() {
        this.checkConfig();
        this.initConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.updateChecker();
            }
        }.runTaskTimerAsynchronously(this.plugin, 0, 360000);
    }

    private void checkConfig() {

        if (!this.discordFile.exists())
            return;

        this.plugin.reloadConfig();
        final Configuration defaults = this.plugin.getConfig().getDefaults();
        final int oldVersion = configVersion;
        final int currentVersion = defaults != null ? defaults.getInt("Version") : 0;

        if (oldVersion == 0) {
            this.resetConfig();
            return;
        }

        if (oldVersion < currentVersion) {
            try {
                FileUpdater.update(this.plugin, "discord.yml", this.discordFile, Collections.singletonList("Version"));
                Log.warning("Config file updated from version " + oldVersion + " to version " + currentVersion);
                Log.warning("Checking the config file and adjusting the new settings is highly recommended");
            } catch (final IOException e) {
                Log.severe("Error reading the config file!");
                resetConfig();
            }
        }
    }

    private void initConfig() {
        try {
            this.discord = new Config(this.plugin, "discord.yml");
        } catch (final FileNotFoundException e) { Log.severe("Discord file not found", e); }
    }

    private void resetConfig() {

        try {
            Files.move(this.discordFile.toPath(), this.discordFile.toPath().resolveSibling("discord.old.yml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) { Log.severe("Error resetting discord file"); }

        this.initConfig();
        Log.warning("Due to an error reading the discord file, it was reset to default settings");
        Log.warning("This was likely caused by a mistake while you changed settings, like an extra space or missing quotes");
        Log.warning("The broken discord file was renamed to discord.old.yml, you can copy your old settings manually if you need them");
    }

    public final FileConfiguration getDiscord() {
        return this.discord;
    }
}
