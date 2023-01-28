package me.prism3.logger.events.inventories;

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
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnFurnace implements Listener {

    public final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInv(final FurnaceExtractEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String playerName = event.getPlayer().getName();
        final UUID playerUUID = player.getUniqueId();
        final String worldName = player.getWorld().getName();
        final int blockX = event.getBlock().getLocation().getBlockX();
        final int blockY = event.getBlock().getLocation().getBlockY();
        final int blockZ = event.getBlock().getLocation().getBlockZ();
        final Material item = event.getItemType();
        final int amount = event.getItemAmount();
        
        final Coordinates coordinates = new Coordinates(blockX, blockY, blockZ, worldName);

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%x%", String.valueOf(blockX));
        placeholders.put("%y%", String.valueOf(blockY));
        placeholders.put("%z%", String.valueOf(blockZ));
        placeholders.put("%amount%", String.valueOf(amount));
        placeholders.put("%item%", item.toString());

        //Log To Files Handling
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Furnace-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Furnace", placeholders, FileHandler.getFurnaceFile());
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Furnace-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Furnace-Craft", placeholders, DiscordChannels.FURNACE, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueFurnace(Data.serverName, playerName, playerUUID.toString(), item.toString(), amount, coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueFurnace(Data.serverName, playerName, playerUUID.toString(), item.toString(), amount, coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
