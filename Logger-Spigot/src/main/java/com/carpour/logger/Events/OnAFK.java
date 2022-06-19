package com.carpour.logger.Events;

import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnAFK implements Listener {

  private final Main main = Main.getInstance();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void afk(AfkStatusChangeEvent e) {

    boolean afk = e.getAffected().isAfk();

    Player player = e.getAffected().getBase();
    String playerName = player.getName();
    double x = player.getLocation().getBlockX();
    double y = player.getLocation().getBlockY();
    double z = player.getLocation().getBlockZ();
    String worldName = player.getWorld().getName();
    String serverName = main.getConfig().getString("Server-Name");
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    if (player.hasPermission("logger.exempt")) return;

    if (!afk) {

      if (main.getConfig().getBoolean("Log.AFK")) {

        // Log To Files Handling
        if (main.getConfig().getBoolean("Log-to-Files")) {

          if (main.getConfig().getBoolean("Staff.Enabled")
              && player.hasPermission("logger.staff.log")) {

            main.getDiscord()
                .sendStaffChat(
                    playerName,
                    player.getUniqueId(),
                    Objects.requireNonNull(Messages.get().getString("Discord.AFK-Staff"))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%x%", String.valueOf(x))
                        .replaceAll("%y%", String.valueOf(y))
                        .replaceAll("%z%", String.valueOf(z)),
                    false);

            try {

              BufferedWriter out =
                  new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
              out.write(
                  Objects.requireNonNull(Messages.get().getString("Files.AFK-Staff"))
                          .replaceAll("%time%", dateFormat.format(date))
                          .replaceAll("%world%", worldName)
                          .replaceAll("%player%", playerName)
                          .replaceAll("%x%", String.valueOf(x))
                          .replaceAll("%y%", String.valueOf(y))
                          .replaceAll("%z%", String.valueOf(z))
                      + "\n");
              out.close();

            } catch (IOException event) {

              main.getServer()
                  .getLogger()
                  .warning("An error occurred while logging into the appropriate file.");
              event.printStackTrace();
            }

            if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

              main.getMySQLData().afk(serverName, worldName, playerName, x, y, z, true);
            }

            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

              main.getSqLiteData()
                  .insertAFK(
                      serverName,
                      playerName,
                      worldName,
                      player.getLocation().getBlockX(),
                      player.getLocation().getBlockY(),
                      player.getLocation().getBlockZ(),
                      true);
            }

            return;
          }

          try {

            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAfkFile(), true));
            out.write(
                Objects.requireNonNull(Messages.get().getString("Files.AFK"))
                        .replaceAll("%time%", dateFormat.format(date))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%player%", playerName)
                        .replaceAll("%x%", String.valueOf(x))
                        .replaceAll("%y%", String.valueOf(y))
                        .replaceAll("%z%", String.valueOf(z))
                    + "\n");
            out.close();

          } catch (IOException event) {

            main.getServer()
                .getLogger()
                .warning("An error occurred while logging into the appropriate file.");
            event.printStackTrace();
          }
        }

        // Discord
        if (main.getConfig().getBoolean("Staff.Enabled")
            && player.hasPermission("logger.staff.log")) {

          main.getDiscord()
              .sendStaffChat(
                  playerName,
                  player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.AFK-Staff"))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%x%", String.valueOf(x))
                      .replaceAll("%y%", String.valueOf(y))
                      .replaceAll("%z%", String.valueOf(z)),
                  false);

        } else {

          main.getDiscord()
              .sendAfk(
                  playerName,
                  player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.AFK"))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%x%", String.valueOf(x))
                      .replaceAll("%y%", String.valueOf(y))
                      .replaceAll("%z%", String.valueOf(z)),
                  false);
        }

        // MySQL
        if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

          try {

            main.getMySQLData()
                .afk(
                    serverName,
                    worldName,
                    playerName,
                    x,
                    y,
                    z,
                    player.hasPermission("logger.staff.log"));

          } catch (Exception event) {

            event.printStackTrace();
          }
        }

        // SQLite
        if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

          try {

            main.getSqLiteData()
                .insertAFK(
                    serverName,
                    playerName,
                    worldName,
                    player.getLocation().getBlockX(),
                    player.getLocation().getBlockY(),
                    player.getLocation().getBlockZ(),
                    player.hasPermission("logger.staff.log"));

          } catch (Exception exception) {

            exception.printStackTrace();
          }
        }
      }
    }
  }
}
