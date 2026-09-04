package me.prism3.logger_bungee.commands;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Constants;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
                    "&b&lUsage&b: /loggerproxy <&areload&8&l|&emanual&8&l|&9discord&b>")));
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
            return completions;
        }
        return Collections.emptyList();
    }
}
