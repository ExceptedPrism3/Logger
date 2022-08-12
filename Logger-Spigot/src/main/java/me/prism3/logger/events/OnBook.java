package me.prism3.logger.events;

import com.carpour.loggercore.database.entity.EntityPlayer;
import me.prism3.logger.Main;
import me.prism3.logger.events.spy.OnBookSpy;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
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
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnBook implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void bookEditing(final PlayerEditBookEvent event) {

        // Book Spy
        if (this.main.getConfig().getBoolean("Spy-Features.Book-Spy.Enable")) {

            new OnBookSpy().onBookSpy(event);

        }

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Book-Editing")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final String worldName = player.getWorld().getName();
            final BookMeta bookMeta = event.getNewBookMeta();
            final int pageCount = bookMeta.getPageCount();
            final List<String> pageContent = Collections.singletonList(bookMeta.getPages().toString().replace("\\", "\\\\"));
            String signature = bookMeta.getAuthor();

            final EntityPlayer entityPlayer = new EntityPlayer(playerName, playerUUID.toString());

            if (!event.isSigning()) signature = "no one";

            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Book-Editing-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%page%", String.valueOf(pageCount)).replace("%content%", String.valueOf(pageContent)).replace("%sign%", String.valueOf(signature)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBookEditingFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Book-Editing")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%page%", String.valueOf(pageCount)).replace("%content%", String.valueOf(pageContent)).replace("%sign%", String.valueOf(signature)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Book-Editing-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Book-Editing-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%page%", String.valueOf(pageCount)).replace("%content%", String.valueOf(pageContent)).replace("%sign%", String.valueOf(signature)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Book-Editing")).isEmpty()) {

                        this.main.getDiscord().bookEditing(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Book-Editing")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%page%", String.valueOf(pageCount)).replace("%content%", String.valueOf(pageContent)).replace("%sign%", String.valueOf(signature)), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertBookEditing(Data.serverName, entityPlayer, worldName, pageCount, pageContent, signature, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertBookEditing(Data.serverName, entityPlayer, worldName, pageCount, pageContent, signature, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
