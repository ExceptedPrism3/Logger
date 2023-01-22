package me.prism3.logger.utils.manager;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.config.Config;
import me.prism3.logger.utils.updater.FileUpdater;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

public class DiscordManager {

    private final Main plugin;
    private final File discordFile;
    private Config discord;

    public DiscordManager(final Main plugin) {
        this.plugin = plugin;
        this.discordFile = new File(plugin.getDataFolder(), "discord.yml");
        this.loadDiscord();
    }

    private void loadDiscord() {
        this.checkDiscord();
        this.initDiscord();
    }

    private void checkDiscord() {

        if (!this.discordFile.exists())
            return;

        final int oldVersion = plugin.getConfig().getInt("Discord-Version", 0);
        final int currentVersion = plugin.getConfig().getDefaults().getInt("Discord-Version");

        if (oldVersion == 0) {
            this.resetDiscord();
            return;
        }

        if (oldVersion < currentVersion) {
            try {
                FileUpdater.update(this.plugin, "discord.yml", this.discordFile, Collections.singletonList("Discord-Version"));
                Log.warning("Discord file updated from version " + oldVersion + " to version " + currentVersion);
                Log.warning("Checking the discord file and adjusting the new settings is highly recommended");
            } catch (final IOException e) {
                Log.severe("Error reading the discord file!");
                this.resetDiscord();
            }
        }
    }

    private void initDiscord() {
        try {
            this.discord = new Config(this.plugin, "discord.yml");
        } catch (final FileNotFoundException e) { Log.severe("Discord file not found", e); }
    }

    private void resetDiscord() {

        try {
            Files.move(this.discordFile.toPath(), this.discordFile.toPath().resolveSibling("discord.old.yml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) { Log.severe("Error resetting discord file"); }

        this.initDiscord();
        Log.warning("Due to an error reading the discord file, it was reset to default settings");
        Log.warning("This was likely caused by a mistake while you changed settings, like an extra space or missing quotes");
        Log.warning("The broken discord file was renamed to discord.old.yml, you can copy your old settings manually if you need them");
    }

    public final FileConfiguration get() {
        return this.discord;
    }
}
