package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.MySQL.MySQLData;
import com.carpour.logger.Utils.FileHandler;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onItemDrop implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent event){

        final Player player = event.getPlayer();
        World world = player.getWorld();
        final String worldname = world.getName();
        final String playername = player.getName();
        String item = event.getItemDrop().getName();
        String staff = "false";
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Item-Drop"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                Discord.staffChat(player, "\uD83D\uDEAE **|** \uD83D\uDC6E\u200D♂ [" + worldname + "] Has dropped an item ** " + item  +"**", false, Color.yellow);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldname + "] The Staff <" + playername + "> has dropped an item " + item +"\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Item-Drop")) && (main.SQL.isConnected())) {

                        staff = "true";
                        MySQLData.itemDrop(serverName, worldname, playername, item, staff);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.itemDrop(player, "\uD83D\uDEAE [" + worldname + "] Has dropped an item ** " + item  +"**", false, Color.yellow);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemDropFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldname + "] The Player <" + playername + "> has dropped an item " + item +"\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Item-Drop")) && (main.SQL.isConnected())){

            try {

                MySQLData.itemDrop(serverName, worldname, playername, item, staff);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
