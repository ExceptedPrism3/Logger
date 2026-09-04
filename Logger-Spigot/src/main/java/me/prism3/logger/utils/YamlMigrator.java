package me.prism3.logger.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class YamlMigrator {

    public static boolean syncDefaults(Plugin plugin, File diskFile, String resourcePath) {
        if (diskFile == null || !diskFile.exists()) return false;

        InputStream stream = plugin.getResource(resourcePath);
        if (stream == null && resourcePath.startsWith("/")) {
            stream = plugin.getResource(resourcePath.substring(1));
        }
        if (stream == null) return false;

        try {
            YamlConfiguration diskConfig = YamlConfiguration.loadConfiguration(diskFile);
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));

            boolean updated = copyMissingKeys(defaultConfig, diskConfig);
            if (updated) {
                diskConfig.save(diskFile);
                Log.info("Automatically synchronized missing entries in " + diskFile.getName() + " from latest version.");
                return true;
            }
        } catch (Exception e) {
            Log.warning("Failed to sync defaults for " + diskFile.getName() + ": " + e.getMessage());
        }
        return false;
    }

    private static boolean copyMissingKeys(ConfigurationSection source, ConfigurationSection target) {
        boolean modified = false;
        for (String key : source.getKeys(false)) {
            if (source.isConfigurationSection(key)) {
                ConfigurationSection srcSub = source.getConfigurationSection(key);
                ConfigurationSection tgtSub = target.getConfigurationSection(key);
                if (tgtSub == null) {
                    tgtSub = target.createSection(key);
                    modified = true;
                }
                if (copyMissingKeys(srcSub, tgtSub)) {
                    modified = true;
                }
            } else {
                if (!target.contains(key)) {
                    target.set(key, source.get(key));
                    modified = true;
                }
            }
        }
        return modified;
    }
}
