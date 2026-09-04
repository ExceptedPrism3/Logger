package me.prism3.loggervelocity.utils;

import me.prism3.loggervelocity.Logger;
import me.prism3.loggervelocity.utils.config.Configuration;
import me.prism3.loggervelocity.utils.config.ConfigurationProvider;
import me.prism3.loggervelocity.utils.config.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class YamlMigrator {

    public static boolean syncDefaults(File diskFile, String resourcePath) {
        if (diskFile == null || !diskFile.exists()) return false;

        String path = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
        InputStream stream = YamlMigrator.class.getResourceAsStream(path);
        if (stream == null && path.startsWith("/")) {
            stream = YamlMigrator.class.getResourceAsStream(path.substring(1));
        }
        if (stream == null) return false;

        try {
            Configuration diskConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(diskFile);
            Configuration defaultConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new InputStreamReader(stream, StandardCharsets.UTF_8));

            boolean updated = copyMissingKeys(defaultConfig, diskConfig);
            if (updated) {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(diskConfig, diskFile);
                if (Logger.getInstance() != null && Logger.getInstance().getLogger() != null) {
                    Logger.getInstance().getLogger().info("Automatically synchronized missing entries in " + diskFile.getName() + " from latest version.");
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean copyMissingKeys(Configuration source, Configuration target) {
        boolean modified = false;
        for (String key : source.getKeys()) {
            Object srcVal = source.get(key);
            if (srcVal instanceof Configuration) {
                Object tgtVal = target.get(key);
                Configuration tgtSub;
                if (!(tgtVal instanceof Configuration)) {
                    tgtSub = new Configuration(target);
                    target.set(key, tgtSub);
                    modified = true;
                } else {
                    tgtSub = (Configuration) tgtVal;
                }
                if (copyMissingKeys((Configuration) srcVal, tgtSub)) {
                    modified = true;
                }
            } else {
                if (target.get(key) == null) {
                    target.set(key, srcVal);
                    modified = true;
                }
            }
        }
        return modified;
    }
}
