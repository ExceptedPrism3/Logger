package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class PortalCreation implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPortalCreate(final PortalCreateEvent event) {

        if (event.isCancelled())
            return;

        final String worldName = event.getWorld().getName();
        final PortalCreateEvent.CreateReason reason = event.getReason();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%material%", String.valueOf(reason));

        // Log To Files
        if (Data.isLogToFiles)
            FileHandler.handleFileLog("Files.Server-Side.Portal-Creation", placeholders, FileHandler.getPortalCreateFile());

        // DiscordManager
        if (this.main.getDiscordFile().get().getBoolean("DiscordManager.Enable"))
            this.main.getDiscord().handleDiscordLog("DiscordManager.Server-Side.Portal-Creation", placeholders, DiscordChannels.PORTAL_CREATION, "Portal Creation", null);

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
