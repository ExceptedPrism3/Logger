package me.prism3.logger.Utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class Messages {

    private static File messagesFile;
    private static FileConfiguration file;

    public static void Setup(){

        messagesFile = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Logger")).getDataFolder(), "Messages/en.yml");

        if(!messagesFile.exists()){

            Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Logger")).saveResource("Messages/en.yml", false);

        }

        file = YamlConfiguration.loadConfiguration(messagesFile);

    }

    public static FileConfiguration get(){ return file; }

    /*public static void save() {

        try {

            file.save(messagesFile);

        } catch (IOException e) {

            Bukkit.getLogger().warning(ChatColor.RED + "Could not save the messages file!");
            e.printStackTrace();

        }
    }*/

    public static void reload(){

        file = YamlConfiguration.loadConfiguration(messagesFile);

    }
}
