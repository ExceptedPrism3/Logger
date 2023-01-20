package me.prism3.logger.utils;

import org.bukkit.ChatColor;

import static me.prism3.logger.utils.Data.discordSupportServer;

public class ASCIIArt {

    private static final String ASCII_ART = "|\n" +
            "|" + ChatColor.AQUA + "     __                               \n" +
            "|" + ChatColor.AQUA + "    / /   ____  ____ _____ ____  _____\n" +
            "|" + ChatColor.AQUA + "   / /   / __ \\/ __ `/ __ `/ _ \\/ ___/\n" +
            "|" + ChatColor.AQUA + "  / /___/ /_/ / /_/ / /_/ /  __/ /    \n" +
            "|" + ChatColor.AQUA + " /_____/\\____/\\__, /\\__, /\\___/_/     \n" +
            "|" + ChatColor.AQUA + "             /____//____/     " + ChatColor.RED + Data.pluginVersion + ChatColor.YELLOW + " [ Bukkit Version ]        \n" +
            "|\n" +
            "|" + ChatColor.GOLD + " This is a DEV Build, please report any issues!\n" +
            "|\n" +
            "|" + ChatColor.WHITE + " Discord " + ChatColor.BLUE + discordSupportServer + "\n" +
            "|";

    public void art() { Log.info(ASCII_ART); }
}
