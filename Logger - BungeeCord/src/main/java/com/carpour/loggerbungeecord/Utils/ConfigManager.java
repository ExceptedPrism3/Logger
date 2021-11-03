package com.carpour.loggerbungeecord.Utils;

import com.carpour.loggerbungeecord.Main;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    Configuration config = null;

    public Configuration getConfig() {
        return config;
    }

    public void init() {

        saveDefaultConfig();

        try {

            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile());

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public String getString(String key) {
        String str = config.getString(key);
        str = ChatColor.translateAlternateColorCodes('&', str);
        return str;
    }

    public int getInt(String key) { return this.config.getInt(key);}

    public long getLong(String key) { return this.config.getLong(key);}

    public boolean getBoolean(String key) {
        return this.config.getBoolean(key);
    }

    /*public List<String> getStringList(String key) {
        List<String> list = this.config.getStringList(key);
        List<String> avail = new ArrayList<>();
        for (String str : list)
            avail.add(ChatColor.translateAlternateColorCodes('&', str));
        return avail;
    }*/

    public File getFile() {
        return new File(Main.getInstance().getDataFolder(), "config - Bungee.yml");
    }

    private void saveDefaultConfig() {

        if (!Main.getInstance().getDataFolder().exists()) Main.getInstance().getDataFolder().mkdir();

        File file = getFile();

        if (!file.exists()) {

            try {

                file.createNewFile();

                try (InputStream is = Main.getInstance().getResourceAsStream("config - Bungee.yml")) {

                    OutputStream os = new FileOutputStream(file);
                    ByteStreams.copy(is, os);
                    os.close();

                }

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }
}
