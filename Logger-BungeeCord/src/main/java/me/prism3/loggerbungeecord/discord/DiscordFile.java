package me.prism3.loggerbungeecord.discord;

import com.google.common.io.ByteStreams;
import me.prism3.loggerbungeecord.Main;
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

            this.discord = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.getFile());

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
        return new File(Main.getInstance().getDataFolder(), "discord - Bungee.yml");
    }

    public Configuration get() { return this.discord; }

    private void saveDefaultConfig() {

        if (!Main.getInstance().getDataFolder().exists()) Main.getInstance().getDataFolder().mkdir();

        final File file = this.getFile();

        if (!file.exists()) {

            try {

                file.createNewFile();

                try (final InputStream is = Main.getInstance().getResourceAsStream("discord - Bungee.yml")) {

                    final OutputStream os = new FileOutputStream(file);
                    ByteStreams.copy(is, os);
                    os.close();

                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
