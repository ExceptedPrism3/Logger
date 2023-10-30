package me.prism3.logger.utils.playerdeathutils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import me.prism3.logger.utils.enums.LogCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerFolder {
    private static final SimpleDateFormat FILENAME_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private final File playerFile;

    public PlayerFolder(final Player player) {

        final File backupFolder = new File(LogCategory.PLAYER_DEATH.getFolderName(), player.getName());

        if (!backupFolder.exists() && !backupFolder.mkdir()) {
            this.playerFile = null;
            return;
        }

        final String fileName = FILENAME_DATE_FORMAT.format(new Date()) + ".yml";
        this.playerFile = new File(backupFolder, fileName);

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(this.playerFile))) {
            writer.write(String.format("player-name: %s%nplayer-uuid: %s%ncause: %s%nworld: %s%nx: %d%ny: %d%nz: %d%nxp: %d%ninventory:%narmor:",
                    player.getName(),
                    player.getUniqueId(),
                    player.getLastDamageCause().getCause().name(),
                    player.getWorld().getName(),
                    player.getLocation().getBlockX(),
                    player.getLocation().getBlockY(),
                    player.getLocation().getBlockZ(),
                    player.getLevel()));
        } catch (IOException e) { e.printStackTrace(); }
    }

    // Stores all backup files names
    /*public static String[] fileNames(final String playerName) {

        final int backupCount = backupCount(playerName);

        if (backupCount == 0) return new String[0];

        final File dir = new File(FileHandler.getPlayerDeathBackupLogFolder(), playerName);
        final String[] fileNames = dir.list();

        if (fileNames == null) return new String[0];

        return Arrays.stream(fileNames)
                .map(name -> name.replace(".\\w+", ""))
                .toArray(String[]::new);
    }*/

    // Formats the given the String to better readable date format
    public static String formatFileName(final String fileName) {
        try {
            final Date date = FILENAME_DATE_FORMAT.parse(fileName.replace(".yml", ""));
            return OUTPUT_DATE_FORMAT.format(date);
        } catch (final ParseException e) {
            return fileName;
        }
    }

    public File getPlayerFile() { return this.playerFile; }

    public static UUID getUUID(final String playerName, final String fileName) {

        final FileConfiguration as = YamlConfiguration.loadConfiguration(new File(LogCategory.PLAYER_DEATH.getFolderName() + File.separator + playerName, fileName));

        return UUID.fromString(as.getString("player-uuid"));
    }
}