package me.prism3.logger.utils;

import me.prism3.logger.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FileUpdater {

    public FileUpdater(final File dataFolder) {
        this.updateYamlConfig(dataFolder, "config.yml");
        this.updateYamlConfig(dataFolder, "discord.yml");
    }

    private void updateYamlConfig(final File dataFolder, final String fileName) {

        final File configFile = new File(dataFolder + File.separator + fileName);
        final YamlConfiguration externalYamlConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStreamReader defConfigStream = new InputStreamReader(
                Objects.requireNonNull(Main.getInstance().getResource(fileName)), StandardCharsets.UTF_8);

        final YamlConfiguration internalConfigFile = YamlConfiguration.loadConfiguration(defConfigStream);

        for (String config : internalConfigFile.getKeys(true)) {
            if (!externalYamlConfig.contains(config)) {
                externalYamlConfig.set(config, internalConfigFile.get(config));
            }
        }

        try {
            externalYamlConfig.save(configFile);
        } catch (final IOException io) { io.printStackTrace(); }
    }
}
