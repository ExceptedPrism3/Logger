package me.prism3.loggerbungeecord.discord;

import com.google.common.io.ByteStreams;
import me.prism3.loggerbungeecord.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class DiscordFile {

    private Configuration discord = null;

    public void init() {

        this.saveDefaultConfig();

        try {

            this.discord = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getFile());

        } catch (IOException e) { e.printStackTrace(); }
    }

    public boolean getBoolean(String key) {
        return this.discord.getBoolean(key);
    }

    public String getString(String key) {

        String str = this.discord.getString(key);

        str = ChatColor.translateAlternateColorCodes('&', str);

        return str;
    }

    public File getFile() {
        return new File(Logger.getInstance().getDataFolder(), "discord - Bungee.yml");
    }

    public Configuration get() { return this.discord; }

    private void saveDefaultConfig() {

        if (!Logger.getInstance().getDataFolder().exists()) Logger.getInstance().getDataFolder().mkdir();

        final File file = getFile();

        if (!file.exists()) {

            try {

                file.createNewFile();

                try (InputStream is = Logger.getInstance().getResourceAsStream("discord - Bungee.yml")) {

                    OutputStream os = new FileOutputStream(file);
                    ByteStreams.copy(is, os);
                    os.close();

                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
