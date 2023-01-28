package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.enums.FriendlyEnchants;
import me.prism3.loggercore.database.data.Coordinates;
import me.prism3.loggercore.database.entity.enums.ItemActionType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.time.ZonedDateTime;
import java.util.*;

import static me.prism3.logger.utils.Data.*;

public class OnItemDrop implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(final PlayerDropItemEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final String worldName = player.getWorld().getName();
        final String item = event.getItemDrop().getItemStack().getType().name().replace("_", " ");
        String itemName = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();

        if (itemName != null) {

            itemName = itemName.contains("\\") ? itemName : itemName.replace("\\", "\\\\");

        } else itemName = " ";

        final int amount = event.getItemDrop().getItemStack().getAmount();
        final int blockX = event.getItemDrop().getLocation().getBlockX();
        final int blockY = event.getItemDrop().getLocation().getBlockY();
        final int blockZ = event.getItemDrop().getLocation().getBlockZ();
        final List<String> enchs = new ArrayList<>();

        for (Enchantment ench : event.getItemDrop().getItemStack().getEnchantments().keySet())
            enchs.add(FriendlyEnchants.getFriendlyEnchantment(ench).getFriendlyName());

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
        placeholders.put("%item%", item);
        placeholders.put("%renamed%", itemName);
        placeholders.put("%enchantment%", String.valueOf(enchs));

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Item-Drop-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Item-Drop", placeholders, FileHandler.getItemDropFile());
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Item-Drop-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Item-Drop", placeholders, DiscordChannels.ITEM_DROP, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueItemAction(Data.serverName, playerName, playerUUID.toString(), item, amount, coordinates, enchs.toString(), itemName, player.hasPermission(loggerStaffLog), ItemActionType.ITEM_DROP);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueItemAction(Data.serverName, playerName, playerUUID.toString(), item, amount, coordinates, enchs.toString(), itemName, player.hasPermission(loggerStaffLog), ItemActionType.ITEM_DROP);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
