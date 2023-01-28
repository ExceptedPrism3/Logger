package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class LeverInteraction implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLever(final PlayerInteractEvent event) {

        if (event.getClickedBlock() == null || event.isCancelled())
            return;

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock().getType().equals(Material.LEVER)) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final UUID playerUUID = player.getUniqueId();
            final String worldName = player.getWorld().getName();
            final String playerName = player.getName();
            final int x = event.getClickedBlock().getX();
            final int y = event.getClickedBlock().getY();
            final int z = event.getClickedBlock().getZ();

            final Coordinates coordinates = new Coordinates(x, y, z, worldName);

            final Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
            placeholders.put("%world%", worldName);
            placeholders.put("%uuid%", playerUUID.toString());
            placeholders.put("%player%", playerName);
            placeholders.put("%x%", String.valueOf(x));
            placeholders.put("%y%", String.valueOf(y));
            placeholders.put("%z%", String.valueOf(z));

            // Log To Files
            if (Data.isLogToFiles) {
                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                    FileHandler.handleFileLog("Files.Lever-Interaction-Staff", placeholders, FileHandler.getStaffFile());
                } else {
                    FileHandler.handleFileLog("Files.Lever-Interaction", placeholders, FileHandler.getLeverInteractionFile());
                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    this.main.getDiscord().handleDiscordLog("Discord.Lever-Interaction-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                } else {

                    this.main.getDiscord().handleDiscordLog("Discord.Lever-Interaction", placeholders, DiscordChannels.LEVER_INTERACTION, playerName, playerUUID);
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueLeverInteraction(Data.serverName, playerName, playerUUID.toString(), coordinates, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueLeverInteraction(Data.serverName, playerName, playerUUID.toString(), coordinates, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
