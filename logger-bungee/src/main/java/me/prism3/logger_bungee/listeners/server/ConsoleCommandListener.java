package me.prism3.logger_bungee.listeners.server;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ConsoleCommandListener {
    private final LoggerBungee plugin;

    public ConsoleCommandListener(LoggerBungee plugin) {
        this.plugin = plugin;
        this.setupFilter();
    }

    private void setupFilter() {
        Logger rootLogger = plugin.getProxy().getLogger();
        if (rootLogger == null) return;

        Filter oldFilter = rootLogger.getFilter();
        rootLogger.setFilter(new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {
                if (record != null && record.getMessage() != null) {
                    processMessage(record.getMessage());
                }
                return oldFilter == null || oldFilter.isLoggable(record);
            }
        });
    }

    private void processMessage(String message) {
        if (!plugin.getConfigManager().getConfig().getBoolean("Log-Server.Console-Commands", true)) {
            return;
        }

        // Detect console command logs in BungeeCord
        // E.g., "issued server command: /xyz" or "Command /xyz"
        if (message.contains("issued server command: /") || message.contains("issued command: /") || message.startsWith("Command /")) {
            String cmd = message;
            if (message.contains("command: /")) {
                cmd = message.substring(message.indexOf("command: /") + "command: /".length());
            } else if (message.contains("command: ")) {
                cmd = message.substring(message.indexOf("command: ") + "command: ".length());
            } else if (message.startsWith("Command /")) {
                cmd = message.substring("Command /".length());
            }

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("command", cmd);
            placeholders.put("log", cmd);

            plugin.getLogManager().logServerEvent(Constants.Events.SERVER_COMMANDS, placeholders);
        }
    }
}
