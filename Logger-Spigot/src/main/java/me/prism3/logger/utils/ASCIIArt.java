package me.prism3.logger.utils;

public class ASCIIArt {

    public void art() {

        final String reset = "\u001B[0m";
        final String purple = "\u001B[35m";
        final String aqua = "\u001B[36m";
        final String red = "\u001B[31m";
        final String yellow = "\u001B[33m";
        final String gold = "\u001B[33m";
        final String white = "\u001B[37m";
        final String blue = "\u001B[34m";

        Log.info(purple + "\n|\n" +
                purple + "|" + aqua + "     __                               \n" +
                purple + "|" + aqua + "    / /   ____  ____ _____ ____  _____\n" +
                purple + "|" + aqua + "   / /   / __ \\/ __ `/ __ `/ _ \\/ ___/\n" +
                purple + "|" + aqua + "  / /___/ /_/ / /_/ / /_/ /  __/ /    \n" +
                purple + "|" + aqua + " /_____/\\____/\\__, /\\__, /\\___/_/     \n" +
                purple + "|" + aqua + "             /____//____/     " + red + Data.pluginVersion + yellow + " [ Bukkit Version ]        \n" +
                purple + "|\n" +
                purple + "|" + gold + " This is a DEV Build, please report any issues!\n" + purple + "|\n" +
                purple + "|" + white + " Discord " + blue + "https://discord.gg/MfR5mcpVfX\n" +
                purple + "|"+ reset);
    }
}
