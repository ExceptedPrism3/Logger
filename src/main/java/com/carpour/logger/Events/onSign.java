package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.MySQL.MySQLData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onSign implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)

    public void onPlayerSign(SignChangeEvent event) {
        Player player = event.getPlayer();
        String playername = player.getName();
        World world = player.getWorld();
        String[] lines = event.getLines();
        String worldname = world.getName();
        double x = Math.floor(player.getLocation().getX());
        double y = Math.floor(player.getLocation().getY());
        double z = Math.floor(player.getLocation().getZ());
        String staff = "false";
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Sign-Text"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                Discord.staffChat(player, "\uD83E\uDEA7 **|** \uD83D\uDC6E\u200D♂ [" + worldname + "]" + " X = " + x + " Y = " + y + " Z = " + z + " **|** " + lines[0] + " **|** " + lines[1] + " **|** " + lines[2] + " **|** " + lines[3],false, Color.red);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldname + " | Location X = " + x + " Y = " + y + " Z = " + z + "] <" + playername + "> " + "Line 1: " + lines[0] + " | Line 2: " + lines[1] + " | Line 3: " + lines[2] + " | Line 4: " + lines[3] + "\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Sign-Text")) && (main.SQL.isConnected())) {

                        staff = "true";
                        MySQLData.playerSignText(serverName, worldname, x, y, z, playername, "[" + lines[0] + "] " + "[" + lines[1] + "] " + "[" + lines[2] + "] " + "[" + lines[3] + "]", staff);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.playerSignText(player, "\uD83E\uDEA7 [" + worldname + "]" + " X = " + x + " Y = " + y + " Z = " + z + " **|** " + lines[0] + " **|** " + lines[1] + " **|** " + lines[2] + " **|** " + lines[3],false, Color.red);

            try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getSignLogFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldname + " | Location X = " + x + " Y = " + y + " Z = " + z + "] <" + playername + "> " + "Line 1: " + lines[0] + " | Line 2: " + lines[1] + " | Line 3: " + lines[2] + " | Line 4: " + lines[3] + "\n");
                    out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Sign-Text")) && (main.SQL.isConnected())){

            try {

                MySQLData.playerSignText(serverName, worldname, x, y, z, playername, "[" + lines[0] + "] " + "[" + lines[1] + "] " + "[" + lines[2] + "] " + "[" + lines[3] + "]", staff);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
