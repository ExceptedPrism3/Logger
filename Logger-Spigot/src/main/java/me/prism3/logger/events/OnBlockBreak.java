package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.data.Coordinates;
import me.prism3.loggercore.database.entity.enums.InteractionType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnBlockBreak implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(final BlockBreakEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final String worldName = player.getWorld().getName();
        final int x = event.getBlock().getLocation().getBlockX();
        final int y = event.getBlock().getLocation().getBlockY();
        final int z = event.getBlock().getLocation().getBlockZ();
        final Material blockType = event.getBlock().getType();

        final Coordinates coordinates = new Coordinates(x, y, z, worldName);

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%x%", String.valueOf(x));
        placeholders.put("%y%", String.valueOf(y));
        placeholders.put("%z%", String.valueOf(z));
        placeholders.put("%block%", blockType.toString());

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Block-Break-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Block-Break", placeholders, FileHandler.getBlockBreakLogFile());
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Block-Break-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Block-Break", placeholders, DiscordChannels.BLOCK_BREAK, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueBlockInteraction(Data.serverName, playerName, playerUUID.toString(), blockType.name(), coordinates, player.hasPermission(loggerStaffLog), InteractionType.BLOCK_BREAK);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueBlockInteraction(Data.serverName, playerName, playerUUID.toString(), blockType.name(), coordinates, player.hasPermission(loggerStaffLog), InteractionType.BLOCK_BREAK);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
