package me.prism3.logger.utils;

import me.prism3.logger.LoggerAPI;
import org.bukkit.ChatColor;

public class ASCIIArt {

    private final LoggerAPI plugin;

    public ASCIIArt(final LoggerAPI plugin) {
        this.plugin = plugin;
        this.printArt();
    }

    private void printArt() {
        String version = this.plugin.getData().getPluginVersion();
        String devMsg = "";
        if (version.toUpperCase().contains("SNAPSHOT") || version.toUpperCase().contains("DEV")) {
            devMsg = ChatColor.DARK_PURPLE + "|" + ChatColor.GOLD + " This is a DEV Build, please report any issues!\n"
                    +
                    ChatColor.DARK_PURPLE + "|\n";
        }

        String art = ChatColor.DARK_PURPLE + "\n|\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "     __                               \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "    / /   ____  ____ _____ ____  _____\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "   / /   / __ \\/ __ `/ __ `/ _ \\/ ___/\n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "  / /___/ /_/ / /_/ / /_/ /  __/ /    \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + " /_____/\\____/\\__, /\\__, /\\___/_/     \n" +
                ChatColor.DARK_PURPLE + "|" + ChatColor.AQUA + "             /____//____/     " +
                ChatColor.RED + version +
                ChatColor.YELLOW + " [ Bukkit Version ]        \n" +
                ChatColor.DARK_PURPLE + "|\n" +
                devMsg +
                ChatColor.DARK_PURPLE + "|" + ChatColor.WHITE + " Discord " + ChatColor.BLUE
                + "https://discord.gg/MfR5mcpVfX\n" +
                ChatColor.DARK_PURPLE + "|";

        // Convert the ChatColor codes (prefix "§") to ANSI escape codes.
        String ansiArt = convertChatColorsToANSI(art);
        // Use your Log utility to print.
        Log.info(ansiArt);
    }

    /**
     * Converts Bukkit's ChatColor codes (usually preceded by '§') to ANSI escape
     * sequences.
     * This is a simple replacement; adjust as needed to cover more colors.
     */
    private String convertChatColorsToANSI(String input) {
        if (input == null)
            return null;

        // Define mappings: ChatColor code to ANSI escape sequence.
        // You can extend this mapping to include additional colors.
        String result = input
                .replace(ChatColor.DARK_PURPLE.toString(), "\u001B[35m")
                .replace(ChatColor.AQUA.toString(), "\u001B[36m")
                .replace(ChatColor.RED.toString(), "\u001B[31m")
                .replace(ChatColor.YELLOW.toString(), "\u001B[33m")
                .replace(ChatColor.BLUE.toString(), "\u001B[34m")
                .replace(ChatColor.GOLD.toString(), "\u001B[33;1m") // Bold yellow
                .replace(ChatColor.WHITE.toString(), "\u001B[37m");
        // Append reset code at the end
        result += "\u001B[0m";
        return result;
    }
}
