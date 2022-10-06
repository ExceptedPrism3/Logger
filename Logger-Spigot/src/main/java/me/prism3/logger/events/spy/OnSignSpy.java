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

public class OnSignSpy implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignSpy(final SignChangeEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || player.hasPermission(Data.loggerSpyBypass)) return;

        final List<String> lines = Arrays.asList(event.getLines());

        for (Player players : Bukkit.getOnlinePlayers()) {

            if (players.hasPermission(Data.loggerSpy)) {

                players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                this.main.getConfig().getString("Spy-Features.Sign-Spy.Message")).
                        replace("%player%", player.getName()).
                            replace("%line1%", lines.get(0).replace("\\", "\\\\")).
                                replace("%line2%", lines.get(1).replace("\\", "\\\\")).
                                    replace("%line3%", lines.get(2).replace("\\", "\\\\")).
                                        replace("%line4%", lines.get(3).replace("\\", "\\\\")));

            }
        }
    }
}
