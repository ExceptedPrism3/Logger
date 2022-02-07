package com.carpour.logger.Events.Spy;

import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import java.util.List;
import java.util.Objects;

public class OnBookSpy implements Listener{

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBookSpy(PlayerEditBookEvent event) {

        Player player = event.getPlayer();

        if (player.hasPermission("logger.exempt") || player.hasPermission("logger.spy.bypass")) return;

        //Book Spy
        if (main.getConfig().getBoolean("Log-Player.Book-Editing") && main.getConfig().getBoolean("Spy-Features.Book-Spy.Enable")) {

            List<String> pageContent = event.getNewBookMeta().getPages();

            for (Player players : Bukkit.getOnlinePlayers()) {

                if (players.hasPermission("logger.spy")) {

                    players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(main.getConfig().getString("Spy-Features.Book-Spy.Message")).
                                    replace("%player%", player.getName()).
                                    replace("%content%", pageContent.toString())));

                }
            }
        }
    }
}
