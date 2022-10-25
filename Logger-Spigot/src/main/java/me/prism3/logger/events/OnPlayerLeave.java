package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnPlayerLeave implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String worldName = player.getWorld().getName();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final int x = player.getLocation().getBlockX();
        final int y = player.getLocation().getBlockY();
        final int z = player.getLocation().getBlockZ();

        final Coordinates coordinates = new Coordinates(x, y, z, worldName);

        // Log To Files
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {
                   
                    out.write(this.main.getMessages().get().getString("Files.Player-Leave-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%uuid%", playerUUID.toString()) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerLeaveLogFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Player-Leave").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%uuid%", playerUUID.toString()) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }
        }

        // Discord
        if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("Discord.Player-Leave-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Leave-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%uuid%", playerUUID.toString()), false);
                }
            } else {
                if (!this.main.getMessages().get().getString("Discord.Player-Leave").isEmpty()) {

                    this.main.getDiscord().playerLeave(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Leave").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%uuid%", playerUUID.toString()), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getQueueManager().queuePlayerLeave(Data.serverName, playerName, playerUUID.toString(), coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getQueueManager().queuePlayerLeave(Data.serverName, playerName, playerUUID.toString(), coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
