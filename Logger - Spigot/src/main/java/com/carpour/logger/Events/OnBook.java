package com.carpour.logger.Events;

import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Events.Spy.OnBookSpy;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OnBook implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void bookEditing(PlayerEditBookEvent event){

        Player player = event.getPlayer();
        String playerName = player.getName();
        String worldName = player.getWorld().getName();
        int pageCount = event.getNewBookMeta().getPageCount();
        List<String> pageContent = event.getNewBookMeta().getPages();
        String signature = event.getNewBookMeta().getAuthor();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        //Book Spy
        if (main.getConfig().getBoolean("Spy-Features.Book-Spy.Enable")) {

            OnBookSpy bookSpy = new OnBookSpy();
            bookSpy.onBookSpy(event);

        }

        if (!event.isSigning()) signature = "no one";

        //Log To Files Handling
        if (!event.isCancelled() && main.getConfig().getBoolean("Log-Player.Book-Editing")) {

            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Book-Editing-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                        MySQLData.bookEditing(serverName, worldName, playerName, pageCount, pageContent, signature, true);

                    }
                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertBook(serverName, player, pageCount, pageContent, signature, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBookEditingFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Book-Editing")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                if (!Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff")).isEmpty()) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)), false);

                }

            } else {

                if (!Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing")).isEmpty()) {

                    Discord.bookEditing(player, Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%page%", String.valueOf(pageCount)).replaceAll("%content%", String.valueOf(pageContent)).replaceAll("%sign%", String.valueOf(signature)), false);
                }
            }

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.bookEditing(serverName, worldName, playerName, pageCount, pageContent, signature, false);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertBook(serverName, player, pageCount, pageContent, signature, player.hasPermission("logger.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
