package com.carpour.logger.events;

import com.carpour.logger.Main;
import com.carpour.logger.utils.FileHandler;
import com.carpour.logger.utils.Messages;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnPlayerTeleport implements Listener {

  private final Main main = Main.getInstance();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTeleport(PlayerTeleportEvent event) {

    Player player = event.getPlayer();
    World world = player.getWorld();
    String worldName = world.getName();
    String playerName = player.getName();
    int tx = Objects.requireNonNull(event.getTo()).getBlockX();
    int ty = event.getTo().getBlockY();
    int tz = event.getTo().getBlockZ();
    double ox = Math.floor(player.getLocation().getX());
    double oy = Math.floor(player.getLocation().getY());
    double oz = Math.floor(player.getLocation().getZ());

    String serverName = main.getConfig().getString("Server-Name");
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    if (player.hasPermission("logger.exempt")) return;

    if (!event.isCancelled() && main.getConfig().getBoolean("Log.Player-Teleport")) {

      // Log To Files Handling
      if (main.getConfig().getBoolean("Log-to-Files")) {

        if (main.getConfig().getBoolean("Staff.Enabled")
            && player.hasPermission("logger.staff.log")) {

          main.getDiscord()
              .sendStaffChat(
                  playerName,
                  player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff"))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%oldX%", String.valueOf(ox))
                      .replaceAll("%oldY%", String.valueOf(oy))
                      .replaceAll("%oldZ%", String.valueOf(oz))
                      .replaceAll("%newX%", String.valueOf(tx))
                      .replaceAll("%newY%", String.valueOf(ty))
                      .replaceAll("%newZ%", String.valueOf(tz)),
                  false);

          try {

            BufferedWriter out =
                new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
            out.write(
                Objects.requireNonNull(Messages.get().getString("Files.Player-Teleport-Staff"))
                        .replaceAll("%time%", dateFormat.format(date))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%player%", playerName)
                        .replaceAll("%oldX%", String.valueOf(ox))
                        .replaceAll("%oldY%", String.valueOf(oy))
                        .replaceAll("%oldZ%", String.valueOf(oz))
                        .replaceAll("%newX%", String.valueOf(tx))
                        .replaceAll("%newY%", String.valueOf(ty))
                        .replaceAll("%newZ%", String.valueOf(tz))
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
                .playerTeleport(serverName, worldName, playerName, ox, oy, oz, tx, ty, tz, true);
          }

          if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

            main.getSqLiteData()
                .insertPlayerTeleport(
                    serverName,
                    playerName,
                    worldName,
                    (int) ox,
                    (int) oy,
                    (int) oz,
                    tx,
                    ty,
                    tz,
                    true);
          }

          return;
        }

        try {

          BufferedWriter out =
              new BufferedWriter(new FileWriter(FileHandler.getPlayerTeleportLogFile(), true));
          out.write(
              Objects.requireNonNull(Messages.get().getString("Files.Player-Teleport"))
                      .replaceAll("%time%", dateFormat.format(date))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%player%", playerName)
                      .replaceAll("%oldX%", String.valueOf(ox))
                      .replaceAll("%oldY%", String.valueOf(oy))
                      .replaceAll("%oldZ%", String.valueOf(oz))
                      .replaceAll("%newX%", String.valueOf(tx))
                      .replaceAll("%newY%", String.valueOf(ty))
                      .replaceAll("%newZ%", String.valueOf(tz))
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
                Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%oldX%", String.valueOf(ox))
                    .replaceAll("%oldY%", String.valueOf(oy))
                    .replaceAll("%oldZ%", String.valueOf(oz))
                    .replaceAll("%newX%", String.valueOf(tx))
                    .replaceAll("%newY%", String.valueOf(ty))
                    .replaceAll("%newZ%", String.valueOf(tz)),
                false);

      } else {

        main.getDiscord()
            .sendPlayerTeleport(
                playerName,
                player.getUniqueId(),
                Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%oldX%", String.valueOf(ox))
                    .replaceAll("%oldY%", String.valueOf(oy))
                    .replaceAll("%oldZ%", String.valueOf(oz))
                    .replaceAll("%newX%", String.valueOf(tx))
                    .replaceAll("%newY%", String.valueOf(ty))
                    .replaceAll("%newZ%", String.valueOf(tz)),
                false);
      }

      // MySQL
      if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

        try {

          main.getMySQLData()
              .playerTeleport(
                  serverName,
                  worldName,
                  playerName,
                  ox,
                  oy,
                  oz,
                  tx,
                  ty,
                  tz,
                  player.hasPermission("logger.staff.log"));

        } catch (Exception e) {

          e.printStackTrace();
        }
      }

      // SQLite
      if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

        try {

          main.getSqLiteData()
              .insertPlayerTeleport(
                  serverName,
                  playerName,
                  worldName,
                  (int) ox,
                  (int) oy,
                  (int) oz,
                  tx,
                  ty,
                  tz,
                  player.hasPermission("logger.staff.log"));

        } catch (Exception e) {

          e.printStackTrace();
        }
      }
    }
  }
}
