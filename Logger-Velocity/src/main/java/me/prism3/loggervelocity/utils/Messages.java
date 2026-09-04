package me.prism3.loggervelocity.utils;

import com.google.common.io.ByteStreams;
import me.prism3.loggervelocity.Logger;
import me.prism3.loggervelocity.utils.config.Configuration;
import me.prism3.loggervelocity.utils.config.ConfigurationProvider;
import me.prism3.loggervelocity.utils.config.StringUtils;
import me.prism3.loggervelocity.utils.config.YamlConfiguration;

import java.io.*;

public class Messages {

    private File file;
    private Configuration configuration;

    private final File dataFolder = Logger.getInstance().getFolder().toFile();

    private static final String[] BUNDLED_LANGUAGES = new String[] {
            "en_US", "fr_FR", "es_ES", "de_DE", "it_IT", "pt_BR", "ru_RU", "zh_CN", "ja_JP", "ko_KR", "ar_SA"
    };

    public Messages() {
        this.load();
    }

    public void reload() {
        this.load();
    }

    private void load() {
        File messagesDir = new File(dataFolder, "messages");
        if (!messagesDir.exists()) {
            messagesDir.mkdirs();
        }

        // Extract all bundled language files and sync missing keys non-destructively
        for (String lang : BUNDLED_LANGUAGES) {
            File langFile = new File(messagesDir, lang + ".yml");
            if (!langFile.exists()) {
                InputStream is = Messages.class.getResourceAsStream("/velocity-messages/" + lang + ".yml");
                if (is == null) is = Messages.class.getResourceAsStream("/messages/" + lang + ".yml");
                if (is != null) {
                    try (InputStream in = is; OutputStream os = new FileOutputStream(langFile)) {
                        ByteStreams.copy(in, os);
                    } catch (IOException ignored) {}
                }
            } else {
                InputStream is = Messages.class.getResourceAsStream("/velocity-messages/" + lang + ".yml");
                if (is != null) {
                    YamlMigrator.syncDefaults(langFile, "/velocity-messages/" + lang + ".yml");
                } else {
                    YamlMigrator.syncDefaults(langFile, "/messages/" + lang + ".yml");
                }
            }
        }

        String chosenLang = "en_US";
        try {
            if (Logger.getInstance().getConfig() != null) {
                String val = Logger.getInstance().getConfig().getString("Language");
                if (val != null && !val.isEmpty() && !val.startsWith("String at path")) {
                    chosenLang = val;
                }
            }
        } catch (Exception ignored) {}

        File chosenFile = new File(messagesDir, chosenLang + ".yml");
        if (!chosenFile.exists()) {
            if (chosenLang.equalsIgnoreCase("fr")) chosenFile = new File(messagesDir, "fr_FR.yml");
            else if (chosenLang.equalsIgnoreCase("es")) chosenFile = new File(messagesDir, "es_ES.yml");
            else if (chosenLang.equalsIgnoreCase("de")) chosenFile = new File(messagesDir, "de_DE.yml");
            else if (chosenLang.equalsIgnoreCase("it")) chosenFile = new File(messagesDir, "it_IT.yml");
            else if (chosenLang.equalsIgnoreCase("pt") || chosenLang.equalsIgnoreCase("pt_br")) chosenFile = new File(messagesDir, "pt_BR.yml");
            else if (chosenLang.equalsIgnoreCase("ru")) chosenFile = new File(messagesDir, "ru_RU.yml");
            else if (chosenLang.equalsIgnoreCase("zh") || chosenLang.equalsIgnoreCase("zh_cn")) chosenFile = new File(messagesDir, "zh_CN.yml");
            else if (chosenLang.equalsIgnoreCase("ja") || chosenLang.equalsIgnoreCase("ja_jp")) chosenFile = new File(messagesDir, "ja_JP.yml");
            else if (chosenLang.equalsIgnoreCase("ko") || chosenLang.equalsIgnoreCase("ko_kr")) chosenFile = new File(messagesDir, "ko_KR.yml");
            else if (chosenLang.equalsIgnoreCase("ar") || chosenLang.equalsIgnoreCase("ar_sa")) chosenFile = new File(messagesDir, "ar_SA.yml");
        }

        if (!chosenFile.exists()) {
            chosenFile = new File(messagesDir, "en_US.yml");
        }

        if (!chosenFile.exists()) {
            chosenFile = new File(dataFolder, "messages.yml");
            if (!chosenFile.exists()) {
                InputStream is = ConfigManager.class.getResourceAsStream("/velocity-messages.yml");
                if (is == null) is = ConfigManager.class.getResourceAsStream("/messages.yml");
                if (is != null) {
                    try (InputStream in = is; OutputStream os = new FileOutputStream(chosenFile)) {
                        ByteStreams.copy(in, os);
                    } catch (IOException ignored) {}
                }
            }
        }

        this.file = chosenFile;

        try {
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return this.file;
    }

    public String getString(final String path) {
        if (this.configuration != null && this.configuration.get(path) != null) {
            return StringUtils.translateAlternateColorCodes('&', this.configuration.getString(path));
        }
        return "String at path: " + path + " not found!";
    }
}
