package me.prism3.loggerbungeecord.utils;

import com.google.common.io.ByteStreams;
import me.prism3.loggerbungeecord.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class Messages {

    private Configuration messagesFile = null;

    public void init() {

        this.saveDefaultConfig();

        try {

            this.messagesFile = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile());

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public String getString(String key) {

        String str = this.messagesFile.getString(key);

        str = ChatColor.translateAlternateColorCodes('&', str);

        return str;
    }

    public File getFile() {
        return new File(Logger.getInstance().getDataFolder(), "messages - Bungee.yml");
    }

    private void saveDefaultConfig() {

        if (!Logger.getInstance().getDataFolder().exists()) Logger.getInstance().getDataFolder().mkdir();

        final File file = getFile();

        if (!file.exists()) {

            try {

                file.createNewFile();

                try (final InputStream is = Logger.getInstance().getResourceAsStream("messages - Bungee.yml")) {

                    final OutputStream os = new FileOutputStream(file);
                    ByteStreams.copy(is, os);
                    os.close();

                }
            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }
}