package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.data.Coordinates;
import me.prism3.loggercore.database.entity.enums.InteractionType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;
import static me.prism3.logger.utils.Data.isStaffEnabled;

public class ItemFrameBreak implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemFrameBreak(final HangingBreakByEntityEvent event) {

        // Return if not a player
        if (event.isCancelled() || !(event.getRemover() instanceof Player)) return;

        if (event.getEntity() instanceof ItemFrame) {

            final Player player = (Player) event.getRemover();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final String worldName = player.getWorld().getName();
            final int x = event.getEntity().getLocation().getBlockX();
            final int y = event.getEntity().getLocation().getBlockY();
            final int z = event.getEntity().getLocation().getBlockZ();

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
                    FileHandler.handleFileLog("Files.Item-Frame-Break-Staff", placeholders, FileHandler.getStaffFile());
                } else {
                    FileHandler.handleFileLog("Files.Item-Frame-Break", placeholders, FileHandler.getItemFrameBreakFile());
                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    this.main.getDiscord().handleDiscordLog("Discord.Item-Frame-Break-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                } else {

                    this.main.getDiscord().handleDiscordLog("Discord.Item-Frame-Break", placeholders, DiscordChannels.ITEM_FRAME_BREAK, playerName, playerUUID);
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueBlockInteraction(Data.serverName, playerName, playerUUID.toString(),event.getEntity().getName(), coordinates, player.hasPermission(loggerStaffLog), InteractionType.BLOCK_BREAK);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueBlockInteraction(Data.serverName, playerName, playerUUID.toString(),event.getEntity().getName(), coordinates, player.hasPermission(loggerStaffLog), InteractionType.BLOCK_BREAK);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
