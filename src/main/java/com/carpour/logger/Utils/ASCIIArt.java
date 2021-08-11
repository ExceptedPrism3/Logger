package com.carpour.logger.Utils;

import com.carpour.logger.Main;
import org.bukkit.ChatColor;

public class ASCIIArt {

    private final Main main = Main.getInstance();

    public void Art(){

        String Version = main.getDescription().getVersion();

        System.out.println(ChatColor.DARK_PURPLE + "|\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "     __                               \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "    / /   ____  ____ _____ ____  _____\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "   / /   / __ \\/ __ `/ __ `/ _ \\/ ___/\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "  / /___/ /_/ / /_/ / /_/ /  __/ /    \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + " /_____/\\____/\\__, /\\__, /\\___/_/     \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "             /____//____/     " + ChatColor.RED + Version + "        \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.WHITE + " Discord " + ChatColor.BLUE + "https://discord.gg/MfR5mcpVfX\n" +
                ChatColor.DARK_PURPLE + "|");

    }
}
