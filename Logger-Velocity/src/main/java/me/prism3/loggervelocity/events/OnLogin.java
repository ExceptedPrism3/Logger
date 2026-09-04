package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Logger;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnLogin {

    @Subscribe
    public void onLogin(final ServerPostConnectEvent event) {

        final Logger main = Logger.getInstance();
        final Player player = event.getPlayer();

        if (main.getConfig().getBoolean("Log-Player.Login")) {

            // Only log initial connection (when previousServer is null)
            if (event.getPreviousServer() != null) return;

            if (player.hasPermission(loggerExempt)) return;

            final String server = player.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("Unknown");
            
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("player", player.getUsername());
            placeholders.put("server", server);
            placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));
            if (isPlayerIP) {
                placeholders.put("IP", player.getRemoteAddress().getHostString());
            }

            if (!placeholders.containsKey("IP")) placeholders.put("IP", "");

            boolean isStaff = isStaffEnabled && player.hasPermission(loggerStaffLog);

            main.getLogManager().logPlayerEvent("Player-Login", player, placeholders, isStaff);
        }
    }
}
