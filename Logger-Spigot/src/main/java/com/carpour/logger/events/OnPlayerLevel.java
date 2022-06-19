package com.carpour.logger.events;

import com.carpour.logger.Main;
import com.carpour.logger.utils.FileHandler;
import com.carpour.logger.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnPlayerLevel implements Listener {

  private final Main main = Main.getInstance();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onLevelChange(PlayerLevelChangeEvent event) {

    Player player = event.getPlayer();
    String playerName = player.getName();
    int logAbove = main.getConfig().getInt("Player-Level.Log-Above");
    double playerLevel = event.getNewLevel();
    String serverName = main.getConfig().getString("Server-Name");
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    if (player.hasPermission("logger.exempt")) return;

    if (main.getConfig().getBoolean("Log.Player-Level")) {

      if (playerLevel == logAbove) {

        // Log To Files Handling
        if (main.getConfig().getBoolean("Log-to-Files")) {

          if (main.getConfig().getBoolean("Staff.Enabled")
              && player.hasPermission("logger.staff.log")) {

            main.getDiscord()
                .sendStaffChat(
                    playerName,
                    player.getUniqueId(),
                    Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff"))
                        .replaceAll("%level%", String.valueOf(logAbove)),
                    false);

            try {

              BufferedWriter out =
                  new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
              out.write(
                  Objects.requireNonNull(Messages.get().getString("Files.Player-Level-Staff"))
                          .replaceAll("%time%", dateFormat.format(date))
                          .replaceAll("%player%", playerName)
                          .replaceAll("%level%", String.valueOf(logAbove))
                      + "\n");
              out.close();

            } catch (IOException e) {

              main.getServer()
                  .getLogger()
                  .warning("An error occurred while logging into the appropriate file.");
              e.printStackTrace();
            }

            if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

              main.getMySQLData().levelChange(serverName, playerName, true);
            }

            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

              main.getSqLiteData().insertLevelChange(serverName, playerName, true);
            }

            return;
          }

          try {

            BufferedWriter out =
                new BufferedWriter(new FileWriter(FileHandler.getPlayerLevelFile(), true));
            out.write(
                Objects.requireNonNull(Messages.get().getString("Files.Player-Level"))
                        .replaceAll("%time%", dateFormat.format(date))
                        .replaceAll("%player%", playerName)
                        .replaceAll("%level%", String.valueOf(logAbove))
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
        if (main.getConfig().getBoolean("Staff.Enabled")
            && player.hasPermission("logger.staff.log")) {

          main.getDiscord()
              .sendStaffChat(
                  playerName,
                    player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff"))
                      .replaceAll("%level%", String.valueOf(logAbove)),
                  false);

        } else {
          main.getDiscord()
              .sendPlayerLevel(
                  playerName,
                    player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.Player-Level"))
                      .replaceAll("%level%", String.valueOf(logAbove)),
                  false);
        }

        // MySQL
        if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

          try {

            main.getMySQLData().levelChange(serverName, playerName, player.hasPermission("logger.staff.log"));

          } catch (Exception e) {

            e.printStackTrace();
          }
        }

        // SQLite
        if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

          try {

            main.getSqLiteData().insertLevelChange(
                serverName, playerName, player.hasPermission("logger.staff.log"));

          } catch (Exception e) {

            e.printStackTrace();
          }
        }
      }
    }
  }
}
