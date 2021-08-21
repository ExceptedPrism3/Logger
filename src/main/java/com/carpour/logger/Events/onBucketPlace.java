package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.database.MySQL.MySQLData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onBucketPlace implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)

    public void onBucket(PlayerBucketEmptyEvent event){
        Player player = event.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        String worldName = world.getName();
        String bucket = event.getBucket().toString();
        bucket = bucket.replaceAll("_", " ");
        bucket = bucket.replaceAll("LEGACY", "");
        double x = Math.floor(player.getLocation().getX());
        double y = Math.floor(player.getLocation().getY());
        double z = Math.floor(player.getLocation().getZ());
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Bucket-Place"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                Discord.staffChat(player, "\uD83E\uDEA3️ **|** \uD83D\uDC6E\u200D♂️️ [" + worldName + "] Has placed a **" + bucket + "** at X = " + x + " Y = " + y + " Z = " + z, false, Color.red);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has placed " + bucket + " at X = " + x + " Y = " + y + " Z = " + z + "\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Bucket-Place")) && (main.mySQL.isConnected())) {

                        MySQLData.bucketPlace(serverName, worldName, playerName, bucket, x, y, z, true);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.bucketPlace(player,"\uD83E\uDEA3️ [" + worldName + "] Has placed a **" + bucket + "** at X = " + x + " Y = " + y + " Z = " + z, false, Color.red);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBucketPlaceFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has placed " + bucket + " at X = " + x + " Y = " + y + " Z = " + z + "\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }

        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Bucket-Place")) && (main.mySQL.isConnected())){

            try {

                MySQLData.bucketPlace(serverName, worldName, playerName, bucket, x, y, z, false);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
