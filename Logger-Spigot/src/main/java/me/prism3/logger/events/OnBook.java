package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnBook implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void bookEditing(final PlayerEditBookEvent event) {

        if (!event.isCancelled()) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final String worldName = player.getWorld().getName();
            final BookMeta bookMeta = event.getNewBookMeta();
            final int pageCount = bookMeta.getPageCount();
            final List<String> pageContent = Collections.singletonList(bookMeta.getPages().toString().replace("\\", "\\\\"));
            String signature = bookMeta.getAuthor();

            if (!event.isSigning()) signature = "no one";

            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Book-Editing-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%page%", String.valueOf(pageCount)).replace("%content%", String.valueOf(pageContent)).replace("%sign%", String.valueOf(signature)).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBookEditingFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Book-Editing").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%page%", String.valueOf(pageCount)).replace("%content%", String.valueOf(pageContent)).replace("%sign%", String.valueOf(signature)).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Book-Editing-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Book-Editing-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%page%", String.valueOf(pageCount)).replace("%content%", String.valueOf(pageContent)).replace("%sign%", String.valueOf(signature)).replace("%uuid%", playerUUID.toString()), false);
                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Book-Editing").isEmpty()) {

                        this.main.getDiscord().bookEditing(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Book-Editing").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%page%", String.valueOf(pageCount)).replace("%content%", String.valueOf(pageContent)).replace("%sign%", String.valueOf(signature)).replace("%uuid%", playerUUID.toString()), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueBookEditing(Data.serverName, playerName, playerUUID.toString(), worldName, pageCount, pageContent, signature, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueBookEditing(Data.serverName, playerName, playerUUID.toString(), worldName, pageCount, pageContent, signature, player.hasPermission(loggerStaffLog));

                } catch (final Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
