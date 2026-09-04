package me.prism3.logger_bungee.listeners.player;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.listeners.BaseListener;
import me.prism3.logger_bungee.utils.Constants;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class CommandListener extends BaseListener {

    public CommandListener(LoggerBungee plugin) {
        super(plugin);
    }

    @EventHandler
    public void onCommand(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer))
            return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (!event.getMessage().startsWith("/"))
            return;

        Map<String, String> placeholders = createPlayerPlaceholders(player);
        placeholders.put("command", event.getMessage());
        logEvent(Constants.Events.PLAYER_COMMAND, player, placeholders);
    }
}