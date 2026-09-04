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
        ProxiedPlayer player = event.getPlayer();
        Map<String, String> placeholders = createPlayerPlaceholders(player);
        placeholders.put("from", event.getFrom() != null ? event.getFrom().getName() : "unknown");
        placeholders.put("to", player.getServer().getInfo().getName());
        logEvent(Constants.Events.PLAYER_SWITCH, player, placeholders);
    }
} 