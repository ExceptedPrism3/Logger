package me.prism3.logger.events.inventories;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.time.ZonedDateTime;
import java.util.*;

import static me.prism3.logger.utils.Data.*;

public class OnChestInteraction implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpenEvent(final InventoryOpenEvent event) {

        if (event.isCancelled() || event.getInventory().getLocation() == null) return; //TODO FIX

        final Player player = (Player) event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        String chestName = "";
        switch (event.getInventory().getType()) {
            case CHEST:
                chestName = event.getInventory().getSize() == 54 ? "DoubleChest" : "Chest";
                break;
            case ENDER_CHEST:
                chestName = "EnderChest";
                break;
            case SHULKER_BOX:
                if (Main.getInstance().getVersion().isAtLeast(NmsVersions.v1_11_R1)) {
                    chestName = "ShulkerBox";
                }
                break;
            case BARREL:
                if (Main.getInstance().getVersion().isAtLeast(NmsVersions.v1_14_R1)) {
                    chestName = "Barrel";
                }
                break;
            default:
                return;
        }

        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final String worldName = player.getWorld().getName();
        final int x = event.getInventory().getLocation().getBlockX();
        final int y = event.getInventory().getLocation().getBlockY();
        final int z = event.getInventory().getLocation().getBlockZ();

        final StringBuilder itemsBuilder = new StringBuilder();

        for (ItemStack stack : event.getInventory().getContents()) {
            if (stack == null) continue;
            itemsBuilder.append("(");
            if (stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
                itemsBuilder.append(stack.getItemMeta().getDisplayName());

            itemsBuilder.append(")");
            itemsBuilder.append(stack.getType().name().replace("_", " "));
            itemsBuilder.append(" x ");
            itemsBuilder.append(stack.getAmount());
            itemsBuilder.append(", ");
        }

        final String items = itemsBuilder.toString();


        final Coordinates coordinates = new Coordinates(x, y, z, worldName);

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%x%", String.valueOf(x));
        placeholders.put("%y%", String.valueOf(y));
        placeholders.put("%z%", String.valueOf(z));
        placeholders.put("%chest%", chestName);
        placeholders.put("%items%", items);

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Chest-Interaction-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Chest-Interaction", placeholders, FileHandler.getChestInteractionFile());
            }
        }

        // DiscordManager Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("DiscordManager.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("DiscordManager.Chest-Interaction-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("DiscordManager.Chest-Interaction", placeholders, DiscordChannels.CHEST_INTERACTION, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

//                Main.getInstance().getDatabase().getDatabaseQueue().queueChestInteraction(Data.serverName, playerName, playerUUID.toString(), coordinates, items, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

//                Main.getInstance().getDatabase().getDatabaseQueue().queueChestInteraction(Data.serverName, playerName, playerUUID.toString(), coordinates, items, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}