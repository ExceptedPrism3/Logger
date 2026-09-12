package me.prism3.loggervelocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.prism3.logger_core.utils.DumpHelper;
import me.prism3.loggervelocity.Logger;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static me.prism3.loggervelocity.utils.Data.*;

public class LoggerProxyCommands implements SimpleCommand {

    private final Logger main = Logger.getInstance();

    @Override
    public void execute(Invocation invocation) {
        final CommandSource sender = invocation.source();
        final String[] args = invocation.arguments();

        // If no arguments, show a help message.
        if (args.length == 0) {
            Component helpMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(
                    "&b&lUsage&b: /loggerproxy <&areload&8&l|&emanual&8&l|&9discord&8&l|&bdump&b>"
            );
            sender.sendMessage(Identity.nil(), helpMessage);
            return;
        }

        // Subcommand: reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission(loggerReload) && !sender.hasPermission("loggerproxy.admin")) {
                sender.sendMessage(Identity.nil(),
                        LegacyComponentSerializer.legacyAmpersand().deserialize(
                                this.main.getMessages().getString("General.No-Permission").replace("%prefix%", pluginPrefix)));
                return;
            }
            // Reload all components cleanly
            this.main.reload();
            sender.sendMessage(Identity.nil(),
                    LegacyComponentSerializer.legacyAmpersand().deserialize(
                            this.main.getMessages().getString("General.Reload").replace("%prefix%", pluginPrefix)));
            return;
        }

        // Subcommand: manual
        if (args[0].equalsIgnoreCase("manual")) {
            if (!sender.hasPermission(loggerReload) && !sender.hasPermission("loggerproxy.admin")) {
                sender.sendMessage(Identity.nil(),
                        LegacyComponentSerializer.legacyAmpersand().deserialize(
                                this.main.getMessages().getString("General.No-Permission").replace("%prefix%", pluginPrefix)));
                return;
            }

            if (args.length < 2) {
                sender.sendMessage(Identity.nil(),
                        LegacyComponentSerializer.legacyAmpersand().deserialize("&cUsage: /loggerproxy manual <message...>"));
                return;
            }

            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("log", message);
            placeholders.put("command", message);

            this.main.getLogManager().logServerEvent("Server-Side.Manual-Log", placeholders);

            sender.sendMessage(Identity.nil(),
                    LegacyComponentSerializer.legacyAmpersand().deserialize(pluginPrefix + "&aManual log recorded: &f" + message));
            return;
        }

        // Subcommand: discord
        if (args[0].equalsIgnoreCase("discord")) {
            sender.sendMessage(Identity.nil(),
                    LegacyComponentSerializer.legacyAmpersand().deserialize(pluginPrefix + "&bDiscord Support: &f" + discordSupportServer));
            return;
        }

        // Subcommand: dump
        if (args[0].equalsIgnoreCase("dump")) {
            if (!sender.hasPermission(loggerReload) && !sender.hasPermission("loggerproxy.admin")) {
                sender.sendMessage(Identity.nil(),
                        LegacyComponentSerializer.legacyAmpersand().deserialize(
                                this.main.getMessages().getString("General.No-Permission").replace("%prefix%", pluginPrefix)));
                return;
            }

            sender.sendMessage(Identity.nil(),
                    LegacyComponentSerializer.legacyAmpersand().deserialize(pluginPrefix + "&7Generating dump to Pastebin..."));

            CompletableFuture.runAsync(() -> {
                try {
                    File dataFolder = this.main.getFolder().toFile();
                    InputStream bundledEnv = getClass().getClassLoader().getResourceAsStream(".env");
                    String apiKey = DumpHelper.resolveApiKey(dataFolder, bundledEnv);

                    if (apiKey == null || apiKey.trim().isEmpty()) {
                        sender.sendMessage(Identity.nil(),
                                LegacyComponentSerializer.legacyAmpersand().deserialize("&cPastebin API key not configured. Set PASTEBIN_API in environment variables or .env file."));
                        return;
                    }

                    Map<String, File> files = new LinkedHashMap<>();
                    // Config
                    File cfg = new File(dataFolder, "config.yml");
                    files.put("velocity-config.yml", cfg);

                    // Discord
                    File discord = new File(dataFolder, "discord.yml");
                    if (!discord.exists()) {
                        File addonCfg = new File(dataFolder.getParentFile(), "LoggerDiscordAddon/velocity-discord.yml");
                        if (addonCfg.exists()) discord = addonCfg;
                    }
                    files.put("velocity-discord.yml", discord);

                    // Messages
                    String lang = this.main.getConfig().getString("Language", "en_US");
                    File msgFile = new File(dataFolder, "messages/" + lang + ".yml");
                    if (!msgFile.exists()) msgFile = new File(dataFolder, "messages.yml");
                    files.put("messages/" + lang + ".yml", msgFile);

                    // Velocity Log
                    File logFile = new File("logs/velocity.log");
                    if (!logFile.exists()) logFile = new File("velocity.log");
                    if (!logFile.exists()) logFile = new File("logs/latest.log");
                    files.put("velocity.log", logFile);

                    String pasteUrl = DumpHelper.postDump(apiKey, "Logger Velocity Dump", files);
                    if (pasteUrl != null && pasteUrl.startsWith("http")) {
                        sender.sendMessage(Identity.nil(),
                                LegacyComponentSerializer.legacyAmpersand().deserialize(
                                        pluginPrefix + "&a" + pasteUrl + "\n&cDo not share this link with anyone!"));
                    } else {
                        sender.sendMessage(Identity.nil(),
                                LegacyComponentSerializer.legacyAmpersand().deserialize("&cFailed to post to Pastebin: " + (pasteUrl != null ? pasteUrl : "unknown error")));
                    }
                } catch (Exception e) {
                    sender.sendMessage(Identity.nil(),
                            LegacyComponentSerializer.legacyAmpersand().deserialize("&cFailed to create dump: " + e.getMessage()));
                }
            });
            return;
        }

        // If subcommand is not recognized:
        sender.sendMessage(Identity.nil(),
                LegacyComponentSerializer.legacyAmpersand().deserialize(
                        this.main.getMessages().getString("General.Invalid-Syntax").replace("%prefix%", pluginPrefix)));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length <= 1) {
            List<String> list = new ArrayList<>();
            String partial = args.length == 0 ? "" : args[0].toLowerCase();
            if ("reload".startsWith(partial)) list.add("reload");
            if ("manual".startsWith(partial)) list.add("manual");
            if ("discord".startsWith(partial)) list.add("discord");
            if ("dump".startsWith(partial)) list.add("dump");
            return list;
        }
        return Collections.emptyList();
    }
}
