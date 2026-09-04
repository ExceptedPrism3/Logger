package me.prism3.logger.events.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.enums.LogType;

import java.util.HashMap;
import java.util.Map;


public class StartListener {

    private final LoggerAPI plugin;

    public StartListener(final LoggerAPI plugin) {
        this.plugin = plugin;
        this.onServerStart();
    }

    private void onServerStart() {
        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("event_type", "SERVER_START");
        this.plugin.getLoggerManager().logEvent(LogType.SERVER_START, null, placeholders);
    }
}
