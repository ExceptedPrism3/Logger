package com.carpour.logger.Events.Spy;

import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OnSignSpy implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignSpy(SignChangeEvent event) {

        Player player = event.getPlayer();

        if (player.hasPermission("logger.exempt") || player.hasPermission("logger.spy.bypass")) return;

        if (main.getConfig().getBoolean("Log-Player.Sign-Text") && main.getConfig().getBoolean("Spy-Features.Sign-Spy.Enable")) {

            List<String> lines = Arrays.asList(event.getLines());

            for (Player players : Bukkit.getOnlinePlayers()) {

                if (players.hasPermission("logger.spy")) {

                    players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(main.getConfig().getString("Spy-Features.Sign-Spy.Message")).
                                    replace("%player%", player.getName()).
                                    replace("%line1%", lines.get(0).replace("\\", "\\\\")).
                                    replace("%line2%", lines.get(1).replace("\\", "\\\\")).
                                    replace("%line3%", lines.get(2).replace("\\", "\\\\")).
                                    replace("%line4%", lines.get(3).replace("\\", "\\\\"))));

                }
            }
        }
    }
}
