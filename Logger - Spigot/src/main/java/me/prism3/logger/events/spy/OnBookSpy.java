package me.prism3.logger.events.spy;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
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
    public void onBookSpy(final PlayerEditBookEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Book-Editing")
                && this.main.getConfig().getBoolean("Spy-Features.Book-Spy.Enable")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || player.hasPermission(Data.loggerSpyBypass)) return;

            final List<String> pageContent = event.getNewBookMeta().getPages();

            for (Player players : Bukkit.getOnlinePlayers()) {

                if (players.hasPermission(Data.loggerSpy)) {

                    players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(this.main.getConfig().getString("Spy-Features.Book-Spy.Message")).
                                    replace("%player%", player.getName()).
                                    replace("%content%", pageContent.toString().replace("\\", "\\\\"))));

                }
            }
        }
    }
}
