package com.carpour.logger.Commands.onCommands;

import com.carpour.logger.Commands.SubCommands;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Credits extends SubCommands {

    @Override
    public String getName() {
        return "credits";
    }

    @Override
    public String getDescription() {
        return "Plugin credits";
    }

    @Override
    public String getSyntax() {
        return "/logger credits";
    }

    @Override
    public void perform(Player player, String[] args) {

        player.sendMessage(ChatColor.BLUE.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "|==========|" + ChatColor.RESET + " " +
                ChatColor.AQUA + ChatColor.BOLD + "Logger" + ChatColor.RESET + " " + ChatColor.BLUE + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "|==========|" +
                ChatColor.WHITE + "\n\nThis Plugin was made with " + ChatColor.RED + "<3" + ChatColor.WHITE + " by " + ChatColor.GOLD + ChatColor.ITALIC +
                "Prism3" + ChatColor.RESET + " and " + ChatColor.GOLD + ChatColor.ITALIC + "thelooter" + ChatColor.AQUA + "\nspigotmc.org/resources/logger.94236\n\n" +
                ChatColor.BLUE + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "|============================|");

    }
}
