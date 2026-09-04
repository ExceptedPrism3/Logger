package me.prism3.logger_bungee.listeners.player;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.listeners.BaseListener;
import me.prism3.logger_bungee.utils.Constants;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class PlayerDisconnectListener extends BaseListener {

    public PlayerDisconnectListener(LoggerBungee plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Map<String, String> placeholders = createPlayerPlaceholders(player);
        logEvent(Constants.Events.PLAYER_LEAVE, player, placeholders);
    }
} 