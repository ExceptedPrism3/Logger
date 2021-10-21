package com.carpour.logger.Events;

import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class onItemPickup implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onItemPick(PlayerPickupItemEvent event){

        Player player = event.getPlayer();
        String playerName = player.getName();
        Material item = event.getItem().getItemStack().getType();
        String itemName = Objects.requireNonNull(event.getItem().getItemStack().getItemMeta()).getDisplayName();
        int amount = event.getItem().getItemStack().getAmount();
        List<String> itemToLog = main.getConfig().getStringList("Item-Pickup");
        World world = player.getWorld();
        String worldName = world.getName();
        int blockX = event.getItem().getLocation().getBlockX();
        int blockY = event.getItem().getLocation().getBlockY();
        int blockZ = event.getItem().getLocation().getBlockZ();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (!event.isCancelled() && main.getConfig().getBoolean("Log.Item-Pickup")) {

            for (String list : itemToLog) {

                if (item.equals(Material.getMaterial(list))) {

                    //Log To Files Handling
                    if (main.getConfig().getBoolean("Log-to-Files")) {

                        if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                            Discord.staffChat(player, "\uD83E\uDDF2 **|** \uD83D\uDC6E\u200D♂ [" + worldName + "] Has picked up **" + amount + "** of **" + item + "** at X= **" + blockX + "** Y= **" + blockY + "** Z= **" + blockZ + "** *| " + itemName + "*", false);

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> Has picked up " + amount + " of " + item + " at X= " + blockX + " Y= " + blockY + " Z= " + blockZ + " | " + itemName + "\n");
                                out.close();

                            } catch (IOException e) {

                                main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }

                            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                                MySQLData.itemPickup(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, itemName, true);

                            }

                            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                                SQLiteData.insertItemPickup(serverName, player, item, blockX, amount, blockY, blockZ, itemName, true);

                            }

                            return;

                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemPickupFile(), true));
                            out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> Has picked up " + amount + " of " + item + " at X= " + blockX + " Y= " + blockY + " Z= " + blockZ + " | " + itemName + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    //Discord
                    if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                        Discord.staffChat(player, "\uD83E\uDDF2 **|** \uD83D\uDC6E\u200D♂ [" + worldName + "] Has picked up **" + amount + "** of **" + item + "** at X= **" + blockX + "** Y= **" + blockY + "** Z= **" + blockZ + "** *| " + itemName + "*", false);

                    } else {

                        Discord.itemPickup(player, "\uD83E\uDDF2 [" + worldName + "] Has picked up **" + amount + "** of **" + item + "** at X= **" + blockX + "** Y= **" + blockY + "** Z= **" + blockZ + "** *| " + itemName + "*", false);

                    }

                    //MySQL
                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                        try {

                            MySQLData.itemPickup(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, itemName, player.hasPermission("logger.staff.log"));

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }

                    //SQLite
                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        try {

                            SQLiteData.insertItemPickup(serverName, player, item, amount, blockX, blockY, blockZ, itemName, player.hasPermission("logger.staff.log"));

                        } catch (Exception exception) {

                            exception.printStackTrace();

                        }
                    }
                }
            }
        }
    }
}
