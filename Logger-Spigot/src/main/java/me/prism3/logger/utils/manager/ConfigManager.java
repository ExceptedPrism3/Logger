package me.prism3.logger.utils.manager;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.updater.FileUpdater;
import org.bukkit.configuration.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

import static me.prism3.logger.utils.Data.configVersion;

public class ConfigManager {

	private final Main plugin;
	private final File configFile;
	private final String configFileName = "config.yml";

	public ConfigManager(final Main plugin) {
		this.plugin = plugin;
		this.configFile = new File(plugin.getDataFolder(), configFileName);
		this.loadConfig();
	}

	private void loadConfig() {
		this.checkConfig();
		this.initConfig();
	}

	private void checkConfig() {

		this.plugin.getExecutor().submit(() -> {

			if (!this.configFile.exists())
				return;

			this.plugin.reloadConfig();
			final Configuration defaults = this.plugin.getConfig().getDefaults();
			final String currentVersion = configVersion;
			final String jarVersion = defaults != null ? defaults.getString("Config-Version") : "";

			if (currentVersion == null || currentVersion.isEmpty()) {
				this.resetConfig();
				return;
			}

			if (this.compareVersions(currentVersion, jarVersion) < 0) {
				try {
					FileUpdater.update(this.plugin, configFileName, this.configFile, Collections.singletonList("Config-Version"));
					Log.warning("FileInitializer file updated from version " + currentVersion + " to version " + jarVersion);
				} catch (final IOException e) {
					Log.severe("Error reading the config file, if the issue persists contact the authors!");
					this.resetConfig();
				}
			}
		});
	}

	private void initConfig() { new FileInitializer(this.plugin, configFileName); }

	private void resetConfig() {

		this.plugin.getExecutor().submit(() -> {

			try {

				Files.move(this.configFile.toPath(), this.configFile.toPath().resolveSibling("config.old.yml"), StandardCopyOption.REPLACE_EXISTING);

				this.initConfig();
				Log.warning("Due to an error reading the config, it was reset to default settings");
				Log.warning("This was likely caused by a mistake while you changed settings, like an extra space or missing quotes");
				Log.warning("The broken config was renamed to config.old.yml, you can copy your old settings manually if you need them");

			} catch (final IOException e) { Log.severe("Error resetting the config file"); }
		});
	}

	private int compareVersions(String current, String latest) {

		final String[] currentParts = current.split("\\.");
		final String[] latestParts = latest.split("\\.");

		for (int i = 0; i < Math.max(currentParts.length, latestParts.length); i++) {

			int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
			int latestPart = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;

			if (currentPart != latestPart)
				return currentPart - latestPart;
		}
		return 0;
	}
}
