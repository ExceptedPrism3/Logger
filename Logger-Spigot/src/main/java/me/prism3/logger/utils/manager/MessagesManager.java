package me.prism3.logger.utils.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.Languages;
import me.prism3.logger.utils.updater.FileUpdater;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.prism3.logger.utils.Data.*;

public class MessagesManager {

    private final Main plugin = Main.getInstance();
    private final Set<String> langFiles = Stream.of(Languages.values()).map(Languages::getMessageFile).collect(Collectors.toSet());
    private final Map<String, FileConfiguration> cache = new ConcurrentHashMap<>();
    private static MessagesManager instance;
    private FileConfiguration messagesFile;
    private String langFolderPath;

    private MessagesManager() {}

    public void init() {

        this.langFolderPath = this.plugin.getDataFolder() + File.separator + langPath + File.separator;

        // Copy language files if they don't exist
        this.copyLangFiles();

        if (!this.langFiles.contains(selectedLang.toLowerCase())) {
            Log.severe("The selected language file: '" + selectedLang + "' could not be found or invalid, disabling...");
            return;
        }

        this.load();

        this.versionCheck();
    }

    public void reload() {
        this.cache.clear();
        this.load();
    }

    private void load() {

        final String selectedLangFile = this.langFolderPath + selectedLang + fileType;
        File langFile = new File(selectedLangFile);

        if (!langFile.exists()) {
            this.plugin.saveResource(langPath + File.separator + selectedLang + fileType, false);
            langFile = new File(selectedLangFile);
        }

        if (this.cache.containsKey(selectedLang)) {
            this.messagesFile = this.cache.get(selectedLang);
        } else {
            this.messagesFile = YamlConfiguration.loadConfiguration(langFile);
            this.cache.put(selectedLang, this.messagesFile);
        }
    }

    // Cycle through the Messages Folder and load what's listed in the Array
    private void copyLangFiles() {
        for (String l : this.langFiles) {
            final Path filePath = Paths.get(this.langFolderPath + l + fileType);
            if (!Files.exists(filePath))
                this.plugin.saveResource(langPath + File.separator + l + fileType, false);
        }
    }

    private void versionCheck() {

        String jarVersion = null;
        String currentVersion = null;

        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try (final InputStream input = Files.newInputStream(new File(this.langFolderPath, selectedLang + fileType).toPath())) {

            JsonNode versionNode = mapper.readTree(this.plugin.getResource("messages/" + selectedLang + fileType));
            jarVersion = versionNode.get("Messages-Version").asText();
            versionNode = mapper.readTree(input);
            currentVersion = versionNode.get("Messages-Version").asText();
        } catch (final Exception e) { e.printStackTrace(); }

        if (currentVersion == null || currentVersion.isEmpty() || currentVersion.equalsIgnoreCase("null")) {
            this.resetMessages();
            return;
        }

        if (this.compareVersions(currentVersion, jarVersion) < 0) {
            try {
                FileUpdater.update(this.plugin, "messages/" + selectedLang + fileType, new File(this.plugin.getDataFolder() + "/messages/" + selectedLang + fileType), Collections.singletonList("Messages-Version"));
                Log.warning("Messages file updated from version " + currentVersion + " to version " + jarVersion);
            } catch (final IOException e) {
                Log.severe("Error reading the messages file, if the issue persists contact the authors!");
                this.resetMessages();
            }
        }
    }

    private void resetMessages() {

        this.plugin.getExecutor().submit(() -> {

            final File fileM = new File(this.plugin.getDataFolder() + "/messages/", selectedLang + fileType);

            try {
                Files.move(fileM.toPath(), fileM.toPath().resolveSibling(selectedLang + ".old.yml"), StandardCopyOption.REPLACE_EXISTING);

                this.load();
                Log.warning("Due to an error reading the messages file, it was reset to default settings");
                Log.warning("This was likely caused by a mistake while you changed settings, like an extra space or missing quotes");
                Log.warning("The broken messages file was renamed to " + selectedLang + ".old.yml, you can copy your old settings manually if you need them");
            } catch (final IOException e) { Log.severe("Error resetting messages file"); }
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

    public static MessagesManager getInstance() {
        if (instance == null)
            instance = new MessagesManager();
        return instance;
    }

    public FileConfiguration get() { return this.messagesFile; }
}
