package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.MySQL.MySQLData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onPlayerKick implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTabCompletion(PlayerKickEvent event){

        Player player = event.getPlayer();
        String reason = event.getReason();
        String worldname = player.getWorld().getName();
        String playername = player.getName();
        double x = Math.floor(player.getLocation().getX());
        double y = Math.floor(player.getLocation().getY());
        double z = Math.floor(player.getLocation().getZ());
        String staff = "false";
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Kick"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                Discord.staffChat(player, "\uD83E\uDD7E **|** \uD83D\uDC6E\u200D♂ [" + worldname + "]" + " X = " + x + " Y = " + y + " Z = " + z + " **Reason** " + reason, false, Color.blue);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldname + "] The Staff <" + playername + "> got kicked at X = " + x + " Y = " + y + " Z = " + z + " for " + reason + "\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Kick")) && (main.SQL.isConnected())) {

                        staff = "true";
                        MySQLData.playerKick(serverName, worldname, playername, x, y, z, reason, staff);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.playerKick(player, "\uD83E\uDD7E [" + worldname + "]" + " X = " + x + " Y = " + y + " Z = " + z + " **Reason** " + reason, false, Color.blue);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerKickLogFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldname + "] The Player <" + playername + "> got kicked at X = " + x + " Y = " + y + " Z = " + z + " for " + reason + "\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }

        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Kick")) && (main.SQL.isConnected())){

            try {

                MySQLData.playerKick(serverName, worldname, playername, x, y, z, reason, staff);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
