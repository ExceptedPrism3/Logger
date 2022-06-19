package com.carpour.logger.events;

import com.carpour.logger.Main;
import com.carpour.logger.utils.FileHandler;
import com.carpour.logger.utils.Messages;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnBucketPlace implements Listener {

  private final Main main = Main.getInstance();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBucket(PlayerBucketEmptyEvent event) {
    Player player = event.getPlayer();
    String playerName = player.getName();
    World world = player.getWorld();
    String worldName = world.getName();
    String bucket = event.getBucket().toString();
    bucket = bucket.replaceAll("_", " ");
    bucket = bucket.replaceAll("LEGACY", "");
    double x = Math.floor(player.getLocation().getX());
    double y = Math.floor(player.getLocation().getY());
    double z = Math.floor(player.getLocation().getZ());
    String serverName = main.getConfig().getString("Server-Name");
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    if (player.hasPermission("logger.exempt")) return;

    if (!event.isCancelled() && (main.getConfig().getBoolean("Log.Bucket-Place"))) {

      // Log To Files Handling
      if (main.getConfig().getBoolean("Log-to-Files")) {

        if (main.getConfig().getBoolean("Staff.Enabled")
            && player.hasPermission("logger.staff.log")) {

          main.getDiscord()
              .sendStaffChat(
                  playerName,
                  player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.Bucket-Place-Staff"))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%x%", String.valueOf(x))
                      .replaceAll("%y%", String.valueOf(y))
                      .replaceAll("%z%", String.valueOf(z))
                      .replaceAll("%bucket%", bucket),
                  false);

          try {

            BufferedWriter out =
                new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
            out.write(
                Objects.requireNonNull(Messages.get().getString("Files.Bucket-Place-Staff"))
                        .replaceAll("%time%", dateFormat.format(date))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%player%", playerName)
                        .replaceAll("%x%", String.valueOf(x))
                        .replaceAll("%y%", String.valueOf(y))
                        .replaceAll("%z%", String.valueOf(z))
                        .replaceAll("%bucket%", bucket)
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
                .bucketPlace(serverName, worldName, playerName, bucket, x, y, z, true);
          }

          if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

            main.getSqLiteData()
                .insertBucketPlace(
                    serverName,
                    playerName,
                    worldName,
                    player.getLocation().getBlockX(),
                    player.getLocation().getBlockY(),
                    player.getLocation().getBlockZ(),
                    bucket,
                    true);
          }

          return;
        }

        try {

          BufferedWriter out =
              new BufferedWriter(new FileWriter(FileHandler.getBucketPlaceFile(), true));
          out.write(
              Objects.requireNonNull(Messages.get().getString("Files.Bucket-Place"))
                      .replaceAll("%time%", dateFormat.format(date))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%player%", playerName)
                      .replaceAll("%x%", String.valueOf(x))
                      .replaceAll("%y%", String.valueOf(y))
                      .replaceAll("%z%", String.valueOf(z))
                      .replaceAll("%bucket%", bucket)
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
                Objects.requireNonNull(Messages.get().getString("Discord.Bucket-Place-Staff"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%x%", String.valueOf(x))
                    .replaceAll("%y%", String.valueOf(y))
                    .replaceAll("%z%", String.valueOf(z))
                    .replaceAll("%bucket%", bucket),
                false);

      } else {

        main.getDiscord()
            .sendBucketPlace(
                playerName,
                player.getUniqueId(),
                Objects.requireNonNull(Messages.get().getString("Discord.Bucket-Place"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%x%", String.valueOf(x))
                    .replaceAll("%y%", String.valueOf(y))
                    .replaceAll("%z%", String.valueOf(z))
                    .replaceAll("%bucket%", bucket),
                false);
      }

      // MySQL
      if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

        try {

          main.getMySQLData()
              .bucketPlace(
                  serverName,
                  worldName,
                  playerName,
                  bucket,
                  x,
                  y,
                  z,
                  player.hasPermission("logger.staff.log"));

        } catch (Exception e) {

          e.printStackTrace();
        }
      }

      // SQLite
      if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

        try {

          main.getSqLiteData()
              .insertBucketPlace(
                  serverName,
                  playerName,
                  worldName,
                  player.getLocation().getBlockX(),
                  player.getLocation().getBlockY(),
                  player.getLocation().getBlockZ(),
                  bucket,
                  player.hasPermission("logger.staff.log"));

        } catch (Exception e) {

          e.printStackTrace();
        }
      }
    }
  }
}
