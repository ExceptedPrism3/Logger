package me.prism3.logger.utils;

import me.prism3.logger.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class FileUpdater {

    public FileUpdater(final File dataFolder) {
//        this.updater(dataFolder);
    }

    private void updater(final File dataFolder) {

        // Discord File
        final File discordFile = new File(dataFolder + File.separator + "discord.yml");
        final YamlConfiguration externalYamlDiscord = YamlConfiguration.loadConfiguration(discordFile);

        final InputStreamReader defDiscordStream = new InputStreamReader(Main.getInstance().getResource("discord.yml"), StandardCharsets.UTF_8);
        final YamlConfiguration internalDiscordFile = YamlConfiguration.loadConfiguration(defDiscordStream);

        // Gets all the keys inside the internal file and iterates through all of it's key pairs
        for (String discord : internalDiscordFile.getKeys(true)) {
            // Checks if the external file contains the key already.
            if (!externalYamlDiscord.contains(discord)) {
                // If it doesn't contain the key, we set the key based off what was found inside the plugin jar
                externalYamlDiscord.set(discord, internalDiscordFile.get(discord));
            }
        }

        try {
            externalYamlDiscord.save(discordFile);
        } catch (final IOException io) { io.printStackTrace(); }
    }
}
