package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Logger;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnSwitch {

    @Subscribe
    public void onSwitch(ServerConnectedEvent event) {

        if (!isServerSwitch) return;

        final Logger main = Logger.getInstance();
        final Player player = event.getPlayer();

        final String playerName = player.getUsername();
        final String toServer = event.getServer().getServerInfo().getName();
        final String fromServer = event.getPreviousServer().map(server -> server.getServerInfo().getName()).orElse("Unknown");

        // We only care if they are switching FROM a server, not initial connection (which is OnLogin)
        if (event.getPreviousServer().isPresent()) {
            
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("player", playerName);
            placeholders.put("toServer", toServer);
            placeholders.put("fromServer", fromServer);
            placeholders.put("server", toServer); // Current server for context
            placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));

            boolean isStaff = isStaffEnabled && player.hasPermission(loggerStaffLog);

            if (!player.hasPermission(loggerExempt)) {
                main.getLogManager().logPlayerEvent("Player-Server-Switch", player, placeholders, isStaff);
            }
        }
    }
}
