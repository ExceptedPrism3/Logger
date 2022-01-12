package com.carpour.logger.Commands;

import com.carpour.logger.Discord.DiscordFile;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OnLogger implements CommandExecutor {

    private final Main main = Main.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (sender.hasPermission("logger.staff") || sender.hasPermission("logger.reload")) {

            if (args.length != 0 && !(args[0].equalsIgnoreCase("Reload") || args[0].equalsIgnoreCase("Credits"))) {

                sender.sendMessage(Objects.requireNonNull(Messages.get().getString("General.Invalid-Syntax")).replaceAll("&", "§"));
                return false;

            } else if (args.length == 1) {

                if (args[0].equalsIgnoreCase("Reload")) {

                    main.reloadConfig();
                    Messages.reload();
                    DiscordFile.reload();
                    sender.sendMessage(Objects.requireNonNull(Messages.get().getString("General.Reload")).replaceAll("&", "§"));

                } else if (args[0].equalsIgnoreCase("Credits")) {

                    sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "|==========|" + ChatColor.RESET + " " +
                            ChatColor.AQUA + ChatColor.BOLD + "Logger" + ChatColor.RESET + " " + ChatColor.BLUE + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "|==========|" +
                            ChatColor.WHITE + "\n\nThis Plugin was made with " + ChatColor.RED + "<3" + ChatColor.WHITE + " by " + ChatColor.GOLD + ChatColor.ITALIC +
                            "Prism3" + ChatColor.RESET + " and " + ChatColor.GOLD + ChatColor.ITALIC + "thelooter" + ChatColor.AQUA + "\nspigotmc.org/resources/logger.94236\n\n" +
                            ChatColor.BLUE + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "|============================|");

                }

            } else if (args.length > 1 && (args[0].equalsIgnoreCase("Reload") || args[0].equalsIgnoreCase("Credits"))) {

                sender.sendMessage(Objects.requireNonNull(Messages.get().getString("General.Invalid-Syntax")).replaceAll("&", "§"));
                return false;

            } else if (args.length == 0) {

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Running Logger &a&l" + Main.getInstance().getDescription().getVersion() + "\n" +
                        "&f/Logger credits &6- &bShows the plugin's Authors\n&f/Logger reload &6- &bReloads the Plugin"));

            }
        }else {

            sender.sendMessage(Objects.requireNonNull(Messages.get().getString("General.No-Permission")).replaceAll("&", "§"));
            return false;

        } return true;
    }
}
