package com.carpour.loggervelocity.Utils;

import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.Config.Configuration;
import com.carpour.loggervelocity.Utils.Config.ConfigurationProvider;
import com.carpour.loggervelocity.Utils.Config.StringUtils;
import com.carpour.loggervelocity.Utils.Config.YamlConfiguration;
import com.google.common.io.ByteStreams;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigManager {

    private File file;
    private Configuration configuration;
    private final File dataFolder = Main.getInstance().getFolder().toFile();

    public ConfigManager() {

        this.file = new File(dataFolder, "config - Velocity.yml");

        try {

            if (!this.file.exists()) {

                if (!dataFolder.exists()) { dataFolder.mkdir(); }

                this.file.createNewFile();

                try (final InputStream is = ConfigManager.class.getResourceAsStream("/config - Velocity.yml");
                     final OutputStream os = new FileOutputStream(this.file)) {
                    assert is != null;
                    ByteStreams.copy(is, os);
                }
            }
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public File getFile() {
        return this.file;
    }

    public double getDouble(final String path) {

        if (this.configuration.get(path) != null) {

            return this.configuration.getDouble(path);

        } return 0.0;
    }

    public int getInt(final String path) {

        if (this.configuration.get(path) != null) {

            return this.configuration.getInt(path);

        } return 0;
    }

    public int getLong(final String path) {

        if (this.configuration.get(path) != null) {

            return (int) this.configuration.getLong(path);

        } return 0;
    }

    public Object get(final String path) {
        return this.configuration.get(path);
    }

    public boolean getBoolean(final String path) {

        return this.configuration.get(path) != null && this.configuration.getBoolean(path);

    }

    public String getString(final String path) {

        if (this.configuration.get(path) != null) {

            return StringUtils.translateAlternateColorCodes('&', this.configuration.getString(path));

        } return "String at path: " + path + " not found!";
    }

    public List<String> getStringList(final String path) {

        if (this.configuration.get(path) != null) {

            final ArrayList<String> strings = new ArrayList<>();

            for (final String string : this.configuration.getStringList(path)) {

                strings.add(StringUtils.translateAlternateColorCodes('&', string));

            } return strings;
        } return Collections.singletonList("String List at path: " + path + " not found!");
    }

    public void reload(){ load(); }

    private void load() {

        this.file = new File(dataFolder, "config - Velocity.yml");

        try {

            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        }

        catch (IOException e) { e.printStackTrace(); }
    }

    /*private void save() {

        try {

            ConfigurationProvider.getProvider( YamlConfiguration.class).save(this.configuration, this.file);

        }

        catch (IOException e) { e.printStackTrace(); }
    }*/
}
