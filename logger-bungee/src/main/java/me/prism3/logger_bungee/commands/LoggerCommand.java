package me.prism3.logger_bungee.commands;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Constants;
import me.prism3.logger_core.utils.DumpHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LoggerCommand extends Command implements TabExecutor {

    private final LoggerBungee plugin;

    public LoggerCommand(LoggerBungee plugin) {
        super("loggerproxy", null, "lgp");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!this.plugin.getPermissionManager().canReload(sender)) {
            sender.sendMessage(new TextComponent(this.plugin.getMessageManager().getGeneralMessage("No-Permission")));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    "&b&lUsage&b: /loggerproxy <&areload&8&l|&emanual&8&l|&9discord&8&l|&bdump&b>")));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            this.plugin.reload();
            sender.sendMessage(new TextComponent(this.plugin.getMessageManager().getGeneralMessage("Reload")));
            return;
        }

        if (args[0].equalsIgnoreCase("manual")) {
            if (args.length < 2) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /loggerproxy manual <message...>"));
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            String message = sb.toString().trim();

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("log", message);
            placeholders.put("command", message);
            this.plugin.getLogManager().logServerEvent(Constants.Events.SERVER_MANUAL_LOG, placeholders);

            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    "&b[Logger] &aManual log recorded: &f" + message)));
            return;
        }

        if (args[0].equalsIgnoreCase("discord")) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    "&b[Logger] Discord Support: &fhttps://discord.gg/MfR5mcpVfX")));
            return;
        }

        if (args[0].equalsIgnoreCase("dump")) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    "&b[Logger] &7Generating dump to Pastebin...")));

            this.plugin.runAsync(() -> {
                try {
                    File dataFolder = this.plugin.getDataFolder();
                    InputStream bundledEnv = this.plugin.getResourceAsStream(".env");
                    String apiKey = DumpHelper.resolveApiKey(dataFolder, bundledEnv);

                    if (apiKey == null || apiKey.trim().isEmpty()) {
                        sender.sendMessage(new TextComponent(ChatColor.RED + "Pastebin API key not configured. Set PASTEBIN_API in environment variables or .env file."));
                        return;
                    }

                    Map<String, File> files = new LinkedHashMap<>();
                    // Config
                    File cfg = new File(dataFolder, "bungee-config.yml");
                    if (!cfg.exists()) cfg = new File(dataFolder, "config.yml");
                    files.put("bungee-config.yml", cfg);

                    // Discord
                    File discord = new File(dataFolder, "bungee-discord.yml");
                    if (!discord.exists()) {
                        File addonCfg = new File(dataFolder.getParentFile(), "LoggerDiscordAddon/bungee-discord.yml");
                        if (addonCfg.exists()) discord = addonCfg;
                    }
                    files.put("bungee-discord.yml", discord);

                    // Messages
                    String lang = this.plugin.getConfigManager().getConfig().getString("Language", "en_US");
                    File msgFile = new File(dataFolder, "bungee-messages/" + lang + ".yml");
                    files.put("bungee-messages/" + lang + ".yml", msgFile);

                    // Proxy log
                    File logFile = new File("proxy.log.0");
                    if (!logFile.exists()) logFile = new File("logs/proxy.log.0");
                    if (!logFile.exists()) logFile = new File("proxy.log");
                    if (!logFile.exists()) logFile = new File("logs/latest.log");
                    files.put("proxy.log", logFile);

                    String pasteUrl = DumpHelper.postDump(apiKey, "Logger BungeeCord Dump", files);
                    if (pasteUrl != null && pasteUrl.startsWith("http")) {
                        sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&b[Logger] &a" + pasteUrl + "\n&cDo not share this link with anyone!")));
                    } else {
                        sender.sendMessage(new TextComponent(ChatColor.RED + "Failed to post to Pastebin: " + (pasteUrl != null ? pasteUrl : "unknown error")));
                    }
                } catch (Exception e) {
                    sender.sendMessage(new TextComponent(ChatColor.RED + "Failed to create dump: " + e.getMessage()));
                }
            });
            return;
        }

        sender.sendMessage(new TextComponent(this.plugin.getMessageManager().getGeneralMessage("Invalid-Syntax")));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (!this.plugin.getPermissionManager().canReload(sender)) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            String partial = args[0].toLowerCase();
            if ("reload".startsWith(partial)) completions.add("reload");
            if ("manual".startsWith(partial)) completions.add("manual");
            if ("discord".startsWith(partial)) completions.add("discord");
            if ("dump".startsWith(partial)) completions.add("dump");
            return completions;
        }
        return Collections.emptyList();
    }
}
