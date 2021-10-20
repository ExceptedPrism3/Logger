package com.carpour.logger.ServerSide;

import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PortalCreation implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalCreate(PortalCreateEvent event) {

        String worldName = event.getWorld().getName();
        PortalCreateEvent.CreateReason reason = event.getReason();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (!event.isCancelled() && main.getConfig().getBoolean("Log.Portal-Creation")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPortalCreateFile(), true));
                    out.write("[" + dateFormat.format(date) + "] A portal has been created in " + worldName + " using " + reason + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            Discord.portalCreation("\uD83D\uDEAA A portal has been created in **" + worldName + "** using **" + reason + "**", false);

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.getConfig().getBoolean("Log.Portal-Creation") && main.mySQL.isConnected()) {

                try {

                    MySQLData.portalCreate(serverName, worldName, reason);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Portal-Creation") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPortalCreate(serverName, worldName, reason);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
