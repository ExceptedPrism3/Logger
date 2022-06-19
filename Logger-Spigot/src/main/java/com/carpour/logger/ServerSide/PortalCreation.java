package com.carpour.logger.ServerSide;

import org.carour.loggercore.database.mysql.MySQLData;
import org.carour.loggercore.database.sqlite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
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
import java.util.Objects;

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
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Portal-Creation")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%material%", String.valueOf(reason)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            main.getDiscord().sendPortalCreation(Objects.requireNonNull(Messages.get().getString("Discord.Portal-Creation")).replaceAll("%world%", worldName).replaceAll("%material%", String.valueOf(reason)), false);

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

                try {

                    main.getMySQLData().portalCreate(serverName, worldName, reason.name());

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    main.getSqLiteData().insertPortalCreate(serverName, worldName, reason.name());

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
