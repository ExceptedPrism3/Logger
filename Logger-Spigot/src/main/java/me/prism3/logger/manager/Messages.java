package me.prism3.logger.manager;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.Languages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.prism3.logger.utils.Data.*;


public class Messages {

    private final Main main = Main.getInstance();
    private FileConfiguration messagesFile;
    private final Set<String> langFiles = Stream.of(Languages.values()).map(Languages::getMessageFile).collect(Collectors.toSet());

    public Messages() {

        this.copyLangFiles();

        String selectedLanguage = selectedLang;

        // Check if the selected Language is Valid | Exists
        if (!this.langFiles.contains(selectedLanguage.toLowerCase())) {
            Log.severe("Unknown selected language file: '" + selectedLanguage + "'");
            Log.severe("Using en_en instead");
            selectedLanguage = "en_en";
            selectedLang = "en_en";
        }

        if (selectedLanguage != null) {
            final File configFile = new File(this.main.getDataFolder(), langPath + File.separator + selectedLanguage + fileType);
            messagesFile = YamlConfiguration.loadConfiguration(configFile);
        }
    }

    public FileConfiguration get() { return messagesFile; }

    public void reload() {

        if (messagesFile != null) {

            final String languageFileName = this.main.getConfig().getString("Language");

            if (languageFileName != null) {
                File configFile = new File(this.main.getDataFolder(), langPath + File.separator + languageFileName + fileType);
                messagesFile = YamlConfiguration.loadConfiguration(configFile);
            }
        }
    }

    // Create language files that don't exist
    private void copyLangFiles() {
        this.langFiles.stream()
                .map(lang -> new File(this.main.getDataFolder(), langPath + File.separator + lang + fileType))
                .filter(langFile -> !langFile.exists())
                .forEach(langFile -> this.main.saveResource(langPath + File.separator + langFile.getName(), false));
    }
}
