package me.prism3.logger.events.misc;

import com.carpour.loggercore.database.entity.Coordinates;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerExempt;
import static me.prism3.logger.utils.Data.loggerStaffLog;

public class ItemFramePlace implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFramePlace(final HangingPlaceEvent event) {

        if (event.getEntity() instanceof ItemFrame) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final String worldName = player.getWorld().getName();
            final int x = event.getBlock().getX();
            final int y = event.getBlock().getY();
            final int z = event.getBlock().getZ();

            final Coordinates coordinates = new Coordinates(x, y, z, worldName);

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Item-Frame-Place-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%uuid%", playerUUID.toString()).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemFramePlaceFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Item-Frame-Place").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%uuid%", playerUUID.toString()).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Item-Frame-Place-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Item-Frame-Place-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%uuid%", playerUUID.toString()).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);

                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Item-Frame-Place").isEmpty()) {

                        this.main.getDiscord().itemFramePlace(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Item-Frame-Place").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%uuid%", playerUUID.toString()).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertItemFramePlace(Data.serverName, playerName, playerUUID.toString(), coordinates, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertItemFramePlace(Data.serverName, playerName, playerUUID.toString(), coordinates, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
