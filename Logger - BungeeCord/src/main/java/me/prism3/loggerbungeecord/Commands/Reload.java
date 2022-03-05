package me.prism3.loggerbungeecord.Commands;

import me.prism3.loggerbungeecord.Utils.ConfigManager;
import me.prism3.loggerbungeecord.Utils.Messages;
import me.prism3.loggerbungeecord.Utils.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Reload extends Command {

    public Reload(){ super("loggerproxy"); }

    @Override
    public void execute(CommandSender sender, String[] args){

        if (sender.hasPermission(Data.loggerStaff) || sender.hasPermission(Data.loggerReload)) {

            if (args.length == 0) {

                sender.sendMessage(new TextComponent("Running Logger " + ChatColor.AQUA + ChatColor.BOLD + Data.pluginVersion));

            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

                new ConfigManager().init();
                new Messages().init();
                sender.sendMessage(new TextComponent(Messages.getString("General.Reload")));

            } else {

                sender.sendMessage(new TextComponent(Messages.getString("General.Invalid-Syntax")));

            }
        } else {

            sender.sendMessage(new TextComponent(Messages.getString("General.No-Permission")));

        }
    }
}
