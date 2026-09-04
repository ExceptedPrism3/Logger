package me.prism3.logger.managers;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabaseLoggerFactory;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.DatabaseServerLogger;
import me.prism3.logger.utils.enums.LogType;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Optimized LoggerManager:
 * - Async file logging via a dedicated worker thread & queue
 * - Async Discord & Database writes
 */
public class LoggerManager {

    private final LoggerAPI plugin;
    private final BlockingQueue<LogTask> queue = new LinkedBlockingQueue<>();
    private final ExecutorService discordExecutor;
    private Thread fileWorkerThread;

    public LoggerManager(final LoggerAPI plugin) {
        this.plugin = plugin;
        this.discordExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "Logger-Discord-Worker");
            t.setDaemon(true);
            return t;
        });
        startWorker();
    }

    private void startWorker() {
        this.fileWorkerThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    LogTask task = queue.take();
                    plugin.getFileLogger().log(task.logType, task.message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "LoggerManager-FileWriter");
        this.fileWorkerThread.setDaemon(true);
        this.fileWorkerThread.start();
    }

    public void logEvent(final LogType logType, final Player player, final Map<String, String> placeholders) {
        boolean isStaff = player != null && PermissionManager.canLogStaff(player);
        if (!logType.isEnabled(plugin.getConfig()))
            return;

        // Format once for file
        String fileMsg = plugin.getMessageManager().getMessage(logType, isStaff, placeholders, 'F', player);
        if (plugin.getData().isLogToFile()) {
            queue.offer(new LogTask(isStaff ? LogType.STAFF : logType, fileMsg));
        }

        // Async Discord
        me.prism3.logger_core.discord.DiscordManager discordManager = plugin.getDiscordManager();
        if (discordManager != null && discordManager.isEnabled()) {
            String discordMsg = plugin.getMessageManager().getMessage(logType, isStaff, placeholders, 'D', player);
            final String eventType = (isStaff && plugin.getConfig().getBoolean("Staff.Enabled", false)) ? "STAFF" : logType.name();
            discordExecutor.submit(() -> {
                try {
                    me.prism3.logger_core.objects.LogPlayer corePlayer = null;
                    if (player != null) {
                        corePlayer = new me.prism3.logger_core.objects.LogPlayer(player.getName(), player.getUniqueId(),
                                plugin.getData().getServerName());
                    }
                    discordManager.sendMessage(eventType, discordMsg, corePlayer, logType.name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Async Database
        if (plugin.getData().getDatabaseSettings().enabled) {
            plugin.getDatabaseManager().submit(() -> {
                try {
                    Object dbLogger = DatabaseLoggerFactory.createLogger(logType, plugin);
                    if (dbLogger instanceof DatabasePlayerLogger) {
                        ((DatabasePlayerLogger) dbLogger).logEvent(player, placeholders);
                    } else if (dbLogger instanceof DatabaseServerLogger) {
                        ((DatabaseServerLogger) dbLogger).logEvent(placeholders);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void shutdown() {
        // Shutdown Discord executor
        discordExecutor.shutdown();
        try {
            if (!discordExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                discordExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            discordExecutor.shutdownNow();
        }

        // Shutdown file worker
        if (fileWorkerThread != null) {
            fileWorkerThread.interrupt();
        }
    }

    private static class LogTask {
        final LogType logType;
        final String message;

        LogTask(LogType logType, String message) {
            this.logType = logType;
            this.message = message;
        }
    }
}