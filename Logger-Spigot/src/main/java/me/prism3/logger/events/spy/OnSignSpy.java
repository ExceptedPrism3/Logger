package me.prism3.logger.events.spy;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OnSignSpy implements Listener {

    private final String signSpyMessage;

    public OnSignSpy() {
        this.signSpyMessage = ChatColor.translateAlternateColorCodes('&',
                Main.getInstance().getConfig().getString("Spy-Features.Sign-Spy.Message"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignSpy(final SignChangeEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || player.hasPermission(Data.loggerSpyBypass))
            return;

        final List<String> lines = Arrays.asList(event.getLines());

        if (lines.size() < 4)
            return;

        final String line1 = lines.get(0).replace("\\", "\\\\");
        final String line2 = lines.get(1).replace("\\", "\\\\");
        final String line3 = lines.get(2).replace("\\", "\\\\");
        final String line4 = lines.get(3).replace("\\", "\\\\");

        final List<Player> playersWithSpyPermission = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(Data.loggerSpy))
                .collect(Collectors.toList());

        for (Player spyPlayer : playersWithSpyPermission) {
            spyPlayer.sendMessage(signSpyMessage
                    .replace("%player%", player.getName())
                    .replace("%line1%", line1)
                    .replace("%line2%", line2)
                    .replace("%line3%", line3)
                    .replace("%line4%", line4));
        }
    }
}
