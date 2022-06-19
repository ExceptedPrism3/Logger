package com.carpour.logger.Events;

import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OnSign implements Listener {

  private final Main main = Main.getInstance();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerSign(SignChangeEvent event) {
    Player player = event.getPlayer();
    String playerName = player.getName();
    World world = player.getWorld();
    List<String> lines = Arrays.asList(event.getLines());
    String worldName = world.getName();
    double x = Math.floor(player.getLocation().getX());
    double y = Math.floor(player.getLocation().getY());
    double z = Math.floor(player.getLocation().getZ());
    String serverName = main.getConfig().getString("Server-Name");
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    if (player.hasPermission("logger.exempt")) return;

    if (!event.isCancelled() && main.getConfig().getBoolean("Log.Player-Sign-Text")) {

      // Log To Files Handling
      if (main.getConfig().getBoolean("Log-to-Files")) {

        if (main.getConfig().getBoolean("Staff.Enabled")
            && player.hasPermission("logger.staff.log")) {

          main.getDiscord()
              .sendStaffChat(
                  playerName,
                  player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.Player-Sign-Text-Staff"))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%x%", String.valueOf(x))
                      .replaceAll("%y%", String.valueOf(y))
                      .replaceAll("%z%", String.valueOf(z))
                      .replaceAll("%line1%", lines.get(0))
                      .replaceAll("%line2%", lines.get(1))
                      .replaceAll("%line3%", lines.get(2))
                      .replaceAll("%line4%", lines.get(3)),
                  false);

          try {

            BufferedWriter out =
                new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
            out.write(
                Objects.requireNonNull(Messages.get().getString("Files.Player-Sign-Text-Staff"))
                        .replaceAll("%time%", dateFormat.format(date))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%player%", playerName)
                        .replaceAll("%x%", String.valueOf(x))
                        .replaceAll("%y%", String.valueOf(y))
                        .replaceAll("%z%", String.valueOf(z))
                        .replaceAll("%line1%", lines.get(0))
                        .replaceAll("%line2%", lines.get(1))
                        .replaceAll("%line3%", lines.get(2))
                        .replaceAll("%line4%", lines.get(3))
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
                .playerSignText(
                    serverName,
                    worldName,
                    x,
                    y,
                    z,
                    playerName,
                    "["
                        + lines.get(0)
                        + "] "
                        + "["
                        + lines.get(1)
                        + "] "
                        + "["
                        + lines.get(2)
                        + "] "
                        + "["
                        + lines.get(3)
                        + "]",
                    true);
          }

          if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

            main.getSqLiteData()
                .insertPlayerSignText(
                    serverName,
                    playerName,
                    worldName,
                    player.getLocation().getBlockX(),
                    player.getLocation().getBlockY(),
                    player.getLocation().getBlockZ(),
                    "["
                        + lines.get(0)
                        + "] "
                        + "["
                        + lines.get(1)
                        + "] "
                        + "["
                        + lines.get(2)
                        + "] "
                        + "["
                        + lines.get(3)
                        + "]",
                    true);
          }

          return;
        }

        try {

          BufferedWriter out =
              new BufferedWriter(new FileWriter(FileHandler.getSignLogFile(), true));
          out.write(
              Objects.requireNonNull(Messages.get().getString("Files.Player-Sign-Text"))
                      .replaceAll("%time%", dateFormat.format(date))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%player%", playerName)
                      .replaceAll("%x%", String.valueOf(x))
                      .replaceAll("%y%", String.valueOf(y))
                      .replaceAll("%z%", String.valueOf(z))
                      .replaceAll("%line1%", lines.get(0))
                      .replaceAll("%line2%", lines.get(1))
                      .replaceAll("%line3%", lines.get(2))
                      .replaceAll("%line4%", lines.get(3))
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
                Objects.requireNonNull(Messages.get().getString("Discord.Player-Sign-Text-Staff"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%x%", String.valueOf(x))
                    .replaceAll("%y%", String.valueOf(y))
                    .replaceAll("%z%", String.valueOf(z))
                    .replaceAll("%line1%", lines.get(0))
                    .replaceAll("%line2%", lines.get(1))
                    .replaceAll("%line3%", lines.get(2))
                    .replaceAll("%line4%", lines.get(3)),
                false);

      } else {

        main.getDiscord()
            .sendPlayerSignText(
                playerName,
                player.getUniqueId(),
                Objects.requireNonNull(Messages.get().getString("Discord.Player-Sign-Text"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%x%", String.valueOf(x))
                    .replaceAll("%y%", String.valueOf(y))
                    .replaceAll("%z%", String.valueOf(z))
                    .replaceAll("%line1%", lines.get(0))
                    .replaceAll("%line2%", lines.get(1))
                    .replaceAll("%line3%", lines.get(2))
                    .replaceAll("%line4%", lines.get(3)),
                false);
      }

      // MySQL
      if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

        try {

          main.getMySQLData()
              .playerSignText(
                  serverName,
                  worldName,
                  x,
                  y,
                  z,
                  playerName,
                  "["
                      + lines.get(0)
                      + "] "
                      + "["
                      + lines.get(1)
                      + "] "
                      + "["
                      + lines.get(2)
                      + "] "
                      + "["
                      + lines.get(3)
                      + "]",
                  player.hasPermission("logger.staff.log"));

        } catch (Exception e) {

          e.printStackTrace();
        }
      }

      // SQLite
      if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

        try {

          main.getSqLiteData()
              .insertPlayerSignText(
                  serverName,
                  playerName,
                  worldName,
                  player.getLocation().getBlockX(),
                  player.getLocation().getBlockY(),
                  player.getLocation().getBlockZ(),
                  "["
                      + lines.get(0)
                      + "] "
                      + "["
                      + lines.get(1)
                      + "] "
                      + "["
                      + lines.get(2)
                      + "] "
                      + "["
                      + lines.get(3)
                      + "]",
                  player.hasPermission("logger.staff.log"));

        } catch (Exception exception) {

          exception.printStackTrace();
        }
      }
    }
  }
}
