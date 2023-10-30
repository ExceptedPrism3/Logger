package me.prism3.logger.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;


/**
 * The Colorizer class provides utility methods for colorizing text using ChatColor codes.
 */
public final class Colorizer {

    /**
     * Colorizes the provided text by replacing '&' with the ChatColor color code.
     *
     * @param text The text to colorize.
     * @return The colorized text.
     */
    public static String colorize(String text) { return ChatColor.translateAlternateColorCodes('&', text); }

    /**
     * Colorizes the provided text lines by replacing '&' with the ChatColor color code.
     *
     * @param loreList The line to colorize.
     * @return The colorized line.
     */
    public static List<String> colorizeLore(final List<String> loreList) { return loreList.stream().map(Colorizer::colorize).collect(Collectors.toList()); }
}
