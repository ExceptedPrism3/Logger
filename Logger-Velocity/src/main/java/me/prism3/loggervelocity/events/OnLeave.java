package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Logger;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnLeave {

    @Subscribe
    public void onLeave(final DisconnectEvent event) {

        final Logger main = Logger.getInstance();
        final Player player = event.getPlayer();

        if (main.getConfig().getBoolean("Log-Player.Leave")) {

            if (player.hasPermission(loggerExempt)) return;

            final String server = player.getCurrentServer().isPresent() ? player.getCurrentServer().get().getServerInfo().getName() : "HUB"; // Fallback if kicked/disconnected before server? 
            // Original code:
            /*
             final String server = player.getCurrentServer().get().getServerInfo().getName();
             */
            // Velocity DisconnectEvent happens after disconnection. currentServer might be empty?
            // Assuming original code was correct, but adding safety.
            String serverName = "unknown";
            if (player.getCurrentServer().isPresent()) {
                serverName = player.getCurrentServer().get().getServerInfo().getName();
            }

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("player", player.getUsername());
            placeholders.put("server", serverName);
            placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));

            boolean isStaff = isStaffEnabled && player.hasPermission(loggerStaffLog);

            main.getLogManager().logPlayerEvent("Player-Leave", player, placeholders, isStaff);
        }
    }
}
