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
import org.bukkit.event.player.PlayerTeleportEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class onPlayerTeleport implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent event){

        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldname = world.getName();
        String playername = player.getName();
        int tx = Objects.requireNonNull(event.getTo()).getBlockX();
        int ty = event.getTo().getBlockY();
        int tz = event.getTo().getBlockZ();
        double ox = Math.floor(player.getLocation().getX());
        double oy = Math.floor(player.getLocation().getY());
        double oz = Math.floor(player.getLocation().getZ());
        String staff = "false";
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Teleport"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                Discord.staffChat(player, "\uD83D\uDDFA **|** \uD83D\uDC6E\u200D♂️ [" + worldname + "]" + " From X = " + ox + " Y = " + oy + " Z = " + oz + " **To** X = " + tx + " Y = " + ty + " Z = " + tz, false, Color.green);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldname + "] The Staff " + playername + " has teleported from X = " + ox + " Y = " + oy + " Z = " + oz + " to => X = " + tx + " Y = " + ty + " Z = " + tz +  "\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Teleport")) && (main.SQL.isConnected())) {

                        staff = "true";
                        MySQLData.playerTeleport(serverName, worldname, playername, ox, oy, oz, tx, ty, tz, staff);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.playerTeleport(player, "\uD83D\uDDFA [" + worldname + "]" + " From X = " + ox + " Y = " + oy + " Z = " + oz + " **To** X = " + tx + " Y = " + ty + " Z = " + tz, false, Color.green);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerTeleportLogFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldname + "] The Player " + playername + " has teleported from X = " + ox + " Y = " + oy + " Z = " + oz + " to => X = " + tx + " Y = " + ty + " Z = " + tz +  "\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }

        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Teleport")) && (main.SQL.isConnected())){

            try {

                MySQLData.playerTeleport(serverName, worldname, playername, ox, oy, oz, tx, ty, tz, staff);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
