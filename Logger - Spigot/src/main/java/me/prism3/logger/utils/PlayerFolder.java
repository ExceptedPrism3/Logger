package me.prism3.logger.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static me.prism3.logger.utils.Data.allowedBackups;

public class PlayerFolder {

    private static File playerFile;

    // Create the backup file for the appropriate player
    public void create(Player player) {

        final Date date = new Date();
        final SimpleDateFormat filenameDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        final File backupFolder = new File(FileHandler.getPlayerDeathBackupLogFolder(), player.getName());

        playerFile = new File(backupFolder, filenameDateFormat.format(date) + ".yml");

        try {

            if (!backupFolder.exists()) backupFolder.mkdir();

            playerFile.createNewFile();

            this.fileInput(playerFile, player);

        } catch (Exception e) { e.printStackTrace(); }
    }

    // Grabs the file from the above method and writes into it
    private void fileInput(File file, Player player) throws IOException, InvalidConfigurationException {

        final FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.load(file);

        yaml.set("Player-Name", player.getName());
        yaml.set("Player-UUID", player.getUniqueId().toString());
        yaml.set("World", player.getWorld().getName());
        yaml.set("X", player.getLocation().getBlockX());
        yaml.set("Y", player.getLocation().getBlockY());
        yaml.set("Z", player.getLocation().getBlockZ());
        yaml.set("XP", player.getLevel());
        yaml.createSection("Inventory");
        yaml.createSection("Armor");

        yaml.save(file);

    }

    // Counts the total backup files of a player
    public static int backupCount(Player player) {

        final File backupFolder = new File(FileHandler.getPlayerDeathBackupLogFolder(), player.getName());

        int backupCount = 0;

        if (backupFolder.getParentFile().list() != null) {
            try (Stream<Path> files = Files.list(Paths.get(String.valueOf(backupFolder)))) {
                long count = files.count();
                backupCount = (int) count;
            } catch (Exception ignored) {}
        }
        return backupCount;
    }

    // Stores all backup files names
    public static String[] fileNames(Player player) {

        if (backupCount(player) == 0) return null;

        File dir = new File(FileHandler.getPlayerDeathBackupLogFolder(), player.getName());

        Collection<String> files = new ArrayList<>();

        File[] listFiles = dir.listFiles();

        assert listFiles != null;

        for (File file : listFiles) {
            files.add(file.getName().replaceAll("\\.\\w+", ""));
        }

        return files.toArray(new String[0]);
    }

    // Check if it's allowed to make a new backup file
    public boolean isAllowed(Player player) {
        return backupCount(player) < allowedBackups; // We did '<' since backCount method starts at 0
    }

    public static File getPlayerFile() { return playerFile; }
}
