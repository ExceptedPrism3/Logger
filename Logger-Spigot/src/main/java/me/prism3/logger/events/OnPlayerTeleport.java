package me.prism3.logger.events;

import fr.xephi.authme.api.v3.AuthMeApi;
import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.hooks.AuthMeUtil;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnPlayerTeleport implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(final PlayerTeleportEvent event) {

        if (event.isCancelled() || event.getPlayer().hasMetadata("NPC"))
            return; // Return if player is NPC. Could be Villager or NPC from Citizens etc...

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || (AuthMeUtil.isAllowed && !AuthMeApi.getInstance().isAuthenticated(player))
                || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String worldName = player.getWorld().getName();
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

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%oldX%", String.valueOf(ox));
        placeholders.put("%oldY%", String.valueOf(oy));
        placeholders.put("%oldZ%", String.valueOf(oz));
        placeholders.put("%newX%", String.valueOf(tx));
        placeholders.put("%newY%", String.valueOf(ty));
        placeholders.put("%newZ%", String.valueOf(tz));

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Player-Teleport-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Player-Teleport", placeholders, FileHandler.getPlayerTeleportLogFile());
            }
        }

        // DiscordManager Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("DiscordManager.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("DiscordManager.Player-Teleport-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("DiscordManager.Player-Teleport", placeholders, DiscordChannels.PLAYER_TELEPORT, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerTeleport(Data.serverName, playerName, playerUUID.toString(), oldCoords, newCoords, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerTeleport(Data.serverName, playerName, playerUUID.toString(), oldCoords, newCoords, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
