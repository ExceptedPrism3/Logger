package me.prism3.logger_bungee.listeners.player;

import me.prism3.logger_bungee.LoggerBungee;
import me.prism3.logger_bungee.listeners.BaseListener;
import me.prism3.logger_bungee.utils.Constants;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class ServerKickListener extends BaseListener {

    public ServerKickListener(LoggerBungee plugin) {
        super(plugin);
    }

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        if (!(event.getPlayer() instanceof ProxiedPlayer))
            return;

        ProxiedPlayer player = event.getPlayer();
        String reason = BaseComponent.toLegacyText(event.getKickReasonComponent());
        String serverName = event.getKickedFrom().getName();

        Map<String, String> placeholders = createPlayerPlaceholders(player);
        placeholders.put("reason", reason);
        placeholders.put("server", serverName);

        // Log the kick event
        logEvent(Constants.Events.PLAYER_KICK, player, placeholders);
    }
}
