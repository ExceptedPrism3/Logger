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

    private File resolveLanguageFile(String lang) {
        if (lang == null || lang.isEmpty()) {
            return new File(this.messagesDir, "en_US.yml");
        }

        File file = new File(this.messagesDir, lang + ".yml");
        if (file.exists()) return file;

        file = new File(this.messagesDir, lang.toLowerCase() + ".yml");
        if (file.exists()) return file;

        if (lang.equalsIgnoreCase("fr") || lang.equalsIgnoreCase("fr_FR") || lang.equalsIgnoreCase("fr_fr")) {
            file = new File(this.messagesDir, "fr_fr.yml");
            if (file.exists()) return file;
        } else if (lang.equalsIgnoreCase("zh") || lang.equalsIgnoreCase("zh_CN") || lang.equalsIgnoreCase("zh_cn")) {
            file = new File(this.messagesDir, "zh_cn.yml");
            if (file.exists()) return file;
        } else if (lang.equalsIgnoreCase("ar") || lang.equalsIgnoreCase("ar_SA") || lang.equalsIgnoreCase("ar_sa")) {
            file = new File(this.messagesDir, "ar.yml");
            if (file.exists()) return file;
        } else if (lang.equalsIgnoreCase("en") || lang.equalsIgnoreCase("en_US") || lang.equalsIgnoreCase("en_en")) {
            file = new File(this.messagesDir, "en_US.yml");
            if (file.exists()) return file;
        }

        for (String bundled : BUNDLED_LANGUAGES) {
            if (bundled.equalsIgnoreCase(lang)) {
                try {
                    this.plugin.saveResource("messages/" + bundled + ".yml", false);
                    file = new File(this.messagesDir, bundled + ".yml");
                    if (file.exists()) return file;
                } catch (Exception ignored) {}
            }
        }

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
        }

        final FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(chosen);

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
                    final String tpl = messagesConfig.getString(
                            parent + "." + type.getMessagePath(staff),
                            "Message not found: " + parent + "." + type.getMessagePath(staff));

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
