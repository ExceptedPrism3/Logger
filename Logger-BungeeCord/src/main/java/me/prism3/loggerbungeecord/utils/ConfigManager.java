package me.prism3.loggerbungeecord.utils;

import com.google.common.io.ByteStreams;
import me.prism3.loggerbungeecord.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import ru.vyarus.yaml.updater.YamlUpdater;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;

import static me.prism3.loggerbungeecord.utils.Data.configVersion;

public class ConfigManager {

    Main plugin = Main.getInstance();

    private Configuration config = null;

    public void init() {

        Main.getInstance().getProxy().getScheduler().schedule(Main.getInstance(), () ->
                new PluginUpdater().run(), 1, 12L * 60, TimeUnit.MINUTES);

        this.configVersionChecker();

        this.saveDefaultConfig();

        try {

            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.getFile());

        } catch (final IOException e) { e.printStackTrace(); }
    }

    public String getString(String key) {
        String str = this.config.getString(key);
        str = ChatColor.translateAlternateColorCodes('&', str);
        return str;
    }

    public int getInt(String key) { return this.config.getInt(key); }

    public boolean getBoolean(String key) { return this.config.getBoolean(key); }

    public List<String> getStringList(String key) {
        final List<String> list = this.config.getStringList(key);
        final List<String> avail = new ArrayList<>();
        for (String str : list)
            avail.add(ChatColor.translateAlternateColorCodes('&', str));
        return avail;
    }

    public File getFile() { return new File(Main.getInstance().getDataFolder(), "config - Bungee.yml"); }

    private void saveDefaultConfig() {

        if (!Main.getInstance().getDataFolder().exists()) Main.getInstance().getDataFolder().mkdir();

        final File file = this.getFile();

        if (!file.exists()) {

            try {

                file.createNewFile();

                try (final InputStream is = Main.getInstance().getResourceAsStream("config - Bungee.yml")) {

                    final OutputStream os = Files.newOutputStream(file.toPath());
                    ByteStreams.copy(is, os);
                    os.close();
                }
            } catch (final IOException e) { e.printStackTrace(); }
        }
    }

    private void configVersionChecker() {

        if (!this.getFile().exists())
            return;

        final int oldVersion = configVersion;
        final int currentVersion = plugin.getConfig().getInt("Config-Version");

        try {

            JarFile d = new JarFile("config - Bungee.yml");

        } catch (Exception e) {}


        if (oldVersion == 0) {
            this.resetConfig();
            return;
        }

        if (oldVersion < currentVersion) {
            try {

                final File file = new File(Main.getInstance().getDataFolder(), "config - Bungee.yml");

                YamlUpdater.create(this.getFile(), Main.getInstance().getResourceAsStream(file.getName()))
                        .backup(true)
                        .update();

                Log.warning("Config file updated from version " + oldVersion + " to version " + currentVersion);
            } catch (final Exception e) {
                Log.severe("Error reading the config file, if the issue persists contact the authors!");
                this.resetConfig();
            }
        }
    }

    private void resetConfig() {

        try {
            Files.move(this.getFile().toPath(), this.getFile().toPath().resolveSibling("config - Bungee.old.yml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) { Log.severe("Error resetting the config file"); }

        this.init();
        Log.warning("Due to an error reading the config, it was reset to default settings");
        Log.warning("This was likely caused by a mistake while you changed settings, like an extra space or missing quotes");
        Log.warning("The broken config was renamed to config.old.yml, you can copy your old settings manually if you need them");
    }
}
