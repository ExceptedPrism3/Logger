package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onPlayerDeath implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)

    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        double x = Math.floor(player.getLocation().getX());
        double y = Math.floor(player.getLocation().getY());
        double z = Math.floor(player.getLocation().getZ());
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Death"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")){

                Discord.staffChat(player, "☠️ **|** \uD83D\uDC6E\u200D♂️ [" + worldName + "]" + " X = " + x + " Y = " + y + " Z = " + z, false, Color.RED);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has died at X = " + x + " Y = " + y + " Z = " + z + "\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Death")) && (main.mySQL.isConnected())) {


                        MySQLData.playerDeath(serverName, worldName, playerName, x, y, z, true);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.playerDeath(player, "☠️ [" + worldName + "]" + " X = " + x + " Y = " + y + " Z = " + z, false, Color.RED);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerDeathLogFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has died at X = " + x + " Y = " + y + " Z = " + z + "\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }

        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Death"))
                && (main.mySQL.isConnected())){

            try {

                MySQLData.playerDeath(serverName, worldName, playerName, x, y, z, false);

            }catch (Exception e){

             e.printStackTrace();

            }
        }

        if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Player-Death")
                && main.getSqLite().isConnected()) {

            try {
                SQLiteData.insertPlayerDeath(serverName, player, player.hasPermission("logger.staff.log"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
