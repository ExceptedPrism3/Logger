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
        final String worldName = world.getName();
        final String playerName = player.getName();
        String item = event.getItemDrop().getName();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Item-Drop"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                Discord.staffChat(player, "\uD83D\uDEAE **|** \uD83D\uDC6E\u200D♂ [" + worldName + "] Has dropped an item ** " + item  +"**", false, Color.yellow);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has dropped an item " + item +"\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Item-Drop")) && (main.sql.isConnected())) {

                        MySQLData.itemDrop(serverName, worldName, playerName, item, true);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.itemDrop(player, "\uD83D\uDEAE [" + worldName + "] Has dropped an item ** " + item  +"**", false, Color.yellow);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemDropFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has dropped an item " + item +"\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Item-Drop")) && (main.sql.isConnected())){

            try {

                MySQLData.itemDrop(serverName, worldName, playerName, item, false);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
