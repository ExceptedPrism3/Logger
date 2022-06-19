package com.carpour.logger.Events;

import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.carour.loggercore.database.mysql.MySQLData;
import org.carour.loggercore.database.sqlite.SQLiteData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnAnvil implements Listener {

  private final Main main = Main.getInstance();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInventoryClick(InventoryClickEvent event) {

    Player player = (Player) event.getWhoClicked();
    String playerName = player.getName();
    String serverName = main.getConfig().getString("Server-Name");
    Inventory inv = event.getInventory();
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    if (player.hasPermission("logger.exempt")) return;

    if (!event.isCancelled() && main.getConfig().getBoolean("Log.Anvil")) {

      if (inv instanceof AnvilInventory) {

        InventoryView view = event.getView();

        int rawSlot = event.getRawSlot();

        if (rawSlot == view.convertSlot(rawSlot)) {

          if (rawSlot == 2) {

            ItemStack item = event.getCurrentItem();

            if (item != null) {

              ItemMeta meta = item.getItemMeta();

              if (meta != null) {

                if (meta.hasDisplayName()) {

                  String displayName = meta.getDisplayName();

                  // Log To Files Handling
                  if (main.getConfig().getBoolean("Log-to-Files")) {

                    if (main.getConfig().getBoolean("Staff.Enabled")
                        && player.hasPermission("logger.staff.log")) {

                      main.getDiscord()
                          .sendStaffChat(
                              playerName,
                              player.getUniqueId(),
                              Objects.requireNonNull(
                                      Messages.get().getString("Discord.Anvil-Staff"))
                                  .replaceAll("%renamed%", displayName),
                              false);

                      try {

                        BufferedWriter out =
                            new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(
                            Objects.requireNonNull(Messages.get().getString("Files.Anvil-Staff"))
                                    .replaceAll("%time%", dateFormat.format(date))
                                    .replaceAll("%player%", playerName)
                                    .replaceAll("%renamed%", displayName)
                                + "\n");
                        out.close();

                      } catch (IOException e) {

                        main.getServer()
                            .getLogger()
                            .warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                      }

                      if (main.getConfig().getBoolean("MySQL.Enable")
                          && main.getMySQL().isConnected()) {

                        main.getMySQLData().anvil(serverName, playerName, displayName, true);
                      }

                      if (main.getConfig().getBoolean("SQLite.Enable")
                          && main.getSqLite().isConnected()) {

                        main.getSqLiteData().insertAnvil(serverName, playerName, displayName, true);
                      }

                      return;
                    }

                    try {

                      BufferedWriter out =
                          new BufferedWriter(new FileWriter(FileHandler.getAnvilFile(), true));
                      out.write(
                          Objects.requireNonNull(Messages.get().getString("Files.Anvil"))
                                  .replaceAll("%time%", dateFormat.format(date))
                                  .replaceAll("%player%", playerName)
                                  .replaceAll("%renamed%", displayName)
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
                            Objects.requireNonNull(Messages.get().getString("Discord.Anvil-Staff"))
                                .replaceAll("%renamed%", displayName),
                            false);

                  } else {

                    main.getDiscord()
                        .sendAnvil(
                            playerName,
                            player.getUniqueId(),
                            Objects.requireNonNull(Messages.get().getString("Discord.Anvil"))
                                .replaceAll("%renamed%", displayName),
                            false);
                  }

                  // MySQL
                  if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

                    try {

                      main.getMySQLData().anvil(
                          serverName,
                          playerName,
                          displayName,
                          player.hasPermission("logger.staff.log"));

                    } catch (Exception e) {

                      e.printStackTrace();
                    }
                  }

                  // SQLite
                  if (main.getConfig().getBoolean("SQLite.Enable")
                      && main.getSqLite().isConnected()) {

                    try {

                      main.getSqLiteData().insertAnvil(
                          serverName,
                          playerName,
                          displayName,
                          player.hasPermission("logger.staff.log"));

                    } catch (Exception e) {

                      e.printStackTrace();
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
