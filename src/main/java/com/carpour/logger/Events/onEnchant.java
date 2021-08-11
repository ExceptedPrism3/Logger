package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.MySQL.MySQLData;
import com.carpour.logger.Utils.FileHandler;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchanting(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        String playername = player.getName();
        World world = player.getWorld();
        String worldname = world.getName();
        Map<Enchantment, Integer> ench = event.getEnchantsToAdd();
        String item = event.getItem().getType().toString();
        int cost = event.getExpLevelCost();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        String staff = "false";
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Enchanting"))) {

            for (Map.Entry<Enchantment, Integer> entry : ench.entrySet()) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")) {

                    Discord.staffChat(player, "\uD83D\uDCD6 **|** \uD83D\uDC6E\u200D♂️️ [" + worldname + "] Has used an **Enchantment table** that's located at X = " + x + " Y = " + y + " Z = " + z + " to Apply **" + entry.getKey().getName() + "** on the item **" + item + "** which cost **" + cost + "**", false, Color.pink);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write("[" + dateFormat.format((date)) + "] " + "[" + worldname + "] " + "The Staff " + playername + " has used an Enchantment Table that's Located at X = " + x + " Y = " + y + " Z = " + z + " to Apply " + entry.getKey().getName() + " on the item " + item + " which cost " + cost + "\n");
                        out.close();

                        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Enchanting")) && (main.SQL.isConnected())) {

                            staff = "true";
                            MySQLData.enchant(serverName, worldname, playername, x, y, z, entry.getKey().getName(), item, cost, staff);

                        }

                    } catch (IOException e) {

                        System.out.println("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    return;

                }

                Discord.enchanting(player, "\uD83D\uDCD6️ [" + worldname + "] Has used an **Enchantment table** that's located at X = " + x + " Y = " + y + " Z = " + z + " to Apply **" + entry.getKey().getName() + "** on the item **" + item + "** which cost **" + cost + "**", false, Color.pink);

                try {


                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getEnchantFile(), true));
                    out.write("[" + dateFormat.format((date)) + "] " + "[" + worldname + "] " + "The player " + playername + " has used an Enchantment table that's located at X = " + x + " Y = " + y + " Z = " + z + " to Apply " + entry.getKey().getName() + " on the item " + item + " which cost " + cost + "\n");
                    out.close();

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Enchanting")) && (main.SQL.isConnected())){

                    try {

                        MySQLData.enchant(serverName, worldname, playername, x, y, z, entry.getKey().getName(), item, cost, staff);

                    }catch (Exception e){

                        e.printStackTrace();

                    }
                }
            }
        }
    }
}
