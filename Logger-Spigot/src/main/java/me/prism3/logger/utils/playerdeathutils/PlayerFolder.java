package me.prism3.logger.utils.playerdeathutils;

import me.prism3.logger.utils.FileHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import static me.prism3.logger.utils.Data.allowedBackups;

public class PlayerFolder {

    private File playerFile;

    // Create the backup file for the appropriate player
    public void create(Player player) {
        final Date date = new Date();
        final SimpleDateFormat filenameDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        final File backupFolder = new File(FileHandler.getPlayerDeathBackupLogFolder(), player.getName());

        this.playerFile = new File(backupFolder, filenameDateFormat.format(date) + ".yml");

        try {
            if (!backupFolder.exists()) {
                Files.createDirectories(backupFolder.toPath());
            }
            this.playerFile = Files.createFile(this.playerFile.toPath()).toFile();
            this.fileInput(this.playerFile, player);

        } catch (final IOException e) { e.printStackTrace(); }
    }

    // Grabs the file from the above method and writes into it
    private void fileInput(File file, Player player) {

        try (FileOutputStream fos = new FileOutputStream(file)) {

            final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

            yaml.set("player-name", player.getName());
            yaml.set("player-uuid", player.getUniqueId().toString());
            yaml.set("world", player.getWorld().getName());
            yaml.set("x", player.getLocation().getBlockX());
            yaml.set("y", player.getLocation().getBlockY());
            yaml.set("z", player.getLocation().getBlockZ());
            yaml.set("xp", player.getLevel());
            yaml.createSection("inventory");
            yaml.createSection("armor");

            yaml.save(fos.toString());

        } catch (IOException e) { e.printStackTrace(); }
    }

    // Counts the total backup files of a player
    public static int backupCount(Player player) {
        final File backupFolder = new File(FileHandler.getPlayerDeathBackupLogFolder(), player.getName());
        return backupFolder.listFiles() == null ? 0 : backupFolder.listFiles().length;
    }

    // Stores all backup files names
    public static String[] fileNames(Player player) {

        final File dir = new File(FileHandler.getPlayerDeathBackupLogFolder(), player.getName());
        final String[] fileNames = dir.list((file, name) -> name.endsWith(".yml"));

        if (fileNames != null) {
            for (int i = 0; i < fileNames.length; i++)
                fileNames[i] = fileNames[i].replace(".yml", "");

        } else { return new String[0]; }

        return fileNames;
    }


    // Check if it's allowed to make a new backup file
    public boolean isAllowed(Player player) {
        return backupCount(player) < allowedBackups; // We did '<' since backCount method starts at 0
    }

    public File getPlayerFile() { return this.playerFile; }
}
