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

            if (event.getPreviousServer() != null) return; // Only log initial login? Velocity logic check needed. 
            // Original code likely checked if previous server was null to detect network join.
            // Bungee logic usually handles switch vs join. 
            // Velocity 'ServerPostConnectEvent' fires on every server switch.
            // Original code:
            /*
            if (event.getPreviousServer() == null) {
               // ...
            }
            */
            // Replacing logic assuming original intent was strictly Join (previous == null).
            if (event.getPreviousServer() != null) return;

            if (player.hasPermission(loggerExempt)) return;

            final String server = player.getCurrentServer().get().getServerInfo().getName();
            
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("player", player.getUsername());
            placeholders.put("server", server);
            placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));
            if (isPlayerIP) {
                placeholders.put("IP", player.getRemoteAddress().getHostString());
            }

            // Fix for OnLogin: "placeholders" needs "IP" if enabled.
            // Original code: 
            /*
            main.getMessages().getString("Discord.Player-Login").replace("%IP%", isPlayerIP ? player.getRemoteAddress().getHostString() : "")
            */
            if (!placeholders.containsKey("IP")) placeholders.put("IP", "");

            boolean isStaff = isStaffEnabled && player.hasPermission(loggerStaffLog);

            main.getLogManager().logPlayerEvent("Player-Login", player, placeholders, isStaff);
        }
    }
}
