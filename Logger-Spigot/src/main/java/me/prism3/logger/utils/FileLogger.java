package me.prism3.logger.utils;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Optimized FileLogger:
 *  - Buffered writers per LogType, with date rollover
 *  - Async daily cleanup of old files
 *  - Auto shutdown hook to close writers
 */
public class FileLogger {

    private final LoggerAPI plugin;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final Map<LogType, BufferedWriter> writers = new EnumMap<>(LogType.class);
    private String currentDate;
    private final int retentionDays;

    public FileLogger(final LoggerAPI plugin) {
        this.plugin = plugin;
        this.retentionDays = plugin.getData().getFileDeletionSettings().days;
        this.currentDate = dateFormat.format(new Date());

        // Schedule daily async cleanup
        if (retentionDays >= 0) {
            SchedulerAdapter.runAsyncTimer(plugin, this::deleteOldFiles, 0L, 24 * 60 * 60 * 20L);
        }

        // Ensure writers are closed on plugin disable
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onDisable(PluginDisableEvent e) {
                if (e.getPlugin().equals(plugin)) {
                    shutdown();
                }
            }
        }, plugin);
    }

    private void createFolders() {
        if (!plugin.getData().isLogToFile()) return;
        for (LogType type : LogType.values()) {
            if (!plugin.getData().isEnabled(type)) continue;
            File folder = new File(plugin.getDataFolder(), "logs/" + type.getFolderName());
            if (!folder.exists()) folder.mkdirs();
        }
    }

    private synchronized BufferedWriter getWriter(LogType type) throws IOException {
        String today = dateFormat.format(new Date());
        if (!today.equals(currentDate)) {
            // rollover: close all and reset
            for (BufferedWriter w : writers.values()) {
                try { w.close(); } catch (IOException ignored) {}
            }
            writers.clear();
            currentDate = today;
        }
        return writers.computeIfAbsent(type, t -> {
            try {
                File logFile = getLogFile(t);
                return new BufferedWriter(new FileWriter(logFile, true));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private File getLogFile(LogType type) {
        String date = dateFormat.format(new Date());
        File folder = new File(plugin.getDataFolder(), "logs/" + type.getFolderName());
        if (!folder.exists()) folder.mkdirs();
        File logfile = new File(folder, date + ".log");
        if (!logfile.exists()) {
            try { logfile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        return logfile;
    }

    /**
     * Thread-safe logging to disk with buffered writer.
     */
    public void log(LogType type, String message) {
        try {
            BufferedWriter w = getWriter(type);
            String timestamp = new SimpleDateFormat(plugin.getData().getTimeFormatter()).format(new Date());
            w.write("[" + timestamp + "] " + message);
            w.newLine();
            w.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes log files older than retentionDays, runs async daily.
     */
    private void deleteOldFiles() {
        long cutoff = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(retentionDays);
        if (!plugin.getData().isLogToFile()) return;
        for (LogType type : LogType.values()) {
            if (!plugin.getData().isEnabled(type)) continue;
            File folder = new File(plugin.getDataFolder(), "logs/" + type.getFolderName());
            if (!folder.exists()) continue;
            File[] files = folder.listFiles();
            if (files == null) continue;
            for (File f : files) {
                if (f.isFile() && f.lastModified() < cutoff) {
                    f.delete();
                }
            }
        }
    }

    /**
     * Cleanly close all open writers.
     */
    public synchronized void shutdown() {
        for (BufferedWriter w : writers.values()) {
            try { w.close(); } catch (IOException ignored) {}
        }
        writers.clear();
    }
}