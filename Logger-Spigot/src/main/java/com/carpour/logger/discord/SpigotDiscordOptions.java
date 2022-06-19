package com.carpour.logger.discord;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.carour.loggercore.discord.DiscordOptions;

import java.io.File;
import java.io.IOException;

public class SpigotDiscordOptions extends DiscordOptions {

	File configFile;

	public SpigotDiscordOptions(File file) {
		super(file);
		this.configFile = file;
	}

	@Override
	protected void construct() {
		FileConfiguration config = new YamlConfiguration();
		File file = configFile;
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException(e);
		}

		this.setStaffEnabled(config.getBoolean("Staff.Enabled"));
		this.setPlayerChatEnabled(config.getBoolean("Log.Player-Chat"));
		this.setPlayerCommandsEnabled(config.getBoolean("Log.Player-Commands"));
		this.setPlayerCommandsEnabled(config.getBoolean("Log.Player-Commands"));
		this.setConsoleCommandsEnabled(config.getBoolean("Log.Console-Commands"));
		this.setPlayerSignTextEnabled(config.getBoolean("Log.Player-Sign-Text"));
		this.setPlayerJoinEnabled(config.getBoolean("Log.Player-Join"));
		this.setPlayerLeaveEnabled(config.getBoolean("Log.Player-Leave"));
		this.setPlayerKickEnabled(config.getBoolean("Log.Player-Kick"));
		this.setPlayerDeathEnabled(config.getBoolean("Log.Player-Death"));
		this.setPlayerTeleportEnabled(config.getBoolean("Log.Player-Teleport"));
		this.setPlayerLevelEnabled(config.getBoolean("Log.Player-Level"));
		this.setBlockPlaceEnabled(config.getBoolean("Log.Block-Place"));
		this.setBlockBreakEnabled(config.getBoolean("Log.Block-Break"));
		this.setPortalCreateEnabled(config.getBoolean("Log.Portal-Creation"));
		this.setBucketPlaceEnabled(config.getBoolean("Log.Bucket-Place"));
		this.setAnvilUseEnabled(config.getBoolean("Log.Anvil"));
		this.setTpsEnabled(config.getBoolean("Log.TPS"));
		this.setRamEnabled(config.getBoolean("Log.RAM"));
		this.setServerStartEnabled(config.getBoolean("Log.Server-Start"));
		this.setServerStopEnabled(config.getBoolean("Log.Server-Stop"));
		this.setItemDropEnabled(config.getBoolean("Log.Item-Drop"));
		this.setEnchantEnabled(config.getBoolean("Log.Enchant"));
		this.setBookEditEnabled(config.getBoolean("Log.Book-Editing"));
		this.setAfkEnabled(config.getBoolean("Log.AFK"));
		this.setItemPickupEnabled(config.getBoolean("Log.Item-Pickup"));
		this.setFurnaceEnabled(config.getBoolean("Log.Furnace"));

	}

}
