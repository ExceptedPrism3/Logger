package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class PortalCreation implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalCreate(final PortalCreateEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Server.Portal-Creation")) {

            final String worldName = event.getWorld().getName();
            final PortalCreateEvent.CreateReason reason = event.getReason();

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPortalCreateFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Server-Side.Portal-Creation")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%material%", String.valueOf(reason)) + "\n");
                    out.close();

                } catch (IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.Portal-Creation")).isEmpty()) {

                this.main.getDiscord().portalCreation(Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.Portal-Creation")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%material%", String.valueOf(reason)), false);
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.portalCreate(Data.serverName, worldName, reason);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPortalCreate(Data.serverName, worldName, reason);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
