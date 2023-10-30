package me.prism3.logger.events.spy;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import java.util.List;
import java.util.stream.Collectors;

public class OnBookSpy implements Listener {

    private final String bookSpyMessage;

    public OnBookSpy() {
        this.bookSpyMessage = ChatColor.translateAlternateColorCodes('&',
                Main.getInstance().getConfig().getString("Spy-Features.Book-Spy.Message"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBookSpy(final PlayerEditBookEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || player.hasPermission(Data.loggerSpyBypass))
            return;

        final List<String> pageContent = event.getNewBookMeta().getPages();

        final String content = String.join(", ", pageContent); // Use String.join() for efficient string concatenation

        final List<Player> playersWithSpyPermission = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(Data.loggerSpy))
                .collect(Collectors.toList());

        for (Player spyPlayer : playersWithSpyPermission) {
            spyPlayer.sendMessage(bookSpyMessage
                    .replace("%player%", player.getName())
                    .replace("%content%", content.replace("\\", "\\\\")));
        }
    }
}
