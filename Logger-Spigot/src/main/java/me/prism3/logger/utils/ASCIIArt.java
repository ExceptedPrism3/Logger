package me.prism3.logger.utils;

import me.prism3.logger.Main;
import org.bukkit.ChatColor;

public class ASCIIArt {

    private final Main main = Main.getInstance();

    public void art() {

        main.getLogger().info(ChatColor.DARK_PURPLE + "\n|\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "     __                               \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "    / /   ____  ____ _____ ____  _____\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "   / /   / __ \\/ __ `/ __ `/ _ \\/ ___/\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "  / /___/ /_/ / /_/ / /_/ /  __/ /    \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + " /_____/\\____/\\__, /\\__, /\\___/_/     \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "             /____//____/     " + ChatColor.RED + Data.pluginVersion + ChatColor.YELLOW + " [ Bukkit Version ]        \n" +
                ChatColor.DARK_PURPLE + "|\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.GOLD + " This is a DEV Build, please report any issues!\n" + ChatColor.DARK_PURPLE + "|\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.WHITE + " Discord " + ChatColor.BLUE + "https://discord.gg/MfR5mcpVfX\n" +
                ChatColor.DARK_PURPLE + "|");

    }
}
