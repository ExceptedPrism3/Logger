package com.carpour.logger.Discord;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DiscordFile {

    private static File discordFile;
    private static FileConfiguration file;

    public static void Setup(){

        discordFile = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Logger")).getDataFolder(), "discord.yml");

        if(!discordFile.exists()){

            try {

                discordFile.createNewFile();

            }catch (IOException E){

                E.printStackTrace();

            }

        }

        file = YamlConfiguration.loadConfiguration(discordFile);

    }

    public static FileConfiguration get(){ return file; }

    public static void save() {

        try {

            file.save(discordFile);

        } catch (IOException e) {

            Bukkit.getLogger().warning(ChatColor.RED + "Could not save the discord file!");
            e.printStackTrace();

        }
    }

    public static void reload(){

        file = YamlConfiguration.loadConfiguration(discordFile);

    }

    public static void values(){

        file.options().header("#Discord Bridge Configuration for Logger Plugin\n\n#Need Support? Join the Discord Server: https://discord.gg/MfR5mcpVfX\n\n");
        get().addDefault("Discord.Enable" , false);
        get().addDefault("Discord.Bot-Token", "BOT_KEY");
        get().addDefault("Discord.Staff.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Player-Chat.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Player-Commands.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Console-Commands.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Player-Sign-Text.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Player-Join.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Player-Leave.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Player-Kick.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Player-Death.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Player-Teleport.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Player-Level.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Block-Place.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Block-Break.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Portal-Creation.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Bucket-Place.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Anvil.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.TPS.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.RAM.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Server-Start.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Server-Stop.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Item-Pickup.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Item-Drop.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Enchanting.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Book-Editing.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.AFK.Channel-ID", "LINK_HERE");
        get().addDefault("Discord.Furnace.Channel-ID", "LINK_HERE");
    }
}
