package me.prism3.logger.utils.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.updater.FileUpdater;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

public class DiscordManager {

    private final Main plugin;
    private final File discordFile;
    private final String discordFileName = "discord.yml";
    private FileConfiguration cache;


    public DiscordManager(final Main plugin) {
        this.plugin = plugin;
        this.discordFile = new File(plugin.getDataFolder(), discordFileName);
        this.loadDiscord();
    }

    private void loadDiscord() {
        this.checkDiscord();
        this.initDiscord();
    }

    public void reload() {
        this.plugin.getExecutor().submit(() -> {

            this.initDiscord();

            try {
                if (this.plugin.getDiscord() != null) {

                    this.plugin.getDiscord().disconnect();
                    this.plugin.getDiscord().run();

                    Log.info("The bot has been restarted successfully");
                }
            } catch (final Exception e) {
                Log.severe("An error occurred while restarting the bot. If the issue persists contact the authors.");
                e.printStackTrace();
            }
        });
    }

    private void checkDiscord() {

        this.plugin.getExecutor().submit(() -> {
            if (!this.discordFile.exists())
                return;

            String jarVersion = null;
            String currentVersion = null;

            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try (final InputStream input = Files.newInputStream(new File(this.plugin.getDataFolder(), discordFileName).toPath())) {

                final JsonNode json = mapper.readTree(this.plugin.getResource(discordFileName));
                jarVersion = json.get("Discord-Version").asText();

                final JsonNode json2 = mapper.readTree(input);
                currentVersion = json2.get("Discord-Version").asText();

            } catch (final Exception e) { e.printStackTrace(); }

            if (currentVersion == null || currentVersion.isEmpty()) {
                this.resetDiscord();
                return;
            }

            if (this.compareVersions(currentVersion, jarVersion) < 0) {
                try {
                    FileUpdater.update(this.plugin, discordFileName, this.discordFile, Collections.singletonList("Discord-Version"));
                    Log.warning("DiscordManager file updated from version " + currentVersion + " to version " + jarVersion);
                } catch (final IOException e) {
                    Log.severe("Error reading the discord file, if the issue persists contact the authors!");
                    this.resetDiscord();
                }
            }
        });
    }

    private void initDiscord() { this.cache = new FileInitializer(this.plugin, discordFileName); }

    private void resetDiscord() {
        this.plugin.getExecutor().submit(() -> {
            try {

                Files.move(this.discordFile.toPath(), this.discordFile.toPath().resolveSibling("discord.old.yml"), StandardCopyOption.REPLACE_EXISTING);

                this.initDiscord();
                Log.warning("Due to an error reading the discord file, it was reset to default settings");
                Log.warning("This was likely caused by a mistake while you changed settings, like an extra space or missing quotes");
                Log.warning("The broken discord file was renamed to discord.old.yml, you can copy your old settings manually if you need them");
            } catch (final IOException e) { Log.severe("Error resetting discord file"); }
        });
    }

    private int compareVersions(String current, String latest) {

        final String[] currentParts = current.split("\\.");
        final String[] latestParts = latest.split("\\.");

        for (int i = 0; i < Math.max(currentParts.length, latestParts.length); i++) {

            int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int latestPart = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;

            if (currentPart != latestPart)
                return currentPart - latestPart;
        }
        return 0;
    }

    public FileConfiguration get() { return this.cache; }
}
