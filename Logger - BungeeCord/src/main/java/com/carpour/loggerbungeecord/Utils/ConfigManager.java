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

    public void init() {
        saveDefaultConfig();
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        String str = this.config.getString(key);
        str = ChatColor.translateAlternateColorCodes('&', str);
        return str;
    }

    public boolean getBoolean(String key) {
        return this.config.getBoolean(key);
    }

    public List<String> getStringList(String key) {
        List<String> list = this.config.getStringList(key);
        List<String> avail = new ArrayList<>();
        for (String str : list)
            avail.add(ChatColor.translateAlternateColorCodes('&', str));
        return avail;
    }

    public File getFile() {
        return new File(Main.getInstance().getDataFolder(), "config.yml");
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDefaultConfig() {
        if (!Main.getInstance().getDataFolder().exists())
            Main.getInstance().getDataFolder().mkdir();
        File file = getFile();
        if (!file.exists()) {
            try {
                file.createNewFile();
                InputStream is = Main.getInstance().getResourceAsStream("config.yml");
                Throwable localThrowable6 = null;
                try {
                    OutputStream os = new FileOutputStream(file);
                    Throwable localThrowable7 = null;
                    try {
                        ByteStreams.copy(is, os);
                        os.close();
                        is.close();
                    } catch (Throwable localThrowable1) {
                        localThrowable7 = localThrowable1;
                        throw localThrowable1;
                    }
                } catch (Throwable localThrowable4) {
                    localThrowable6 = localThrowable4;
                    throw localThrowable4;
                } finally {
                    if (is != null)
                        if (localThrowable6 != null) {
                            try {
                                is.close();
                            } catch (Throwable localThrowable5) {
                                localThrowable6.addSuppressed(localThrowable5);
                            }
                        } else {
                            is.close();
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
