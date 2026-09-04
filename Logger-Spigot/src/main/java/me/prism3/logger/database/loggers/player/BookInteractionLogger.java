package me.prism3.logger.database.loggers.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.database.DatabasePlayerLogger;
import me.prism3.logger.database.loggers.AbstractLogger;
import me.prism3.logger.managers.PermissionManager;
import org.bukkit.entity.Player;

import java.util.Map;


/**
 * Logs player book interaction unlock events to the database.
 */
public class BookInteractionLogger extends AbstractLogger implements DatabasePlayerLogger {

    public BookInteractionLogger(final LoggerAPI plugin) { super(plugin); }

    /**
     * Logs a player book interaction event to the database.
     *
     * @param player      The player who triggered the event.
     * @param placeholders A map of placeholders containing event details.
     */
    @Override
    public void logEvent(final Player player, final Map<String, String> placeholders) {

        final String sql = "INSERT INTO player_book_interaction (date, server_name, player_uuid, player_name, world_name, location_x, location_y, location_z, book_title, author, page_count, first_page_snippet, is_staff) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql, ps -> {
            setPlayerFields(ps, player);
            ps.setString (9, placeholders.get("book_title"));
            ps.setString (10, placeholders.get("author"));
            ps.setInt    (11, Integer.parseInt(placeholders.get("page_count")));
            ps.setString (12, placeholders.get("first_page_snippet"));
            ps.setBoolean(13, PermissionManager.canLogStaff(player));
        });
    }
}
