package me.prism3.logger_bungee.managers;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Constants;
import me.prism3.logger_bungee.utils.Log;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileManager {
    private final LoggerBungee plugin;
    private final SimpleDateFormat dateFormat;
    private final File logFolder;
    private final SimpleDateFormat fileDateFormat;
    private final ExecutorService executor;

    public FileManager(LoggerBungee plugin) {
        this.plugin = plugin;
        Configuration config = plugin.getConfigManager().getConfig();
        this.dateFormat = new SimpleDateFormat(config.getString("Time-Formatter", "yyyy-MM-dd HH:mm:ss"));
        this.fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.logFolder = new File(plugin.getDataFolder(), "logs");
        this.executor = Executors.newSingleThreadExecutor();
        this.initialize();
    }

    private void initialize() {
        Configuration config = plugin.getConfigManager().getConfig();
        if (!config.getBoolean("Log-to-Files", true)) {
            return;
        }

        if (!this.logFolder.exists()) {
            this.logFolder.mkdirs();
        }
    }

    public void logToFile(String type, String message) {
        Configuration config = plugin.getConfigManager().getConfig();
        if (!config.getBoolean("Log-to-Files", true))
            return;

        this.executor.submit(() -> {
            try {
                // Create a folder for the event type if it doesn't exist
                File typeFolder = new File(this.logFolder, type);
                if (!typeFolder.exists()) {
                    typeFolder.mkdirs();
                }

                // Create log file with current date
                String date = this.fileDateFormat.format(new Date());
                File logFile = new File(typeFolder, date + ".log");

                try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
                    writer.println(message);
                }
            } catch (IOException e) {
                Log.severe("Failed to write to log file: " + e.getMessage());
            }
        });
    }

    public void deleteOldLogs() {
        this.executor.submit(() -> {
            Configuration config = plugin.getConfigManager().getConfig();
            int deleteAfterDays = config.getInt("File-Deletion", 7);
            if (deleteAfterDays < 0)
                return;

            long deleteAfter = deleteAfterDays * 24 * 60 * 60 * 1000L; // Convert days to milliseconds
            long currentTime = System.currentTimeMillis();

            // Delete old logs from all type folders
            File[] typeFolders = this.logFolder.listFiles(File::isDirectory);
            if (typeFolders == null)
                return;

            for (File typeFolder : typeFolders) {
                File[] logFiles = typeFolder.listFiles((dir, name) -> name.endsWith(".log"));
                if (logFiles == null)
                    continue;

                for (File file : logFiles) {
                    if (currentTime - file.lastModified() > deleteAfter) {
                        if (!file.delete()) {
                            Log.warn("Failed to delete old log file: " + file.getName());
                        }
                    }
                }
            }
        });
    }

    public void reload() {
        this.deleteOldLogs();
    }

    public void shutdown() {
        this.executor.shutdown();
        try {
            if (!this.executor.awaitTermination(5, TimeUnit.SECONDS)) {
                this.executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.executor.shutdownNow();
        }
    }
}
