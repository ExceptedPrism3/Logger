package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.database.MySQL.MySQLData;
import com.carpour.logger.database.SQLite.SQLiteData;
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

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onAnvil implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Anvil"))) {

            if (!event.isCancelled()) {

                Inventory inv = event.getInventory();

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
                                        String playerName = player.getName();

                                        if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                                            Discord.staffChat(player, "\uD83D\uDD28 **|** \uD83D\uDC6E\u200D♂️ Has renamed an Item to **" + displayName + "**", false, Color.red);

                                            try {

                                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                                                out.write("[" + dateFormat.format(date) + "] " + "The Staff " + playerName + " has renamed an item to " + displayName + "\n");
                                                out.close();

                                                if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Anvil")) && (main.mySQL.isConnected())) {


                                                    MySQLData.anvil(serverName, playerName, displayName, true);

                                                }

                                            } catch (IOException e) {

                                                System.out.println("An error occurred while logging into the appropriate file.");
                                                e.printStackTrace();

                                            }

                                            return;

                                        }

                                        Discord.anvil(player, "\uD83D\uDD28️ Has renamed an Item to **" + displayName + "**", false, Color.red);

                                        try {

                                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAnvilFile(), true));
                                            out.write("[" + dateFormat.format(date) + "] " + "The player " + playerName + " has renamed an item to " + displayName + "\n");
                                            out.close();

                                        } catch (IOException e) {

                                            System.out.println("An error occurred while logging into the appropriate file.");
                                            e.printStackTrace();

                                            return;

                                        }

                                        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Anvil")) && (main.mySQL.isConnected())) {

                                            try {

                                                MySQLData.anvil(serverName, playerName, displayName, false);

                                            } catch (Exception e) {

                                                e.printStackTrace();

                                            }
                                        }
                                        if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Anvil") && main.getSqLite().isConnected()) {
                                            try {
                                                SQLiteData.insertAnvil(serverName, player, displayName, player.hasPermission("logger.staff.log"));
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
}
