package me.prism3.loggervelocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.prism3.loggervelocity.Logger;
import me.prism3.loggervelocity.utils.Messages;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;

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
                    "&b&lUsage&b: /loggerproxy <&areload&8&l|&emanual&8&l|&9discord&b>"
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
            return list;
        }
        return Collections.emptyList();
    }
}
