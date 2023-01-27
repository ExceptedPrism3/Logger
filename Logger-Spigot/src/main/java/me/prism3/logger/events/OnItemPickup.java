package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.data.Coordinates;
import me.prism3.loggercore.database.entity.enums.ItemActionType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnItemPickup implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPick(final EntityPickupItemEvent event) {

        if (!event.isCancelled() && event.getEntity() instanceof Player) {

            final Player player = (Player) event.getEntity();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final Material item = event.getItem().getItemStack().getType();
            String itemName = event.getItem().getItemStack().getItemMeta().getDisplayName();

            if (itemName != null) {

                itemName = itemName.contains("\\") ? itemName : itemName.replace("\\", "\\\\");

            } else itemName =  " ";

            final int amount = event.getItem().getItemStack().getAmount();
            final String worldName = player.getWorld().getName();
            final int blockX = event.getItem().getLocation().getBlockX();
            final int blockY = event.getItem().getLocation().getBlockY();
            final int blockZ = event.getItem().getLocation().getBlockZ();

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
            placeholders.put("%item%", String.valueOf(item));
            placeholders.put("%renamed%", itemName);

            // Log To Files
            if (Data.isLogToFiles) {
                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                    FileHandler.handleFileLog("Files.Item-Pickup-Staff", placeholders, FileHandler.getStaffFile());
                } else {
                    FileHandler.handleFileLog("Files.Item-Pickup", placeholders, FileHandler.getItemPickupFile());
                }
            }

            // DiscordManager Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("DiscordManager.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    this.main.getDiscord().handleDiscordLog("DiscordManager.Item-Pickup-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                } else {

                    this.main.getDiscord().handleDiscordLog("DiscordManager.Item-Pickup", placeholders, DiscordChannels.ITEM_PICKUP, playerName, playerUUID);
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueItemAction(Data.serverName, playerName, playerUUID.toString(), item.name(), amount, coordinates, "", itemName, player.hasPermission(loggerStaffLog),  ItemActionType.ITEM_PICKUP);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {
//TODO enchantments
                    Main.getInstance().getDatabase().getDatabaseQueue().queueItemAction(Data.serverName, playerName, playerUUID.toString(), item.name(), amount, coordinates, "", itemName, player.hasPermission(loggerStaffLog), ItemActionType.ITEM_PICKUP);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
