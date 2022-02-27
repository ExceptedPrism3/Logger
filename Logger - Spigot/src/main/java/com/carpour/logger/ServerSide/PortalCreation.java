package com.carpour.logger.ServerSide;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
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
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class PortalCreation implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalCreate(final PortalCreateEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Server.Portal-Creation")) {

            final String worldName = event.getWorld().getName();
            final PortalCreateEvent.CreateReason reason = event.getReason();

            // Log To Files Handling
            if (isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPortalCreateFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.Portal-Creation")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%material%", String.valueOf(reason)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Portal-Creation")).isEmpty()) {

                Discord.portalCreation(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Portal-Creation")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%material%", String.valueOf(reason)), false);
            }

            // MySQL
            if (isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.portalCreate(serverName, worldName, reason);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPortalCreate(serverName, worldName, reason);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
