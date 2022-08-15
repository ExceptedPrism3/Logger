package me.prism3.loggervelocity.discord;

import com.google.common.io.ByteStreams;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.utils.ConfigManager;
import me.prism3.loggervelocity.utils.config.Configuration;
import me.prism3.loggervelocity.utils.config.ConfigurationProvider;
import me.prism3.loggervelocity.utils.config.StringUtils;
import me.prism3.loggervelocity.utils.config.YamlConfiguration;

import java.io.*;

public class DiscordFile {

    private final File file;
    private Configuration configuration;

    public DiscordFile() {

        final File dataFolder = Main.getInstance().getFolder().toFile();
        this.file = new File(dataFolder, "discord - Velocity.yml");

        try {

            if (!this.file.exists()) {

                if (!dataFolder.exists()) dataFolder.mkdir();

                this.file.createNewFile();

                try (final InputStream is = ConfigManager.class.getResourceAsStream("/discord - Velocity.yml");
                     final OutputStream os = new FileOutputStream(this.file)) {
                    assert is != null;
                    ByteStreams.copy(is, os);
                }
            }
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public File getFile() { return this.file; }

    public Configuration get() { return this.configuration; }

    public boolean getBoolean(final String path) {

        return this.configuration.get(path) != null && this.configuration.getBoolean(path);

    }

    public String getString(final String path) {

        if (this.configuration.get(path) != null) {

            return StringUtils.translateAlternateColorCodes('&', this.configuration.getString(path));

        } return "String at path: " + path + " not found!";
    }
}
