package com.carpour.loggerbungeecord.Commands;

import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.ConfigManager;
import com.carpour.loggerbungeecord.Utils.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class Reload extends Command {

    ConfigManager cm = Main.getConfig();

    public Reload(){

        super("loggerbungee");

    }

    @Override
    public void execute(CommandSender sender, String[] args){

        if (args.length == 0 && !(sender instanceof ProxiedPlayer)){

            Main.getInstance().getLogger().info(ChatColor.DARK_PURPLE + "Thank you for using the Logger Plugin!");

        } else if (args.length == 0){

            sender.sendMessage("Running Logger " + ChatColor.AQUA + ChatColor.BOLD + Main.getInstance().getDescription().getVersion());

        }

        else if (args.length == 1 && args[0].equalsIgnoreCase("reload")){

            if(sender.hasPermission("loggerbungee.reload")) {

                cm.init();
                new Messages().init();
                sender.sendMessage(Messages.getString("General.Reload"));

            } else{

                sender.sendMessage(Messages.getString("General.No-Permission"));

            }

        } else{

            sender.sendMessage(Messages.getString("General.Invalid-Syntax"));

        }
    }
}
