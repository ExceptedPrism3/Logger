package me.prism3.loggerbungeecord.utils;

import com.google.common.io.ByteStreams;
import me.prism3.loggerbungeecord.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import ru.vyarus.yaml.updater.YamlUpdater;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConfigManager {

    private final File configFile;

    private Configuration config = null;

    public ConfigManager() {

        this.configFile = new File(Main.getInstance().getDataFolder(), "config - Bungee.yml");
        this.load();
    }

    private void load() {

        this.configVersionChecker();

        this.init();

        // Plugin Update Checker that runs every 12h
        Main.getInstance().getProxy().getScheduler().schedule(Main.getInstance(), () ->
                new PluginUpdater().run(), 5, 12L * 60, TimeUnit.MINUTES);
    }

    public void reload() { this.init(); }

    private void init() {

        if (!Main.getInstance().getDataFolder().exists())
            Main.getInstance().getDataFolder().mkdir();

        if (!this.configFile.exists()) {

            try {

                this.configFile.createNewFile();

                try (final InputStream is = Main.getInstance().getResourceAsStream("config - Bungee.yml")) {

                    final OutputStream os = Files.newOutputStream(this.configFile.toPath());
                    ByteStreams.copy(is, os);
                    os.close();
                }
            } catch (final IOException e) { e.printStackTrace(); }
        }

        try {

            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.configFile);

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

    // Compares the versions between the old config and the new
    private void configVersionChecker() {

        if (!this.configFile.exists())
            return;

        String currentVersion = "0";

        final Yaml currentFileYaml = new Yaml();
        final InputStream currentFileStream;

        final Map<String, Object> currentFileObject;

        try {
            currentFileStream = Files.newInputStream(new File(Main.getInstance().getDataFolder(), "config - Bungee.yml").toPath());
            currentFileObject = currentFileYaml.load(currentFileStream);

            if (currentFileObject.get("Config-Version") != null)
                currentVersion = currentFileObject.get("Config-Version").toString();

        } catch (Exception e) { e.printStackTrace(); }

        final Yaml yaml = new Yaml();
        final InputStream inputStream = Main.getInstance().getClass().getClassLoader().getResourceAsStream("config - Bungee.yml");
        final Map<String, Object> obj = yaml.load(inputStream);

        final String remoteVersion = obj.get("Config-Version") != null ? obj.get("Config-Version").toString() : "0";

        if (currentVersion.equals("0")) {
            this.resetConfig();
            return;
        }

        if (this.versionTagChecker(remoteVersion, currentVersion)) {

            try {

                YamlUpdater.create(this.getFile(), Main.getInstance().getResourceAsStream("config - Bungee.yml"))
                .backup(true)
                .update();

                Log.warning("Config file updated from version " + currentVersion + " to version " + remoteVersion);
            } catch (final Exception e) {

                Log.severe("Error reading the config file, if the issue persists contact the authors!");
                e.printStackTrace();
                this.resetConfig();
            }
        }
    }

    private boolean versionTagChecker(final String remoteVersion, final String currentVersion) {

        if (remoteVersion.equalsIgnoreCase(currentVersion))
            return false;

        final String[] remote = remoteVersion.split("\\.");
        final String[] local = currentVersion.split("\\.");
        final int length = Math.max(local.length, remote.length);

        try {
            for (int i = 0; i < length; i++) {

                final int localNumber = i < local.length ? Integer.parseInt(local[i]) : 0;
                final int remoteNumber = i < remote.length ? Integer.parseInt(remote[i]) : 0;

                if (remoteNumber > localNumber)
                    return true;

                if (remoteNumber < localNumber)
                    return false;
            }
        } catch (final NumberFormatException ex) {

            Log.warning("An Error has occurred whilst reading the version tag. If this issue persists contact the Authors.");
        }

        return false;
    }

    private void resetConfig() {

        try {
            Files.move(this.getFile().toPath(), this.getFile().toPath().resolveSibling("config - Bungee.old.yml"),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (final IOException e) { Log.severe("Error resetting the config file"); }

        this.init();
        Log.warning("Due to an error reading the config, it was reset to default settings");
        Log.warning("This was likely caused by a mistake while you changed settings, like an extra space or missing quotes");
        Log.warning("The broken config was renamed to config.old.yml, you can copy your old settings manually if you need them");
    }
}
