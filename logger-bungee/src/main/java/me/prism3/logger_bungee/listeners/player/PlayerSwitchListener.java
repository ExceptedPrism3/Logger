package me.prism3.logger_bungee.listeners.player;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.listeners.BaseListener;
import me.prism3.logger_bungee.utils.Constants;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class PlayerSwitchListener extends BaseListener {

    public PlayerSwitchListener(LoggerBungee plugin) {
        super(plugin);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        if (event.getFrom() == null) {
            return; // Initial proxy join is handled by PlayerLoginListener
        }
        ProxiedPlayer player = event.getPlayer();
        String fromServer = event.getFrom().getName();
        String toServer = player.getServer() != null && player.getServer().getInfo() != null
                ? player.getServer().getInfo().getName()
                : "unknown";
        Map<String, String> placeholders = createPlayerPlaceholders(player);
        placeholders.put("from", fromServer);
        placeholders.put("to", toServer);
        placeholders.put("fromServer", fromServer);
        placeholders.put("toServer", toServer);
        placeholders.put("server", toServer);
        logEvent(Constants.Events.PLAYER_SWITCH, player, placeholders);
    }
} 