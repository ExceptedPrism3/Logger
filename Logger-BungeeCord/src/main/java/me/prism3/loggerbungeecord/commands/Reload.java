package me.prism3.loggerbungeecord.commands;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import static me.prism3.loggerbungeecord.utils.Data.pluginVersion;

public class Reload extends Command {

    public Reload() { super("loggerb"); }

    @Override
    public void execute(CommandSender sender, String[] args) {

        final Main main = Main.getInstance();

        if (sender.hasPermission(Data.loggerStaff) || sender.hasPermission(Data.loggerReload)) {

            if (args.length == 0) {

                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "Running Logger &b&l" + Data.pluginVersion)));

            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

                main.getConfig().init();
                main.getMessages().init();
                main.initializer(new Data());
                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("General.Reload").replace("%prefix%", pluginVersion))));

            } else {

                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("General.Invalid-Syntax").replace("%prefix%", pluginVersion))));

            }
        } else {

            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', main.getMessages().getString("General.No-Permission").replace("%prefix%", pluginVersion))));

        }
    }
}
