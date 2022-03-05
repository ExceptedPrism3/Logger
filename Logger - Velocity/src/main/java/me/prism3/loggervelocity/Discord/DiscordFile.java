package me.prism3.loggervelocity.Discord;

import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.Utils.Config.Configuration;
import me.prism3.loggervelocity.Utils.Config.ConfigurationProvider;
import me.prism3.loggervelocity.Utils.Config.StringUtils;
import me.prism3.loggervelocity.Utils.Config.YamlConfiguration;
import me.prism3.loggervelocity.Utils.ConfigManager;
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
