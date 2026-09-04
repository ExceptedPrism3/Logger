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
 * Listens for command block executions and logs them.
 */
public class CommandBlockListener implements Listener {

    private final LoggerAPI plugin;

    public CommandBlockListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Listens for command block executions and logs them.
     *
     * @param event the event
     */
    @EventHandler
    public void onCommandBlockExecute(final ServerCommandEvent event) {

        // Only log command block executions
        if (!(event.getSender() instanceof BlockCommandSender) || event.isCancelled())
            return;

        final Map<String, String> placeholders = new HashMap<>();

        placeholders.put("command", event.getCommand());

        this.plugin.getLoggerManager().logEvent(LogType.SERVER_COMMAND_BLOCK, null, placeholders);
    }
}
