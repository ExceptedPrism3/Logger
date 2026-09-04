package me.prism3.logger.events.server;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.HashMap;
import java.util.Map;


/**
 * Listens for console commands and logs them.
 */
public class ConsoleCommandListener implements Listener {

    private final LoggerAPI plugin;

    public ConsoleCommandListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Log console commands.
     *
     * @param event the event
     */
    @EventHandler
    public void onConsoleCommand(final ServerCommandEvent event) {

        // Check if the event is cancelled or if the sender is a BlockCommandSender
        if (event.isCancelled() || event.getSender() instanceof BlockCommandSender)
            return;

        final Map<String, String> placeholders = new HashMap<>();

        placeholders.put("command", event.getCommand());

        this.plugin.getLoggerManager().logEvent(LogType.SERVER_CONSOLE_COMMAND, null, placeholders);
    }
}
