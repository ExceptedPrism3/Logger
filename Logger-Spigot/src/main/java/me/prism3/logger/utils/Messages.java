package me.prism3.logger.utils;

import me.prism3.logger.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static me.prism3.logger.utils.Data.*;

public class Messages {

    private final Main main = Main.getInstance();
    private FileConfiguration messagesFile;
    private final List<String> langFiles = Arrays.asList("ar", "fr_fr", "en_en", "zh_cn", "zh-cht");
    private boolean isValid = true;

    public Messages() {

        this.copyLangFiles();

        // Check if the selected Language is Valid | Exists
        if (!this.langFiles.contains(selectedLang.toLowerCase())) {

            this.main.getLogger().severe("Unknown selected language file: '" + selectedLang + "'");
            this.main.getLogger().severe("Disabling....");
            this.isValid = false;
            return;

        }

        this.messagesFile = YamlConfiguration.loadConfiguration(new File(this.main.getDataFolder() + File.separator + langPath, this.main.getConfig().getString("Language") + fileType));

    }

    public FileConfiguration get() { return this.messagesFile; }

    public void reload() {

        this.messagesFile = YamlConfiguration.loadConfiguration(new File(this.main.getDataFolder() + File.separator + langPath, this.main.getConfig().getString("Language") + fileType));

    }

    // Cycle through the Messages Folder and load what's listed in the Array
    private void copyLangFiles() {

        for (String l : this.langFiles)
            if (!new File(this.main.getDataFolder() + File.separator + langPath + File.separator + l + fileType).exists())
                this.main.saveResource(langPath + File.separator + l + fileType, false);

    }

    public boolean getIsValid() { return this.isValid; }

    public List<String> getLangFiles() { return this.langFiles; }
}
