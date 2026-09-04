package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Logger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnKick {

    @Subscribe
    public void onKick(KickedFromServerEvent event) {

        if (!isKick) return;

        final Logger main = Logger.getInstance();
        final Player player = event.getPlayer();

        // KickedFromServerEvent implies they were on a server.
        // We want to log the reason.
        
        Optional<net.kyori.adventure.text.Component> reasonComp = event.getServerKickReason();
        String reason = reasonComp.map(component -> LegacyComponentSerializer.legacyAmpersand().serialize(component)).orElse("Unknown Reason");

        final String playerName = player.getUsername();
        final String server = event.getServer().getServerInfo().getName();

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", playerName);
        placeholders.put("server", server);
        placeholders.put("reason", reason);
        placeholders.put("time", dateTimeFormatter.format(ZonedDateTime.now()));

        boolean isStaff = isStaffEnabled && player.hasPermission(loggerStaffLog);

        if (!player.hasPermission(loggerExempt)) {
            main.getLogManager().logPlayerEvent("Player-Kick", player, placeholders, isStaff);
        }
    }
}
