package com.carpour.logger.logging;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.carour.loggercore.logging.LoggingOptions;

import java.io.File;
import java.io.IOException;

public class SpigotLoggingOptions extends LoggingOptions {

	File configFile;

	public SpigotLoggingOptions(File configFile) {
		super(configFile);
		this.configFile = configFile;
	}

	@Override protected void construct() {
		FileConfiguration config = new YamlConfiguration();
		File file = configFile;
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException(e);
		}




	}
}
