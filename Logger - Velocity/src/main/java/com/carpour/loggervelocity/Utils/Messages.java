package com.carpour.loggervelocity.Utils;

import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.Config.Configuration;
import com.carpour.loggervelocity.Utils.Config.ConfigurationProvider;
import com.carpour.loggervelocity.Utils.Config.StringUtils;
import com.carpour.loggervelocity.Utils.Config.YamlConfiguration;
import com.google.common.io.ByteStreams;

import java.io.*;

public class Messages {

    private File file;
    private Configuration configuration;

    private final File dataFolder = Main.getInstance().getFolder().toFile();

    public Messages() {

        this.file = new File(dataFolder, "messages - Velocity.yml");

        try {

            if (!this.file.exists()) {

                if (!dataFolder.exists()) { dataFolder.mkdir(); }

                this.file.createNewFile();

                try (final InputStream is = ConfigManager.class.getResourceAsStream("/messages - Velocity.yml");
                     final OutputStream os = new FileOutputStream(this.file)) {
                    assert is != null;
                    ByteStreams.copy(is, os);
                }
            }
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void reload(){ load(); }

    private void load() {

        this.file = new File(dataFolder, "messages - Velocity.yml");

        try {

            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        }

        catch (IOException e) { e.printStackTrace(); }
    }

    public File getFile() {
        return this.file;
    }

    public String getString(final String path) {

        if (this.configuration.get(path) != null) {

            return StringUtils.translateAlternateColorCodes('&', this.configuration.getString(path));

        } return "String at path: " + path + " not found!";
    }
}
