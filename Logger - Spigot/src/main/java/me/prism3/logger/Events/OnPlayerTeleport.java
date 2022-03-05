package me.prism3.logger.Events;

import me.prism3.logger.API.AuthMeUtil;
import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.FileHandler;
import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Database.SQLite.SQLiteData;
import me.prism3.logger.Utils.Messages;
import fr.xephi.authme.api.v3.AuthMeApi;
import me.prism3.logger.Utils.Data;
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

public class OnPlayerTeleport implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(final PlayerTeleportEvent event){

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Teleport")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) ||
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

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)), false);
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Teleport-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.playerTeleport(Data.serverName, worldName, playerName, ox, oy, oz, tx, ty, tz, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerTeleport(Data.serverName, player, player.getLocation(), event.getTo(), true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerTeleportLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Teleport")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport")).isEmpty()) {

                        Discord.playerTeleport(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Teleport")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%oldX%", String.valueOf(ox)).replaceAll("%oldY%", String.valueOf(oy)).replaceAll("%oldZ%", String.valueOf(oz)).replaceAll("%newX%", String.valueOf(tx)).replaceAll("%newY%", String.valueOf(ty)).replaceAll("%newZ%", String.valueOf(tz)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.playerTeleport(Data.serverName, worldName, playerName, ox, oy, oz, tx, ty, tz, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerTeleport(Data.serverName, player, player.getLocation(), event.getTo(), player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
