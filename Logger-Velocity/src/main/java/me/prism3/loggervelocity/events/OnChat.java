package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Logger;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnChat{

    @Subscribe
    public void onChat(final PlayerChatEvent event) {

        final Logger main = Logger.getInstance();
        final Player player = event.getPlayer();

        if (main.getConfig().getBoolean("Log-Player.Chat") && player.getCurrentServer().isPresent()) {

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getUsername();
            final String server = player.getCurrentServer().get().getServerInfo().getName();
            final String message = event.getMessage().replace("\\", "\\\\");

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("player", playerName);
            placeholders.put("server", server);
            placeholders.put("msg", message);
            placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));

            boolean isStaff = isStaffEnabled && player.hasPermission(loggerStaffLog);
            
            // Delegate to LogManager
            main.getLogManager().logPlayerEvent("Player-Chat", player, placeholders, isStaff);
        }
    }
}
