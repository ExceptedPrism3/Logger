package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import java.util.Map;


/**
 * Logs player interactions with books.
 * This includes editing the book's title, author, and content.
 */
public class BookInteractionListener implements Listener {

    private final LoggerAPI plugin;

    public BookInteractionListener(final LoggerAPI plugin) { this.plugin = plugin; }

    /**
     * Handles player book edit events.
     * Logs the book's title, author, page count, and a snippet of the first page.
     *
     * @param event the PlayerEditBookEvent to handle
     */
    @EventHandler
    public void onBookEdit(final PlayerEditBookEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, String> placeholders = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        // title may be null/empty for unsigned books
        final String title = event.getNewBookMeta().getTitle();
        placeholders.put("book_title", title != null ? title : "Untitled");
        placeholders.put("author", event.getNewBookMeta().getAuthor() != null ? event.getNewBookMeta().getAuthor() : "Unknown");

        final int pages = event.getNewBookMeta().getPages().size();

        placeholders.put("page_count", String.valueOf(pages));

        // for brevity only first page snippet
        final String firstPage = event.getNewBookMeta().getPages().isEmpty()
                ? ""
                : event.getNewBookMeta().getPages().get(0).substring(0, Math.min(30, event.getNewBookMeta().getPages().get(0).length()));

        placeholders.put("first_page_snippet", firstPage);

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_BOOK_INTERACTION, player, placeholders);
    }
}
