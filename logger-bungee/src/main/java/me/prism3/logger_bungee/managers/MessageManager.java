package me.prism3.logger_bungee.managers;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Log;
import me.prism3.logger_bungee.utils.YamlMigrator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MessageManager {

    private final LoggerBungee plugin;
    private Configuration messages;
    private File messagesFile;

    private static final String[] BUNDLED_LANGUAGES = new String[] {
            "en_US", "fr_FR", "es_ES", "de_DE", "it_IT", "pt_BR", "ru_RU", "zh_CN", "ja_JP", "ko_KR", "ar_SA"
    };

    public MessageManager(LoggerBungee plugin) {
        this.plugin = plugin;
        this.loadMessages();
    }

    public void loadMessages() {
        try {
            File messagesDir = new File(this.plugin.getDataFolder(), "messages");
            if (!messagesDir.exists()) {
                messagesDir.mkdirs();
            }

            // Extract all bundled language files and sync missing keys non-destructively
            for (String lang : BUNDLED_LANGUAGES) {
                File langFile = new File(messagesDir, lang + ".yml");
                if (!langFile.exists()) {
                    InputStream in = this.plugin.getResourceAsStream("bungee-messages/" + lang + ".yml");
                    if (in == null) in = this.plugin.getResourceAsStream("messages/" + lang + ".yml");
                    if (in != null) {
                        try (InputStream input = in) {
                            Files.copy(input, langFile.toPath());
                        }
                    }
                } else {
                    InputStream in = this.plugin.getResourceAsStream("bungee-messages/" + lang + ".yml");
                    if (in != null) {
                        YamlMigrator.syncDefaults(this.plugin, langFile, "bungee-messages/" + lang + ".yml");
                    } else {
                        YamlMigrator.syncDefaults(this.plugin, langFile, "messages/" + lang + ".yml");
                    }
                }
            }

            // Determine configured language
            String lang = "en_US";
            if (this.plugin.getConfigManager() != null && this.plugin.getConfigManager().getConfig() != null) {
                lang = this.plugin.getConfigManager().getConfig().getString("Language", "en_US");
            }

            this.messagesFile = resolveLanguageFile(messagesDir, lang);

            if (!this.messagesFile.exists()) {
                this.messagesFile = new File(messagesDir, "en_US.yml");
                if (!this.messagesFile.exists()) {
                    this.messagesFile = new File(this.plugin.getDataFolder(), "messages.yml");
                    if (!this.messagesFile.exists()) {
                        InputStream in = this.plugin.getResourceAsStream("bungee-messages/en_US.yml");
                        if (in == null) in = this.plugin.getResourceAsStream("messages/en_US.yml");
                        if (in != null) {
                            try (InputStream input = in) {
                                Files.copy(input, this.messagesFile.toPath());
                            }
                        }
                    }
                }
            }

            this.messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.messagesFile);
            Log.info("Loaded language file: " + this.messagesFile.getName());

        } catch (IOException e) {
            Log.severe("Failed to load messages: " + e.getMessage());
        }
    }

    private File resolveLanguageFile(File messagesDir, String lang) {
        if (lang == null || lang.isEmpty()) {
            return new File(messagesDir, "en_US.yml");
        }

        File file = new File(messagesDir, lang + ".yml");
        if (file.exists()) return file;

        if (lang.equalsIgnoreCase("fr") || lang.equalsIgnoreCase("fr_FR")) return new File(messagesDir, "fr_FR.yml");
        if (lang.equalsIgnoreCase("es") || lang.equalsIgnoreCase("es_ES")) return new File(messagesDir, "es_ES.yml");
        if (lang.equalsIgnoreCase("de") || lang.equalsIgnoreCase("de_DE")) return new File(messagesDir, "de_DE.yml");
        if (lang.equalsIgnoreCase("it") || lang.equalsIgnoreCase("it_IT")) return new File(messagesDir, "it_IT.yml");
        if (lang.equalsIgnoreCase("pt") || lang.equalsIgnoreCase("pt_BR") || lang.equalsIgnoreCase("pt_br")) return new File(messagesDir, "pt_BR.yml");
        if (lang.equalsIgnoreCase("ru") || lang.equalsIgnoreCase("ru_RU")) return new File(messagesDir, "ru_RU.yml");
        if (lang.equalsIgnoreCase("zh") || lang.equalsIgnoreCase("zh_CN") || lang.equalsIgnoreCase("zh_cn")) return new File(messagesDir, "zh_CN.yml");
        if (lang.equalsIgnoreCase("ja") || lang.equalsIgnoreCase("ja_JP")) return new File(messagesDir, "ja_JP.yml");
        if (lang.equalsIgnoreCase("ko") || lang.equalsIgnoreCase("ko_KR")) return new File(messagesDir, "ko_KR.yml");
        if (lang.equalsIgnoreCase("ar") || lang.equalsIgnoreCase("ar_SA") || lang.equalsIgnoreCase("ar_sa")) return new File(messagesDir, "ar_SA.yml");
        if (lang.equalsIgnoreCase("en") || lang.equalsIgnoreCase("en_US")) return new File(messagesDir, "en_US.yml");

        return file;
    }

    public String getGeneralMessage(String path) {
        if (this.messages == null) return path;
        String raw = this.messages.getString("General." + path, path);
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public String formatFileMessage(String path, Map<String, String> placeholders) {
        return getFileMessage(path, placeholders);
    }

    public String getFileMessage(String path, Map<String, String> placeholders) {
        if (this.messages == null) return path;
        String msg = this.messages.getString("Files." + path);
        if (msg == null) {
            msg = this.messages.getString("File." + path);
        }
        if (msg == null) {
            if (path.contains("Server-Commands") || path.contains("Console-Commands")) {
                msg = "[%time%] [%server%] %command%";
            } else if (path.contains("Manual-Log")) {
                msg = "[%time%] [%server%] Manual log recorded => %log%";
            } else {
                msg = "Message not found: " + path;
            }
        }
        return applyPlaceholders(msg, placeholders);
    }

    public String formatDiscordMessage(String path, Map<String, String> placeholders) {
        return getDiscordMessage(path, placeholders);
    }

    public String getDiscordMessage(String path, Map<String, String> placeholders) {
        if (this.messages == null) return path;
        String msg = this.messages.getString("Discord." + path);
        if (msg == null) {
            if (path.contains("Server-Commands") || path.contains("Console-Commands")) {
                msg = "**[%time%]** %command%";
            } else if (path.contains("Manual-Log")) {
                msg = "**[%time%]** Manual log: **%log%**";
            } else {
                msg = "Discord message not found: " + path;
            }
        }
        return applyPlaceholders(msg, placeholders);
    }

    private String applyPlaceholders(String text, Map<String, String> placeholders) {
        if (text == null) return "";

        // Auto-inject time and date if not provided in placeholders
        String timePattern = "yyyy-MM-dd HH:mm:ss";
        if (plugin.getConfigManager() != null && plugin.getConfigManager().getConfig() != null) {
            timePattern = plugin.getConfigManager().getConfig().getString("Time-Formatter", "yyyy-MM-dd HH:mm:ss");
        }
        String formattedTime;
        try {
            formattedTime = new SimpleDateFormat(timePattern).format(new Date());
        } catch (Exception e) {
            formattedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
        text = text.replace("%time%", formattedTime);
        text = text.replace("%date%", formattedTime);

        String serverName = "BungeeCord";
        if (plugin.getConfigManager() != null && plugin.getConfigManager().getConfig() != null) {
            serverName = plugin.getConfigManager().getConfig().getString("Server-Name", "BungeeCord");
        }
        text = text.replace("%server%", serverName);

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                final String key = entry.getKey();
                final String val = entry.getValue() != null ? entry.getValue() : "";
                text = text.replace("%" + key + "%", val);
                text = text.replace("%" + key.toLowerCase() + "%", val);
                text = text.replace("%" + key.toUpperCase() + "%", val);
            }
        }
        return text;
    }

    public void reload() {
        this.loadMessages();
    }
}
