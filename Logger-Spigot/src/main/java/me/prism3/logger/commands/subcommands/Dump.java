package me.prism3.logger.commands.subcommands;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.commands.SubCommand;
import me.prism3.logger_core.utils.DumpHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * Dump class is a subcommand for the Logger plugin that creates an online
 * pastebin URL containing configuration files, Discord configs, language files,
 * and the server's latest.log file with safe tail-truncation.
 */
public class Dump implements SubCommand {

    private final LoggerAPI plugin;

    public Dump(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "dump";
    }

    @Override
    public String getDescription() {
        return "Creates an online pastebin URL of the plugin's config, discord, messages file, and server's latest.log file.";
    }

    @Override
    public String getSyntax() {
        return "/logger " + this.getName();
    }

    @Override
    public void perform(final CommandSender sender, final String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "Generating pastebin dump, please wait...");

        plugin.runAsync(() -> {
            try {
                File dataFolder = plugin.getDataFolder();
                InputStream bundledEnv = plugin.getResource(".env");
                String apiKey = DumpHelper.resolveApiKey(dataFolder, bundledEnv);

                if (apiKey == null || apiKey.trim().isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Pastebin API key not configured. Set PASTEBIN_API in environment variables or .env file.");
                    return;
                }

                Map<String, File> files = new LinkedHashMap<>();
                File configFile = new File(dataFolder, "config.yml");
                if (configFile.exists()) files.put("config.yml", configFile);

                File discordFile = new File(dataFolder, "discord.yml");
                if (!discordFile.exists() && dataFolder.getParentFile() != null) {
                    File addonConfig = new File(new File(dataFolder.getParentFile(), "LoggerDiscordAddon"), "discord.yml");
                    if (addonConfig.exists()) discordFile = addonConfig;
                }
                if (discordFile.exists()) files.put("discord.yml", discordFile);

                String lang = plugin.getData() != null ? plugin.getData().getLanguage() : "en_US";
                File langFile = new File(dataFolder, "messages" + File.separator + lang + ".yml");
                if (langFile.exists()) files.put("messages...", langFile);

                File latestLog = new File("logs" + File.separator + "latest.log");
                if (latestLog.exists()) files.put("latest.log", latestLog);

                String pasteUrl = DumpHelper.postDump(apiKey, "Logger MC Plugin Dump", files);

                if (pasteUrl != null && pasteUrl.startsWith("http")) {
                    readyMessage(sender, pasteUrl);
                } else {
                    sender.sendMessage(ChatColor.RED + "Failed to create Pastebin dump: " + (pasteUrl != null ? pasteUrl : "unknown error"));
                }
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Error creating dump: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    private void readyMessage(CommandSender sender, String pasteUrl) {
        String prefix = plugin.getData() != null ? plugin.getData().getPluginPrefix() : "&b[Logger] &r";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                prefix + pasteUrl + "\n&cDo not share this link with anyone!"));
    }


    @Override
    public List<String> getSubCommandsArgs(final CommandSender sender, final String[] args) {
        return Collections.emptyList();
    }


    @Override
    public String getPermission() {
        return me.prism3.logger.managers.PermissionManager.LOGGER_RELOAD;
    }
}
