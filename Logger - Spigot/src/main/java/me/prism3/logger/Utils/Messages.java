package me.prism3.logger.Utils;

import me.prism3.logger.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static me.prism3.logger.Utils.Data.*;

public class Messages {

    private static final Main main = Main.getInstance();
    private static FileConfiguration messages;
    private final List<String> langFiles = Arrays.asList("ar", "fr_fr", "en_en", "zh_cn");
    private boolean isValid = true;

    public Messages() {

        copyLangFiles();

        // Check if the selected Language is Valid | Exists
        if (!langFiles.contains(selectedLang.toLowerCase())) {

            main.getLogger().severe("Unknown selected language file: '" + selectedLang + "'");
            main.getLogger().severe("Disabling....");
            isValid = false;
            return;

        }

        messages = YamlConfiguration.loadConfiguration(new File(main.getDataFolder() + "/" + langPath, main.getConfig().getString("Language") + fileType));

    }

    public static FileConfiguration get() { return messages; }

    public static void reload() {

        messages = YamlConfiguration.loadConfiguration(new File(main.getDataFolder() + "/" + langPath, main.getConfig().getString("Language") + fileType));

    }

    // Cycle through the Messages Folder and load what's listed in the Array
    private void copyLangFiles() {

        for (String l : langFiles)
            if (!new File(main.getDataFolder() + "/" + langPath + "/" + l + fileType).exists())
                main.saveResource(langPath + "/" + l + fileType, false);

    }

    public static boolean getOldMsg(){

        Path path = Paths.get(main.getDataFolder() + "/" + langPath + "/en.yml");

        return path.toFile().exists();
    }

    public boolean getIsValid() { return isValid; }
}
