package com.carpour.logger.Events.Spy;

import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.Objects;

public class OnCommandSpy implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCmdSpy(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();

        if (player.hasPermission("logger.exempt") || player.hasPermission("logger.spy")) return;

        //Command Spy
        if (main.getConfig().getBoolean("Log-Player.Commands") && main.getConfig().getBoolean("Spy-Features.Commands-Spy.Enable")) {

            for (Player players : Bukkit.getOnlinePlayers()) {

                if (players.hasPermission("logger.spy")) {

                    players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(main.getConfig().getString("Spy-Features.Commands-Spy.Message")).
                                    replace("%player%", player.getName()).
                                    replace("%cmd%", event.getMessage())));

                }
            }
        }
    }
}
