package me.prism3.logger.Discord;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class DiscordFile {

    private static File discordFile;
    private static FileConfiguration file;

    public static void Setup(){

        discordFile = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Logger")).getDataFolder(), "discord.yml");

        if(!discordFile.exists()){

            Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Logger")).saveResource("discord.yml", false);

        }

        file = YamlConfiguration.loadConfiguration(discordFile);

    }

    public static FileConfiguration get(){ return file; }

    /*public static void save() {

        try {

            file.save(discordFile);

        } catch (IOException e) {

            Bukkit.getLogger().warning(ChatColor.RED + "Could not save into the messages file!");
            e.printStackTrace();

        }
    }*/

    public static void reload(){

        file = YamlConfiguration.loadConfiguration(discordFile);

    }
}
