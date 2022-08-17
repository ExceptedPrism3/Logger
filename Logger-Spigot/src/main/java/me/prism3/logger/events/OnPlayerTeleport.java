package me.prism3.logger.events;

import com.carpour.loggercore.database.entity.Coordinates;
import fr.xephi.authme.api.v3.AuthMeApi;
import me.prism3.logger.Main;
import me.prism3.logger.api.AuthMeUtil;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnPlayerTeleport implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(final PlayerTeleportEvent event) {

        if (event.getPlayer().hasMetadata("NPC")) return; // Return if player is NPC. Could be Villager or NPC from Citizens etc...

        if (!event.isCancelled()) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) ||
                    (AuthMeUtil.getAuthMeAPI() != null && !AuthMeApi.getInstance().isAuthenticated(player))
                    || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final World world = player.getWorld();
            final String worldName = world.getName();
            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final int tx = event.getTo().getBlockX();
            final int ty = event.getTo().getBlockY();
            final int tz = event.getTo().getBlockZ();
            final int ox = player.getLocation().getBlockX();
            final int oy = player.getLocation().getBlockY();
            final int oz = player.getLocation().getBlockZ();


            final Coordinates oldCoords = new Coordinates(ox, oy, oz, worldName);
            final Coordinates newCoords = new Coordinates(tx, ty, tz, worldName);

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Player-Teleport-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%oldX%", String.valueOf(ox)).replace("%oldY%", String.valueOf(oy)).replace("%oldZ%", String.valueOf(oz)).replace("%newX%", String.valueOf(tx)).replace("%newY%", String.valueOf(ty)).replace("%newZ%", String.valueOf(tz)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerTeleportLogFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Player-Teleport").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%oldX%", String.valueOf(ox)).replace("%oldY%", String.valueOf(oy)).replace("%oldZ%", String.valueOf(oz)).replace("%newX%", String.valueOf(tx)).replace("%newY%", String.valueOf(ty)).replace("%newZ%", String.valueOf(tz)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Player-Teleport-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Teleport-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%oldX%", String.valueOf(ox)).replace("%oldY%", String.valueOf(oy)).replace("%oldZ%", String.valueOf(oz)).replace("%newX%", String.valueOf(tx)).replace("%newY%", String.valueOf(ty)).replace("%newZ%", String.valueOf(tz)), false);

                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Player-Teleport").isEmpty()) {

                        this.main.getDiscord().playerTeleport(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Teleport").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%oldX%", String.valueOf(ox)).replace("%oldY%", String.valueOf(oy)).replace("%oldZ%", String.valueOf(oz)).replace("%newX%", String.valueOf(tx)).replace("%newY%", String.valueOf(ty)).replace("%newZ%", String.valueOf(tz)), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertPlayerTeleport(Data.serverName, playerName, playerUUID.toString(), oldCoords, newCoords, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertPlayerTeleport(Data.serverName, playerName, playerUUID.toString(), oldCoords, newCoords, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
