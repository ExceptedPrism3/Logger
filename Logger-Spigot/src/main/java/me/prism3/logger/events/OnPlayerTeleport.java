package me.prism3.logger.events;

import fr.xephi.authme.api.v3.AuthMeApi;
import me.prism3.logger.Main;
import me.prism3.logger.api.AuthMeUtil;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
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
import java.util.Objects;

public class OnPlayerTeleport implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(final PlayerTeleportEvent event) {

        if (event.getPlayer().hasMetadata("NPC")) return; // Return if player is NPC. Could be Villager or NPC from Citizens etc...

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

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Teleport-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Teleport-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%oldX%", String.valueOf(ox)).replace("%oldY%", String.valueOf(oy)).replace("%oldZ%", String.valueOf(oz)).replace("%newX%", String.valueOf(tx)).replace("%newY%", String.valueOf(ty)).replace("%newZ%", String.valueOf(tz)), false);
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Player-Teleport-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%oldX%", String.valueOf(ox)).replace("%oldY%", String.valueOf(oy)).replace("%oldZ%", String.valueOf(oz)).replace("%newX%", String.valueOf(tx)).replace("%newY%", String.valueOf(ty)).replace("%newZ%", String.valueOf(tz)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.playerTeleport(Data.serverName, player, tx, ty, tz, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerTeleport(Data.serverName, player, player.getLocation(), event.getTo(), true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerTeleportLogFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Player-Teleport")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%oldX%", String.valueOf(ox)).replace("%oldY%", String.valueOf(oy)).replace("%oldZ%", String.valueOf(oz)).replace("%newX%", String.valueOf(tx)).replace("%newY%", String.valueOf(ty)).replace("%newZ%", String.valueOf(tz)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Teleport-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Teleport-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%oldX%", String.valueOf(ox)).replace("%oldY%", String.valueOf(oy)).replace("%oldZ%", String.valueOf(oz)).replace("%newX%", String.valueOf(tx)).replace("%newY%", String.valueOf(ty)).replace("%newZ%", String.valueOf(tz)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Teleport")).isEmpty()) {

                        this.main.getDiscord().playerTeleport(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Teleport")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%oldX%", String.valueOf(ox)).replace("%oldY%", String.valueOf(oy)).replace("%oldZ%", String.valueOf(oz)).replace("%newX%", String.valueOf(tx)).replace("%newY%", String.valueOf(ty)).replace("%newZ%", String.valueOf(tz)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.playerTeleport(Data.serverName, player, tx, ty, tz, player.hasPermission(Data.loggerStaffLog));

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
