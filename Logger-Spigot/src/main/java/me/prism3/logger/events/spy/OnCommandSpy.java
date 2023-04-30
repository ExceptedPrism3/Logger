package me.prism3.logger.events.spy;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.stream.Collectors;

public class OnCommandSpy implements Listener {

    private final String commandSpyMessage;

    public OnCommandSpy() {
        this.commandSpyMessage = ChatColor.translateAlternateColorCodes('&',
                Main.getInstance().getConfig().getString("Spy-Features.Commands-Spy.Message"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCmdSpy(final PlayerCommandPreprocessEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || player.hasPermission(Data.loggerSpyBypass))
            return;

        final String command = event.getMessage().replace("\\", "\\\\");

        final List<Player> playersWithSpyPermission = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(Data.loggerSpy))
                .collect(Collectors.toList());

        for (Player spyPlayer : playersWithSpyPermission) {
            spyPlayer.sendMessage(commandSpyMessage
                    .replace("%player%", player.getName())
                    .replace("%cmd%", command));
        }
    }
}
