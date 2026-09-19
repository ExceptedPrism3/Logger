package me.prism3.logger.managers;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.YamlMigrator;
import me.prism3.logger.utils.enums.GeneralSideMessages;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Optimized MessageManager:
 * - Automatically extracts all bundled language files on boot
 * - Automatically synchronizes missing entries non-destructively
 * - Supports smart language alias resolution
 * - Caches general messages and raw templates
 */
public class MessageManager {

    private final LoggerAPI plugin;
    private final File messagesDir;
    private final Map<String, String> generalCache = new ConcurrentHashMap<>();
    private final Map<String, String> templateCache = new ConcurrentHashMap<>();

    private static final String[] BUNDLED_LANGUAGES = new String[] {
            "en_US", "fr_fr", "es_ES", "de_DE", "it_IT", "pt_BR", "ru_RU", "zh_cn", "ja_JP", "ko_KR", "ar"
    };

    public MessageManager(final LoggerAPI plugin) {
        this.plugin = plugin;
        this.messagesDir = new File(this.plugin.getDataFolder(), "messages");
        this.loadMessages();
    }

    private String normalizeLanguage(String lang) {
        if (lang == null || lang.trim().isEmpty()) return "en_US";
        String l = lang.trim();
        if (l.equalsIgnoreCase("en") || l.equalsIgnoreCase("en_en") || l.equalsIgnoreCase("en_us") || l.equalsIgnoreCase("en_US") || l.equalsIgnoreCase("english")) {
            return "en_US";
        } else if (l.equalsIgnoreCase("fr") || l.equalsIgnoreCase("fr_fr") || l.equalsIgnoreCase("fr_FR") || l.equalsIgnoreCase("french")) {
            return "fr_fr";
        } else if (l.equalsIgnoreCase("zh") || l.equalsIgnoreCase("zh_cn") || l.equalsIgnoreCase("zh_CN") || l.equalsIgnoreCase("chinese")) {
            return "zh_cn";
        } else if (l.equalsIgnoreCase("ar") || l.equalsIgnoreCase("ar_sa") || l.equalsIgnoreCase("ar_SA") || l.equalsIgnoreCase("arabic")) {
            return "ar";
        } else if (l.equalsIgnoreCase("de") || l.equalsIgnoreCase("de_de") || l.equalsIgnoreCase("de_DE") || l.equalsIgnoreCase("german")) {
            return "de_DE";
        } else if (l.equalsIgnoreCase("es") || l.equalsIgnoreCase("es_es") || l.equalsIgnoreCase("es_ES") || l.equalsIgnoreCase("spanish")) {
            return "es_ES";
        } else if (l.equalsIgnoreCase("it") || l.equalsIgnoreCase("it_it") || l.equalsIgnoreCase("it_IT") || l.equalsIgnoreCase("italian")) {
            return "it_IT";
        } else if (l.equalsIgnoreCase("ja") || l.equalsIgnoreCase("ja_jp") || l.equalsIgnoreCase("ja_JP") || l.equalsIgnoreCase("japanese")) {
            return "ja_JP";
        } else if (l.equalsIgnoreCase("ko") || l.equalsIgnoreCase("ko_kr") || l.equalsIgnoreCase("ko_KR") || l.equalsIgnoreCase("korean")) {
            return "ko_KR";
        } else if (l.equalsIgnoreCase("pt") || l.equalsIgnoreCase("pt_br") || l.equalsIgnoreCase("pt_BR") || l.equalsIgnoreCase("portuguese")) {
            return "pt_BR";
        } else if (l.equalsIgnoreCase("ru") || l.equalsIgnoreCase("ru_ru") || l.equalsIgnoreCase("ru_RU") || l.equalsIgnoreCase("russian")) {
            return "ru_RU";
        }
        return l;
    }

    private File resolveLanguageFile(String lang) {
        String normalized = normalizeLanguage(lang);

        for (String bundled : BUNDLED_LANGUAGES) {
            if (bundled.equalsIgnoreCase(normalized)) {
                File file = new File(this.messagesDir, bundled + ".yml");
                if (file.exists()) return file;
                try {
                    this.plugin.saveResource("messages/" + bundled + ".yml", false);
                    if (file.exists()) return file;
                } catch (Exception ignored) {}
            }
        }

        File file = new File(this.messagesDir, normalized + ".yml");
        if (file.exists()) return file;

        file = new File(this.messagesDir, normalized.toLowerCase() + ".yml");
        if (file.exists()) return file;

        // Fallback default
        return new File(this.messagesDir, "en_US.yml");
    }

