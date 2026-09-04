package me.prism3.logger.events.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;

import java.util.HashMap;
import java.util.Map;


public class RconCommandListener implements Listener { //TODO TO TEST

    private final LoggerAPI plugin;

    public RconCommandListener(final LoggerAPI plugin) { this.plugin = plugin; }

    @EventHandler
    public void onRconCommandExecute(final RemoteServerCommandEvent event) {

        if (event.isCancelled())
            return;

        final Map<String, String> placeholders = new HashMap<>();

        placeholders.put("command", event.getCommand());
        placeholders.put("sender", event.getSender().getServer().getIp());

        this.plugin.getLoggerManager().logEvent(LogType.SERVER_RCON_COMMAND, null, placeholders);
    }
}
