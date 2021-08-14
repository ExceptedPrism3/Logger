package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.MySQL.MySQLData;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class onSign implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)

    public void onPlayerSign(SignChangeEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        List<Component> lines = event.lines();
        String worldName = world.getName();
        double x = Math.floor(player.getLocation().getX());
        double y = Math.floor(player.getLocation().getY());
        double z = Math.floor(player.getLocation().getZ());
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")){ return; }

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Sign-Text"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")){

                Discord.staffChat(player, "\uD83E\uDEA7 **|** \uD83D\uDC6E\u200D♂ [" + worldName + "]" + " X = " + x + " Y = " + y + " Z = " + z + " **|** " + lines.get(0) + " **|** " + lines.get(1) + " **|** " + lines.get(2) + " **|** " + lines.get(3),false, Color.red);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + " | Location X = " + x + " Y = " + y + " Z = " + z + "] <" + playerName + "> " + "Line 1: " + lines.get(0) + " | Line 2: " + lines.get(1) + " | Line 3: " + lines.get(2) + " | Line 4: " + lines.get(3) + "\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Sign-Text")) && (main.sql.isConnected())) {

                        MySQLData.playerSignText(serverName, worldName, x, y, z, playerName, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", true);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.playerSignText(player, "\uD83E\uDEA7 [" + worldName + "]" + " X = " + x + " Y = " + y + " Z = " + z + " **|** " + lines.get(0) + " **|** " + lines.get(1) + " **|** " + lines.get(2) + " **|** " + lines.get(3),false, Color.red);

            try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getSignLogFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + " | Location X = " + x + " Y = " + y + " Z = " + z + "] <" + playerName + "> " + "Line 1: " + lines.get(0) + " | Line 2: " + lines.get(1) + " | Line 3: " + lines.get(2) + " | Line 4: " + lines.get(3) + "\n");
                    out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Sign-Text")) && (main.sql.isConnected())){

            try {

                MySQLData.playerSignText(serverName, worldName, x, y, z, playerName, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", false);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