    public void loadMessages() {

        if (!this.messagesDir.exists())
            this.messagesDir.mkdirs();

        // Extract all bundled language files and sync missing keys non-destructively
        for (String langName : BUNDLED_LANGUAGES) {
            final File langFile = new File(this.messagesDir, langName + ".yml");
            if (!langFile.exists()) {
                try {
                    this.plugin.saveResource("messages/" + langName + ".yml", false);
                } catch (Exception ignored) {}
            } else {
                YamlMigrator.syncDefaults(this.plugin, langFile, "messages/" + langName + ".yml");
            }
        }

        // Also sync main config.yml & discord.yml if they exist
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        if (configFile.exists()) {
            YamlMigrator.syncDefaults(this.plugin, configFile, "config.yml");
        }
        File discordFile = new File(this.plugin.getDataFolder(), "discord.yml");
        if (discordFile.exists()) {
            YamlMigrator.syncDefaults(this.plugin, discordFile, "discord.yml");
        }

        final String lang = this.plugin.getData().getLanguage();
        File chosen = resolveLanguageFile(lang);

        if (!chosen.exists()) {
            chosen = new File(this.messagesDir, "en_US.yml");
            if (!chosen.exists()) {
                try {
                    this.plugin.saveResource("messages/en_US.yml", false);
                } catch (Exception ignored) {}
            }
        }

        // Sync missing defaults directly into chosen file if it is custom or older version
        if (chosen.exists()) {
            String resourceName = "messages/" + chosen.getName();
            if (this.plugin.getResource(resourceName) != null) {
                YamlMigrator.syncDefaults(this.plugin, chosen, resourceName);
            } else {
                YamlMigrator.syncDefaults(this.plugin, chosen, "messages/en_US.yml");
            }
        }

        final FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(chosen);

        // Attach bundled en_US.yml as default fallback so NO key ever evaluates to missing
        try (java.io.InputStream defaultStream = this.plugin.getResource("messages/en_US.yml")) {
            if (defaultStream != null) {
                try (java.io.InputStreamReader reader = new java.io.InputStreamReader(defaultStream, java.nio.charset.StandardCharsets.UTF_8)) {
                    messagesConfig.setDefaults(YamlConfiguration.loadConfiguration(reader));
                }
            }
        } catch (Exception ignored) {}

        this.generalCache.clear();
        this.templateCache.clear();

        // Cache general messages
        for (GeneralSideMessages key : GeneralSideMessages.values()) {

            final String path = key.getPath();
            String raw = messagesConfig.getString(path, "Message not found: " + path);
            raw = raw.replace("%prefix%", this.plugin.getData().getPluginPrefix());
            this.generalCache.put(path, ChatColor.translateAlternateColorCodes('&', raw));
        }

        // Cache templates for all LogTypes & channels & staff flags
        for (LogType type : LogType.values()) {

            for (char channel : new char[] { 'F', 'D' }) {

                for (boolean staff : new boolean[] { false, true }) {

                    final String parent = (channel == 'F') ? "File" : "Discord";
                    final String key = parent + ":" + type.name() + ":" + staff;
                    String tpl = messagesConfig.getString(parent + "." + type.getMessagePath(staff));
                    if (tpl == null || tpl.trim().isEmpty()) {
                        // Try un-staffed variant fallback
                        tpl = messagesConfig.getString(parent + "." + type.getMessagePath(false));
                    }
                    if (tpl == null || tpl.trim().isEmpty()) {
                        tpl = "[" + parent + "] Event: " + type.name();
                    }

                    this.templateCache.put(key, tpl);
                }
            }
        }
    }

    public String getGeneralMessage(final GeneralSideMessages key) {
        return this.generalCache.getOrDefault(key.getPath(), "Message not found: " + key.getPath());
    }

    public String getMessage(final LogType logType, final boolean isStaff, final Map<String, String> placeholders,
            final char channel, final org.bukkit.entity.Player player) {

        final String parent = (channel == 'F') ? "File" : "Discord";
        final String key = parent + ":" + logType.name() + ":" + isStaff;

        String msg = this.templateCache.getOrDefault(key,
                "Message not found: " + parent + "." + logType.getMessagePath(isStaff));

        if (placeholders != null) {
            for (Map.Entry<String, String> e : placeholders.entrySet()) {
                msg = msg.replace("%" + e.getKey() + "%", e.getValue());
            }
        }

        if (org.bukkit.Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            msg = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, msg);
        }

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void reloadMessages() {
        this.loadMessages();
    }
}
