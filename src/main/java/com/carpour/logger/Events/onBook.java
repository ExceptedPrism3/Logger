package com.carpour.logger.Events;

import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
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

public class onBook implements Listener {

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

        if (!event.isSigning()) signature = "no one";

        //Log To Files Handling
        if (!event.isCancelled()
                && main.getConfig().getBoolean("Log-to-Files")
                && main.getConfig().getBoolean("Log.Book-Editing")) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                Discord.staffChat(player, "\uD83D\uDCD6 **|** \uD83D\uDC6E\u200D♂️ has edited a book that consists of **" + pageCount + "** page(s), with **" + pageContent + "**. The book is signed by **" + signature + "**", false);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has edited a book that consists of " + pageCount + " page(s), with " + pageContent + ". The book is signed by " + signature + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                if (main.getConfig().getBoolean("MySQL.Enable") && main.getConfig().getBoolean("Log.Player-Chat") && main.mySQL.isConnected()) {

                    MySQLData.bookEditing(serverName, worldName, playerName, pageCount, pageContent, signature, true);

                }
                if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Book-Editing")
                        && main.getSqLite().isConnected()) {

                    SQLiteData.insertBook(serverName, worldName, player, pageCount, pageContent, signature, true);

                }

                return;

            }

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBookEditingFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has edited a book that consists of " + pageCount + " page(s), with " + pageContent + ". The book is signed by " + signature + "\n");
                out.close();

            } catch (IOException e) {

                main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        if (player.hasPermission("logger.staff.log") && main.getConfig().getBoolean("Staff.Enabled")) {

            Discord.staffChat(player, "\uD83D\uDCD6 **|** \uD83D\uDC6E\u200D♂️ has edited a book that consists of **" + pageCount + "** page(s), with **" + pageContent + "**. The book is signed by **" + signature + "**", false);

        }else {

            Discord.bookEditing(player, "\uD83D\uDCD6 has edited a book that consists of **" + pageCount + "** page(s), with **" + pageContent + "**. The book is signed by **" + signature + "**", false);
        }

        //MySQL Handling
        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Book-Editing"))
                && (main.mySQL.isConnected())) {

            try {

                MySQLData.bookEditing(serverName, worldName, playerName, pageCount, pageContent, signature, false);

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

        //SQLite Handling
        if (main.getConfig().getBoolean("SQLite.Enable") && (main.getConfig().getBoolean("Log.Book-Editing"))
                && (main.getSqLite().isConnected())) {

            try {

                SQLiteData.insertBook(serverName, worldName, player, pageCount, pageContent, signature, player.hasPermission("logger.staff.log"));

            } catch (Exception exception) {

                exception.printStackTrace();

            }
        }
    }
}
