package me.prism3.logger.serverside;

import me.prism3.logger.Main;
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

public class PortalCreation implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalCreate(final PortalCreateEvent event) {

        if (!event.isCancelled()) {

            final String worldName = event.getWorld().getName();
            final PortalCreateEvent.CreateReason reason = event.getReason();

            // Log To Files
            if (Data.isLogToFiles) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPortalCreateFile(), true))) {
                    
                    out.write(this.main.getMessages().get().getString("Files.Server-Side.Portal-Creation").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%material%", String.valueOf(reason)) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }

            // Discord
            if (!this.main.getMessages().get().getString("Discord.Server-Side.Portal-Creation").isEmpty() && this.main.getDiscordFile().getBoolean("Discord.Enable"))
                this.main.getDiscord().portalCreation(this.main.getMessages().get().getString("Discord.Server-Side.Portal-Creation").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%material%", String.valueOf(reason)), false);

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queuePortalCreate(Data.serverName, worldName, reason.toString());

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queuePortalCreate(Data.serverName, worldName, reason.toString());

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
