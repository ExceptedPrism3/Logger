package com.carpour.logger.ServerSide;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.MySQL.MySQLData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PortalCreation implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPortalCreate(PortalCreateEvent event) {

        String worldname = event.getWorld().getName();
        PortalCreateEvent.CreateReason reason = event.getReason();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Portal-Creation"))) {

            Discord.portalCreation("\uD83D\uDEAA A portal has been created in **" + worldname + "** using **" + reason + "**", false, Color.yellow);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPortalCreateFile(), true));
                out.write("[" + dateFormat.format(date) + "] A portal has been created in " + worldname + " using " + reason + "\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Portal-Creation")) && (main.SQL.isConnected())) {

            try {

                MySQLData.portalCreate(serverName, worldname, reason);

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }
}
