package me.prism3.logger.discord;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class DiscordFile {

    private File theDiscordFile;
    private FileConfiguration file;

    public void setup() {

        this.theDiscordFile = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Logger")).getDataFolder(), "discord.yml");

        if (!this.theDiscordFile.exists()) {

            Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Logger")).saveResource("discord.yml", false);

        }

        this.file = YamlConfiguration.loadConfiguration(this.theDiscordFile);

    }

    public FileConfiguration get() { return this.file; }

    public void reload() {

        this.file = YamlConfiguration.loadConfiguration(this.theDiscordFile);

    }
}
