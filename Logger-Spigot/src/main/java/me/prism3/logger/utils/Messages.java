package me.prism3.logger.utils;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.Languages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.prism3.logger.utils.Data.*;

public class Messages {

    private FileConfiguration messagesFile;
    private final List<String> langFiles = Stream.of(Languages.values()).map(Languages::getMessageFile).collect(Collectors.toList());

    public Messages() {

        // Copy language files if they don't exist
        this.copyLangFiles();


        if (!this.langFiles.contains(selectedLang.toLowerCase())) {
            Log.severe("The selected language file: '" + selectedLang + "' could not be found or invalid, disabling...");
            return;
        }

        final String langFilePath = Main.getInstance().getDataFolder() + File.separator + langPath + File.separator + selectedLang + fileType;
        File langFile = new File(langFilePath);
        if (!langFile.exists()) {
            Main.getInstance().saveResource(langPath + File.separator + selectedLang + fileType, false);
            langFile = new File(langFilePath);
        }
        this.messagesFile = YamlConfiguration.loadConfiguration(langFile);
    }

    public FileConfiguration get() { return this.messagesFile; }

    public void reload() {
        final String langFilePath = Main.getInstance().getDataFolder() + File.separator + langPath + File.separator + selectedLang + fileType;
        File langFile = new File(langFilePath);
        if (!langFile.exists()) {
            Main.getInstance().saveResource(langPath + File.separator + selectedLang + fileType, false);
            langFile = new File(langFilePath);
        }
        this.messagesFile = YamlConfiguration.loadConfiguration(langFile);
    }

    // Cycle through the Messages Folder and load what's listed in the Array
    private void copyLangFiles() {
        for (String l : this.langFiles)
            if (!new File(Main.getInstance().getDataFolder() + File.separator + langPath + File.separator + l + fileType).exists())
                Main.getInstance().saveResource(langPath + File.separator + l + fileType, false);
    }
}
