package com.carpour.logger.onCommands;

import com.carpour.logger.Discord.DiscordFile;
import com.carpour.logger.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class LoggerCommand implements CommandExecutor {

    private final Main plugin = Main.getInstance();
//    private Database db;

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {

        if (args.length != 0 && !args[0].equalsIgnoreCase("Reload")) {

            sender.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("Messages.Invalid-Syntax")).replaceAll("&", "§"));

            return false;
        }

        else if (args.length == 1 && args[0].equalsIgnoreCase("Reload")) {

            if (sender instanceof Player) {

                if (sender.hasPermission("logger.reload")) {

                    plugin.reloadConfig();
                    DiscordFile.reload();
                    sender.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("Messages.Reload-Message")).replaceAll("&", "§"));

                } else {

                    sender.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("Messages.No-Permission")).replaceAll("&", "§"));

                }

            } else {

                plugin.reloadConfig();
                DiscordFile.reload();
                getServer().getConsoleSender().sendMessage(Objects.requireNonNull(plugin.getConfig().getString("Messages.Reload-Message")).replaceAll("&", "§"));

            }
            return true;

        } else if (args.length > 1 && args[0].equalsIgnoreCase("Reload")){

            sender.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("Messages.Invalid-Syntax")).replaceAll("&", "§"));

            return false;

        } else if (sender instanceof Player && plugin.getConfig().getBoolean("Credits.Enabled")){

            sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "|==========|" + ChatColor.RESET + " " +
                    ChatColor.AQUA + ChatColor.BOLD + "Logger" + ChatColor.RESET + " " + ChatColor.BLUE + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "|==========|" +
                    ChatColor.WHITE + "\n\nThis Plugin was made with " + ChatColor.RED + "<3" + ChatColor.WHITE + " by " + ChatColor.GOLD + ChatColor.ITALIC +
                    "Prism3" + ChatColor.AQUA + "\nspigotmc.org/resources/logger.94236\n\n" +
                    ChatColor.BLUE + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "|============================|");

            return true;

        }

        /*if (args.length == 1 && args[0].equalsIgnoreCase("ChatLog")){

            if (sender instanceof Player){

                sender.sendMessage(db.getPlayerChatLogs("ExceptedPrism3"));

            }

        }*/


        if (!(sender instanceof Player)){

            getServer().getConsoleSender().sendMessage("\n\n" + ChatColor.DARK_PURPLE + "Thank you for using the Logger Plugin!\n");

        }
        return false;
    }
}
