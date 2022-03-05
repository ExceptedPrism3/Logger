package me.prism3.logger.Events;

import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Database.SQLite.SQLiteData;
import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Events.Spy.OnBookSpy;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.FileHandler;
import me.prism3.logger.Utils.Messages;
import me.prism3.logger.Utils.Data;
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
import java.util.Objects;

public class OnBook implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void bookEditing(final PlayerEditBookEvent event){

        //Book Spy
        if (this.main.getConfig().getBoolean("Spy-Features.Book-Spy.Enable")) {

            new OnBookSpy().onBookSpy(event);

        }

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Book-Editing")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt)) return;

            final String playerName = player.getName();
            final String worldName = player.getWorld().getName();
            final BookMeta bookMeta = event.getNewBookMeta();
            final int pageCount = bookMeta.getPageCount();
            final List<String> pageContent = Collections.singletonList(bookMeta.getPages().toString().replace("\\", "\\\\"));
            String signature = bookMeta.getAuthor();

            if (!event.isSigning()) signature = "no one";

            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Book-Editing-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.bookEditing(Data.serverName, worldName, playerName, pageCount, pageContent, signature, true);

                    }
                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertBook(Data.serverName, player, pageCount, pageContent, signature, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBookEditingFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Book-Editing")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing")).isEmpty()) {

                        Discord.bookEditing(player, Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.bookEditing(Data.serverName, worldName, playerName, pageCount, pageContent, signature, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertBook(Data.serverName, player, pageCount, pageContent, signature, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
