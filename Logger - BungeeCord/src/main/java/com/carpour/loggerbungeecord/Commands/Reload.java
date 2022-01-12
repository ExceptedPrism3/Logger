package com.carpour.loggerbungeecord.Commands;

import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Reload extends Command {

    private final Main main = Main.getInstance();

    public Reload(){ super("loggerproxy"); }

    @Override
    public void execute(CommandSender sender, String[] args){

        if (sender.hasPermission("loggerproxy.staff") || sender.hasPermission("loggerproxy.reload")) {

            if (args.length == 0) {

                sender.sendMessage(new TextComponent("Running Logger " + ChatColor.AQUA + ChatColor.BOLD + Main.getInstance().getDescription().getVersion()));

            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

                main.getConfig().init();
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
