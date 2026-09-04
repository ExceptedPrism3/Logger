package me.prism3.logger.events.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.enums.LogType;

import java.util.HashMap;
import java.util.Map;


public class StopListener {

    private final LoggerAPI plugin;

    public StopListener(final LoggerAPI plugin) {
        this.plugin = plugin;
        this.onServerStop();
    }

    public void onServerStop() {
        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("event_type", "SERVER_STOP");
        this.plugin.getLoggerManager().logEvent(LogType.SERVER_STOP, null, placeholders);
    }
}
