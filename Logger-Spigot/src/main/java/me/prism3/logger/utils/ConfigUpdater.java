package me.prism3.logger.utils;

import me.prism3.logger.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ConfigUpdater {

    public ConfigUpdater(final File dataFolder) {

        final File langFile = new File(dataFolder + "/config.yml");
        final YamlConfiguration externalYamlConfig = YamlConfiguration.loadConfiguration(langFile);

        final InputStreamReader defConfigStream = new InputStreamReader(Objects.requireNonNull(Main.getInstance().getResource("config.yml")), StandardCharsets.UTF_8);
        final YamlConfiguration internalLangConfig = YamlConfiguration.loadConfiguration(defConfigStream);

        // Gets all the keys inside the internal file and iterates through all of it's key pairs
        for (String string : internalLangConfig.getKeys(true)) {
            // Checks if the external file contains the key already.
            if (!externalYamlConfig.contains(string)) {
                // If it doesn't contain the key, we set the key based off what was found inside the plugin jar
                externalYamlConfig.set(string, internalLangConfig.get(string));
            }
        }
        try {
            externalYamlConfig.save(langFile);
        } catch (final IOException io) { io.printStackTrace(); }
    }
}
