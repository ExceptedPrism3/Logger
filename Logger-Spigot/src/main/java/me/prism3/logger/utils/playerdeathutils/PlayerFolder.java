package me.prism3.logger.utils.playerdeathutils;

import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.Player;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

import static me.prism3.logger.utils.Data.allowedBackups;

public class PlayerFolder {

    private final SimpleDateFormat filenameDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private File playerFile;

    // Create the backup file for the appropriate player
    public void create(final Player player) {

        final File backupFolder = new File(FileHandler.getPlayerDeathBackupLogFolder(), player.getName());

        if (!backupFolder.exists() && !backupFolder.mkdir()) return;

        this.playerFile = new File(backupFolder, this.filenameDateFormat.format(new Date()) + ".yml");

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile))) {

            final StringJoiner sj = new StringJoiner(System.lineSeparator());

            sj.add("player-name: " + player.getName());
            sj.add("player-uuid: " + player.getUniqueId());
            sj.add("world: " + player.getWorld().getName());
            sj.add("x: " + player.getLocation().getBlockX());
            sj.add("y: " + player.getLocation().getBlockY());
            sj.add("z: " + player.getLocation().getBlockZ());
            sj.add("xp: " + player.getLevel());
            sj.add("inventory:");
            sj.add("armor:");
            writer.write(sj.toString());

        } catch (IOException e) { e.printStackTrace(); }
    }

    // Counts the total backup files of a player
    public static int backupCount(final Player player) {

        final File backupFolder = new File(FileHandler.getPlayerDeathBackupLogFolder(), player.getName());
        final File[] files = backupFolder.listFiles(File::isFile);

        return files == null ? 0 : files.length;
    }

    // Stores all backup files names
    public static String[] fileNames(final Player player) {

        final int backupCount = backupCount(player);

        if (backupCount == 0) return new String[0];

        final File dir = new File(FileHandler.getPlayerDeathBackupLogFolder(), player.getName());
        final String[] fileNames = dir.list();

        if (fileNames == null) return new String[0];

        final String[] result = new String[backupCount];

        for (int i = 0; i < backupCount; i++)
            result[i] = fileNames[i].replace(".\\w+", "");

        return result;
    }

    // Check if it's allowed to make a new backup file
    public boolean isAllowed(final Player player) {
        return backupCount(player) < allowedBackups; // We did '<' since backCount method starts at 0
    }

    public File getPlayerFile() { return this.playerFile; }
}
