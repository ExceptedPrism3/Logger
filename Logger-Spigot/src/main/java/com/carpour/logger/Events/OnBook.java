package com.carpour.logger.Events;

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
  public void bookEditing(PlayerEditBookEvent event) {

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

    // Log To Files Handling
    if (!event.isCancelled() && main.getConfig().getBoolean("Log.Book-Editing")) {

      if (main.getConfig().getBoolean("Log-to-Files")) {

        if (main.getConfig().getBoolean("Staff.Enabled")
            && player.hasPermission("logger.staff.log")) {

          main.getDiscord()
              .sendStaffChat(
                  playerName,
                  player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff"))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%player%", playerName)
                      .replaceAll("%page%", String.valueOf(pageCount))
                      .replaceAll("%content%", String.valueOf(pageContent))
                      .replaceAll("%sign%", String.valueOf(signature)),
                  false);

          try {

            BufferedWriter out =
                new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
            out.write(
                Objects.requireNonNull(Messages.get().getString("Files.Book-Editing-Staff"))
                        .replaceAll("%time%", dateFormat.format(date))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%player%", playerName)
                        .replaceAll("%page%", String.valueOf(pageCount))
                        .replaceAll("%content%", String.valueOf(pageContent))
                        .replaceAll("%sign%", String.valueOf(signature))
                    + "\n");
            out.close();

          } catch (IOException e) {

            main.getServer()
                .getLogger()
                .warning("An error occurred while logging into the appropriate file.");
            e.printStackTrace();
          }

          if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

            main.getMySQLData()
                .bookEditing(
                    serverName, worldName, playerName, pageCount, pageContent, signature, true);
          }
          if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

            main.getSqLiteData()
                .insertBook(
                    serverName, worldName, playerName, pageCount, pageContent, signature, true);
          }

          return;
        }

        try {

          BufferedWriter out =
              new BufferedWriter(new FileWriter(FileHandler.getBookEditingFile(), true));
          out.write(
              Objects.requireNonNull(Messages.get().getString("Files.Book-Editing"))
                      .replaceAll("%time%", dateFormat.format(date))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%player%", playerName)
                      .replaceAll("%page%", String.valueOf(pageCount))
                      .replaceAll("%content%", String.valueOf(pageContent))
                      .replaceAll("%sign%", String.valueOf(signature))
                  + "\n");
          out.close();

        } catch (IOException e) {

          main.getServer()
              .getLogger()
              .warning("An error occurred while logging into the appropriate file.");
          e.printStackTrace();
        }
      }

      // Discord
      if (player.hasPermission("logger.staff.log")
          && main.getConfig().getBoolean("Staff.Enabled")) {

        main.getDiscord()
            .sendStaffChat(
                playerName,
                player.getUniqueId(),
                Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing-Staff"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%player%", playerName)
                    .replaceAll("%page%", String.valueOf(pageCount))
                    .replaceAll("%content%", String.valueOf(pageContent))
                    .replaceAll("%sign%", String.valueOf(signature)),
                false);

      } else {

        main.getDiscord()
            .sendBookEditing(
                playerName,
                player.getUniqueId(),
                Objects.requireNonNull(Messages.get().getString("Discord.Book-Editing"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%player%", playerName)
                    .replaceAll("%page%", String.valueOf(pageCount))
                    .replaceAll("%content%", String.valueOf(pageContent))
                    .replaceAll("%sign%", String.valueOf(signature)),
                false);
      }

      // MySQL
      if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

        try {

          main.getMySQLData().bookEditing(
              serverName, worldName, playerName, pageCount, pageContent, signature, false);

        } catch (Exception e) {

          e.printStackTrace();
        }
      }

      // SQLite
      if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

        try {

          main.getSqLiteData().insertBook(
              serverName,
              worldName,
              playerName,
              pageCount,
              pageContent,
              signature,
              player.hasPermission("logger.staff.log"));

        } catch (Exception exception) {

          exception.printStackTrace();
        }
      }
    }
  }
}
