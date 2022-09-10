package me.prism3.logger.utils.manager;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.config.Config;
import me.prism3.logger.utils.updater.FileUpdater;
import org.bukkit.configuration.Configuration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

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
        final int currentVersion = defaults != null ? defaults.getInt("Config-Version") : 0;

        if (oldVersion == 0) {
            this.resetConfig();
            return;
        }

        if (oldVersion < currentVersion) {
            try {
                FileUpdater.update(this.plugin, "discord.yml", this.discordFile, Arrays.asList("Config-Version", "Updater.Checker"));
                Log.warning("Config file updated from version " + oldVersion + " to version " + currentVersion);
                Log.warning("Checking the config file and adjusting the new settings is highly recommended");
//				Messages.queueAdminMsg(Messages.PREFIXMSG + " §aConfiguration updated from version §c" + oldVersion + " §ato §c" + currentVersion);
//				Messages.queueAdminMsg(Messages.PREFIXMSG + " §aChecking the config file and adjusting the new settings is highly recommended");
            } catch (final IOException e) {
                Log.severe("Error reading the config file!");
                resetConfig();
            }
        }
    }

    private void initConfig() {
        try {
            this.discord = new Config(this.plugin, "discord.yml");
        } catch (final FileNotFoundException e) {
            Log.severe("Config file not found", e);
        }
    }

    private void resetConfig() {

        try {
            Files.move(this.discordFile.toPath(), this.discordFile.toPath().resolveSibling("config.old.yml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            Log.severe("Error resetting config file");
        }

        this.initConfig();
        Log.warning("Due to an error reading the config, it was reset to default settings");
        Log.warning("This was likely caused by a mistake while you changed settings, like an extra space or missing quotes");
        Log.warning("The broken config was renamed to config.old.yml, you can copy your old settings manually if you need them");
//		Messages.queueAdminMsg(Messages.PREFIXMSG + " §cDue to an error reading the config, it was reset to default settings"
//		        + "\n§cThis was likely caused by a mistake while you changed settings, like an extra space or missing quotes");
//		Messages.queueAdminMsg(Messages.PREFIXMSG + "§cThe broken config was renamed to config.old.yml, you can copy your old settings manually if you need them");
    }
}
