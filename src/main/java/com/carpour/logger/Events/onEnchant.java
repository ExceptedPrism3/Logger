package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.SQLite.SQLiteData;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class onEnchant implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchanting(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        String playerName = player.getName();
        World world = player.getWorld();
        String worldName = world.getName();
        Map<Enchantment, Integer> ench = event.getEnchantsToAdd();
        String item = event.getItem().getType().toString();
        int cost = event.getExpLevelCost();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Enchanting"))) {

            for (Map.Entry<Enchantment, Integer> entry : ench.entrySet()) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, "\uD83D\uDCD6 **|** \uD83D\uDC6E\u200D♂️️ [" + worldName + "] Has used an **Enchantment table** that's located at X = " + x + " Y = " + y + " Z = " + z + " to Apply **" + entry.getKey().getName() + "** on the item **" + item + "** which cost **" + cost + "**", false, Color.PINK);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write("[" + dateFormat.format((date)) + "] " + "[" + worldName + "] " + "The Staff " + playerName + " has used an Enchantment Table that's Located at X = " + x + " Y = " + y + " Z = " + z + " to Apply " + entry.getKey().getName() + " on the item " + item + " which cost " + cost + "\n");
                        out.close();

                        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Enchanting")) && (main.mySQL.isConnected())) {

                            MySQLData.enchant(serverName, worldName, playerName, x, y, z, entry.getKey().getName(), item, cost, true);

                        }

                    } catch (IOException e) {

                        System.out.println("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    return;

                }

                Discord.enchanting(player, "\uD83D\uDCD6️ [" + worldName + "] Has used an **Enchantment table** that's located at X = " + x + " Y = " + y + " Z = " + z + " to Apply **" + entry.getKey().getName() + "** on the item **" + item + "** which cost **" + cost + "**", false, Color.PINK);

                try {


                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getEnchantFile(), true));
                    out.write("[" + dateFormat.format((date)) + "] " + "[" + worldName + "] " + "The player " + playerName + " has used an Enchantment table that's located at X = " + x + " Y = " + y + " Z = " + z + " to Apply " + entry.getKey().getName() + " on the item " + item + " which cost " + cost + "\n");
                    out.close();

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Enchanting")) && (main.mySQL.isConnected())){

                    try {

                        MySQLData.enchant(serverName, worldName, playerName, x, y, z, entry.getKey().getName(), item, cost, false);

                    }catch (Exception e){

                        e.printStackTrace();

                    }
                }
                if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Enchanting") && main.getSqLite().isConnected()) {
                    try{
                        SQLiteData.insertEnchant(serverName, player, entry.getKey().getName(), item, cost, player.hasPermission("logger.staff.log"));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }
}
