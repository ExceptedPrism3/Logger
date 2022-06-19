package com.carpour.logger.Events;

import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OnItemDrop implements Listener {

  private final Main main = Main.getInstance();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDrop(PlayerDropItemEvent event) {

    Player player = event.getPlayer();
    String playerName = player.getName();
    World world = player.getWorld();
    String worldName = world.getName();
    String item = event.getItemDrop().getItemStack().getType().name().replace("_", " ");
    String itemName =
        Objects.requireNonNull(event.getItemDrop().getItemStack().getItemMeta()).getDisplayName();
    int amount = event.getItemDrop().getItemStack().getAmount();
    int blockX = event.getItemDrop().getLocation().getBlockX();
    int blockY = event.getItemDrop().getLocation().getBlockY();
    int blockZ = event.getItemDrop().getLocation().getBlockZ();
    List<String> enchs = new ArrayList<>();
    String serverName = main.getConfig().getString("Server-Name");
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    if (player.hasPermission("logger.exempt")) return;

    if (!event.isCancelled() && main.getConfig().getBoolean("Log.Item-Drop")) {

      for (Enchantment ench : event.getItemDrop().getItemStack().getEnchantments().keySet()) {

        enchs.add(ench.getName());
      }

      // Log To Files Handling
      if (main.getConfig().getBoolean("Log-to-Files")) {

        if (main.getConfig().getBoolean("Staff.Enabled")
            && player.hasPermission("logger.staff.log")) {

          main.getDiscord()
              .sendStaffChat(
                  playerName,
                  player.getUniqueId(),
                  Objects.requireNonNull(Messages.get().getString("Discord.Item-Drop-Staff"))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%item%", item)
                      .replaceAll("%amount%", String.valueOf(amount))
                      .replaceAll("%x%", String.valueOf(blockX))
                      .replaceAll("%y%", String.valueOf(blockY))
                      .replaceAll("%z%", String.valueOf(blockZ))
                      .replaceAll("%renamed%", String.valueOf(itemName))
                      .replaceAll("%enchantment%", String.valueOf(enchs)),
                  false);

          try {

            BufferedWriter out =
                new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
            out.write(
                Objects.requireNonNull(Messages.get().getString("Files.Item-Drop-Staff"))
                        .replaceAll("%time%", dateFormat.format(date))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%player%", playerName)
                        .replaceAll("%item%", item)
                        .replaceAll("%amount%", String.valueOf(amount))
                        .replaceAll("%x%", String.valueOf(blockX))
                        .replaceAll("%y%", String.valueOf(blockY))
                        .replaceAll("%z%", String.valueOf(blockZ))
                        .replaceAll("%renamed%", String.valueOf(itemName))
                        .replaceAll("%enchantment%", String.valueOf(enchs))
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
                .itemDrop(
                    serverName,
                    worldName,
                    playerName,
                    item,
                    amount,
                    blockX,
                    blockY,
                    blockZ,
                    enchs,
                    itemName,
                    true);
          }

          if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

            main.getSqLiteData()
                .insertItemDrop(
                    serverName,
                    playerName,
                    worldName,
                    item,
                    blockX,
                    amount,
                    blockY,
                    blockZ,
                    enchs,
                    itemName,
                    true);
          }

          return;
        }

        try {

          BufferedWriter out =
              new BufferedWriter(new FileWriter(FileHandler.getItemDropFile(), true));
          out.write(
              Objects.requireNonNull(Messages.get().getString("Files.Item-Drop"))
                      .replaceAll("%time%", dateFormat.format(date))
                      .replaceAll("%world%", worldName)
                      .replaceAll("%player%", playerName)
                      .replaceAll("%item%", item)
                      .replaceAll("%amount%", String.valueOf(amount))
                      .replaceAll("%x%", String.valueOf(blockX))
                      .replaceAll("%y%", String.valueOf(blockY))
                      .replaceAll("%z%", String.valueOf(blockZ))
                      .replaceAll("%renamed%", String.valueOf(itemName))
                      .replaceAll("%enchantment%", String.valueOf(enchs))
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
                Objects.requireNonNull(Messages.get().getString("Discord.Item-Drop-Staff"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%item%", item)
                    .replaceAll("%amount%", String.valueOf(amount))
                    .replaceAll("%x%", String.valueOf(blockX))
                    .replaceAll("%y%", String.valueOf(blockY))
                    .replaceAll("%z%", String.valueOf(blockZ))
                    .replaceAll("%renamed%", String.valueOf(itemName))
                    .replaceAll("%enchantment%", String.valueOf(enchs)),
                false);

      } else {

        main.getDiscord()
            .sendItemDrop(
                playerName,
                player.getUniqueId(),
                Objects.requireNonNull(Messages.get().getString("Discord.Item-Drop"))
                    .replaceAll("%world%", worldName)
                    .replaceAll("%item%", item)
                    .replaceAll("%amount%", String.valueOf(amount))
                    .replaceAll("%x%", String.valueOf(blockX))
                    .replaceAll("%y%", String.valueOf(blockY))
                    .replaceAll("%z%", String.valueOf(blockZ))
                    .replaceAll("%renamed%", String.valueOf(itemName))
                    .replaceAll("%enchantment%", String.valueOf(enchs)),
                false);
      }

      // MySQL
      if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

        try {

          main.getMySQLData().itemDrop(
              serverName,
              worldName,
              playerName,
              item,
              amount,
              blockX,
              blockY,
              blockZ,
              enchs,
              itemName,
              player.hasPermission("logger.staff.log"));

        } catch (Exception e) {

          e.printStackTrace();
        }
      }

      // SQLite
      if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

        try {

          main.getSqLiteData().insertItemDrop(
              serverName,
              playerName,
              worldName,
              item,
              amount,
              blockX,
              blockY,
              blockZ,
              enchs,
              itemName,
              player.hasPermission("logger.staff.log"));

        } catch (Exception exception) {

          exception.printStackTrace();
        }
      }
    }
  }
}
