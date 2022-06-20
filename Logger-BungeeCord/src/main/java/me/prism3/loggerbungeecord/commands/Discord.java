package me.prism3.loggerbungeecord.commands;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import static me.prism3.loggerbungeecord.utils.Data.discordSupportServer;

public class Discord extends Command {

    public Discord() { super("loggerproxy"); }

    @Override
    public void execute(CommandSender sender, String[] args) {

        final Main main = Main.getInstance();

        if (sender.hasPermission(Data.loggerStaff) || sender.hasPermission(Data.loggerReload)) {

            if (args.length == 0) {

                sender.sendMessage(new TextComponent("Running Logger " + ChatColor.AQUA + ChatColor.BOLD + Data.pluginVersion));

            } else if (args.length == 1 && args[0].equalsIgnoreCase("discord")) {

                sender.sendMessage(new TextComponent(discordSupportServer));

            } else {

                sender.sendMessage(new TextComponent(main.getMessages().getString("General.Invalid-Syntax")));

            }
        } else {

            sender.sendMessage(new TextComponent(main.getMessages().getString("General.No-Permission")));

        }
    }
}
