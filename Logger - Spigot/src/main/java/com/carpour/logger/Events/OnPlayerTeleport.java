package com.carpour.logger.Events;

import com.carpour.logger.API.AuthMeUtil;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import fr.xephi.authme.api.v3.AuthMeApi;
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
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class OnPlayerTeleport implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(final PlayerTeleportEvent event){

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Teleport")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt) ||
                    (AuthMeUtil.getAuthMeAPI() != null && !AuthMeApi.getInstance().isAuthenticated(player))) return;

            final World world = player.getWorld();
            final String worldName = world.getName();
            final String playerName = player.getName();
            final int tx = Objects.requireNonNull(event.getTo()).getBlockX();
            final int ty = event.getTo().getBlockY();
            final int tz = event.getTo().getBlockZ();
            final int ox = player.getLocation().getBlockX();
            final int oy = player.getLocation().getBlockY();
            final int oz = player.getLocation().getBlockZ();

            // Log To Files Handling
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)), false);
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Teleport-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && this.main.external.isConnected()) {

                        ExternalData.playerTeleport(serverName, worldName, playerName, ox, oy, oz, tx, ty, tz, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerTeleport(serverName, player, player.getLocation(), event.getTo(), true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerTeleportLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Teleport")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport")).isEmpty()) {

                        Discord.playerTeleport(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)), false);
                    }
                }
            }

            // MySQL
            if (isExternal && this.main.external.isConnected()) {

                try {

                    ExternalData.playerTeleport(serverName, worldName, playerName, ox, oy, oz, tx, ty, tz, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerTeleport(serverName, player, player.getLocation(), event.getTo(), player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
