package me.prism3.logger.utils;

import me.prism3.logger.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUpdater {

    public FileUpdater(final File dataFolder) { this.updater(dataFolder); }

    private void updater(final File dataFolder) {

        // Config File
        final File configFile = new File(dataFolder + File.separator + "config.yml");
        final YamlConfiguration externalYamlConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStreamReader defConfigStream = new InputStreamReader(Main.getInstance().getResource("config.yml"), StandardCharsets.UTF_8);
        final YamlConfiguration internalConfigFile = YamlConfiguration.loadConfiguration(defConfigStream);

        // Discord File
        final File discordFile = new File(dataFolder + File.separator + "discord.yml");
        final YamlConfiguration externalYamlDiscord = YamlConfiguration.loadConfiguration(discordFile);

        final InputStreamReader defDiscordStream = new InputStreamReader(Main.getInstance().getResource("discord.yml"), StandardCharsets.UTF_8);
        final YamlConfiguration internalDiscordFile = YamlConfiguration.loadConfiguration(defDiscordStream);

        // Gets all the keys inside the internal file and iterates through all of it's key pairs
        for (String config : internalConfigFile.getKeys(true))
            // Checks if the external file contains the key already.
            if (!externalYamlConfig.contains(config))
                // If it doesn't contain the key, we set the key based off what was found inside the plugin jar
                externalYamlConfig.set(config, internalConfigFile.get(config));

        for (String discord : internalDiscordFile.getKeys(true))
            if (!externalYamlDiscord.contains(discord))
                externalYamlDiscord.set(discord, internalDiscordFile.get(discord));

        try {
            externalYamlConfig.save(configFile);

            externalYamlDiscord.save(discordFile);
        } catch (final IOException io) { io.printStackTrace(); }
    }
}
