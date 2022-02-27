package com.carpour.loggervelocity.Discord;

import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.Config.Configuration;
import com.carpour.loggervelocity.Utils.Config.ConfigurationProvider;
import com.carpour.loggervelocity.Utils.Config.StringUtils;
import com.carpour.loggervelocity.Utils.Config.YamlConfiguration;
import com.carpour.loggervelocity.Utils.ConfigManager;
import com.google.common.io.ByteStreams;

import java.io.*;

public class DiscordFile {

    private final File file;
    private static Configuration configuration;

    public DiscordFile() {

        final File dataFolder = Main.getInstance().getFolder().toFile();
        this.file = new File(dataFolder, "discord - Velocity.yml");

        try {

            if (!this.file.exists()) {

                if (!dataFolder.exists()) { dataFolder.mkdir(); }

                this.file.createNewFile();

                try (final InputStream is = ConfigManager.class.getResourceAsStream("/discord - Velocity.yml");
                     final OutputStream os = new FileOutputStream(this.file)) {
                    assert is != null;
                    ByteStreams.copy(is, os);
                }
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public File getFile() { return this.file; }

    public static Configuration get(){ return configuration; }

    public boolean getBoolean(final String path) {

        return configuration.get(path) != null && configuration.getBoolean(path);

    }

    public String getString(final String path) {

        if (configuration.get(path) != null) {

            return StringUtils.translateAlternateColorCodes('&', configuration.getString(path));

        } return "String at path: " + path + " not found!";
    }
}
