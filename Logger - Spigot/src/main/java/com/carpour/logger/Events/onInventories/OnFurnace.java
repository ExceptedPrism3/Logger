package com.carpour.logger.Events.onInventories;

import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnFurnace implements Listener {

  public final Main main = Main.getInstance();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInv(FurnaceExtractEvent event) {

    Player player = event.getPlayer();
    String playerName = event.getPlayer().getName();
    String worldName = player.getWorld().getName();
    int blockX = event.getBlock().getLocation().getBlockX();
    int blockY = event.getBlock().getLocation().getBlockY();
    int blockZ = event.getBlock().getLocation().getBlockZ();
    Material item = event.getItemType();
    int amount = event.getItemAmount();
    String serverName = main.getConfig().getString("Server-Name");
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    if (player.hasPermission("logger.exempt")) return;

    if (main.getConfig().getBoolean("Log.Furnace")) {

      // Log To Files Handling
      if (main.getConfig().getBoolean("Log-to-Files")) {

        if (main.getConfig().getBoolean("Staff.Enabled")
            && player.hasPermission("logger.staff.log")) {

          main.getDiscord()
              .sendStaffChat(
                  playerName,
                  player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.Furnace-Staff"))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%x%", String.valueOf(blockX))
                      .replaceAll("%y%", String.valueOf(blockY))
                      .replaceAll("%z%", String.valueOf(blockZ))
                      .replaceAll("%amount%", String.valueOf(amount))
                      .replaceAll("%item%", String.valueOf(item)),
                  false);

          try {

            BufferedWriter out =
                new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
            out.write(
                Objects.requireNonNull(Messages.get().getString("Files.Furnace-Staff"))
                        .replaceAll("%time%", dateFormat.format(date))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%player%", playerName)
                        .replaceAll("%x%", String.valueOf(blockX))
                        .replaceAll("%y%", String.valueOf(blockY))
                        .replaceAll("%z%", String.valueOf(blockZ))
                        .replaceAll("%amount%", String.valueOf(amount))
                        .replaceAll("%item%", String.valueOf(item))
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
                .furnace(
                    serverName,
                    worldName,
                    playerName,
                    item.toString(),
                    amount,
                    blockX,
                    blockY,
                    blockZ,
                    true);
          }

          if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

            main.getSqLiteData()
                .insertFurnace(
                    serverName,
                    playerName,
                    worldName,
                    item.toString(),
                    amount,
                    blockX,
                    blockY,
                    blockZ,
                    true);
          }

          return;
        }

        try {

          BufferedWriter out =
              new BufferedWriter(new FileWriter(FileHandler.getFurnaceFile(), true));
          out.write(
              Objects.requireNonNull(Messages.get().getString("Files.Furnace"))
                      .replaceAll("%time%", dateFormat.format(date))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%player%", playerName)
                      .replaceAll("%x%", String.valueOf(blockX))
                      .replaceAll("%y%", String.valueOf(blockY))
                      .replaceAll("%z%", String.valueOf(blockZ))
                      .replaceAll("%amount%", String.valueOf(amount))
                      .replaceAll("%item%", String.valueOf(item))
                  + "\n");
          out.close();

        } catch (IOException e) {

          main.getServer()
              .getLogger()
              .warning("An error occurred while logging into the appropriate file.");
          e.printStackTrace();
        }
      }

      // Discord Integration
      if (main.getConfig().getBoolean("Staff.Enabled")
          && player.hasPermission("logger.staff.log")) {

        main.getDiscord()
            .sendStaffChat(
                playerName,
                player.getUniqueId(),
                Objects.requireNonNull(Messages.get().getString("Discord.Furnace-Staff"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%x%", String.valueOf(blockX))
                    .replaceAll("%y%", String.valueOf(blockY))
                    .replaceAll("%z%", String.valueOf(blockZ))
                    .replaceAll("%amount%", String.valueOf(amount))
                    .replaceAll("%item%", String.valueOf(item)),
                false);

      } else {

        main.getDiscord()
            .sendFurnace(
                playerName,
                player.getUniqueId(),
                Objects.requireNonNull(Messages.get().getString("Discord.Furnace"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%x%", String.valueOf(blockX))
                    .replaceAll("%y%", String.valueOf(blockY))
                    .replaceAll("%z%", String.valueOf(blockZ))
                    .replaceAll("%amount%", String.valueOf(amount))
                    .replaceAll("%item%", String.valueOf(item)),
                false);
      }

      // MySQL Handling
      if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

        try {

          main.getMySQLData()
              .furnace(
                  serverName,
                  worldName,
                  playerName,
                  item.toString(),
                  amount,
                  blockX,
                  blockY,
                  blockZ,
                  player.hasPermission("logger.staff.log"));

        } catch (Exception e) {

          e.printStackTrace();
        }
      }

      // SQLite Handling
      if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

        try {

          main.getSqLiteData()
              .insertFurnace(
                  serverName,
                  playerName,
                  worldName,
                  item.toString(),
                  amount,
                  blockX,
                  blockY,
                  blockZ,
                  player.hasPermission("logger.staff.log"));

        } catch (Exception exception) {

          exception.printStackTrace();
        }
      }
    }
  }
}
