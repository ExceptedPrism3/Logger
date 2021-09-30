package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.SQLite.SQLiteData;
import org.bukkit.World;
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
import java.util.Date;

public class onItemDrop implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event){

        Player player = event.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        String worldName = world.getName();
        String item = event.getItemDrop().getItemStack().getType().name().replace("_", " ");
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (!event.isCancelled() && main.getConfig().getBoolean("Log.Item-Drop")) {

            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, "\uD83D\uDEAE **|** \uD83D\uDC6E\u200D♂ [" + worldName + "] Has dropped ** " + item + "**", false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has dropped " + item + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.getConfig().getBoolean("Log.Item-Drop") && main.mySQL.isConnected()) {

                        MySQLData.itemDrop(serverName, worldName, playerName, item, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Item-Drop") && main.getSqLite().isConnected()) {

                        SQLiteData.insertItemDrop(serverName, player, item, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemDropFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has dropped " + item + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }


            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                Discord.staffChat(player, "\uD83D\uDEAE **|** \uD83D\uDC6E\u200D♂ [" + worldName + "] Has dropped ** " + item + "**", false);

            } else {

                Discord.itemDrop(player, "\uD83D\uDEAE [" + worldName + "] Has dropped ** " + item + "**", false);

            }


            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.getConfig().getBoolean("Log.Item-Drop") && main.mySQL.isConnected()) {

                try {

                    MySQLData.itemDrop(serverName, worldName, playerName, item, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }


            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Item-Drop") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertItemDrop(serverName, player, item, player.hasPermission("logger.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
