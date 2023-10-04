package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.FriendlyEnchants;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OnItemPickup implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPick(final PlayerPickupItemEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Item-Pickup")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final Material item = event.getItem().getItemStack().getType();
            String itemName = Objects.requireNonNull(event.getItem().getItemStack().getItemMeta()).getDisplayName();

            if (itemName != null) {

                itemName = itemName.contains("\\") ? itemName : itemName.replace("\\", "\\\\");

            } else itemName =  " ";

            final int amount = event.getItem().getItemStack().getAmount();
            final World world = player.getWorld();
            final String worldName = world.getName();
            final int blockX = event.getItem().getLocation().getBlockX();
            final int blockY = event.getItem().getLocation().getBlockY();
            final int blockZ = event.getItem().getLocation().getBlockZ();

            final List<String> enchs = event.getItem().getItemStack().getEnchantments().entrySet().stream()
                    .map(entry -> {
                        final Enchantment enchantment = entry.getKey();
                        final int level = entry.getValue();
                        final String friendlyName = FriendlyEnchants.getFriendlyEnchantment(enchantment).getFriendlyName();
                        return friendlyName + " " + level;
                    })
                    .collect(Collectors.toList());

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Pickup-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Pickup-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", itemName).replace("%enchantment%", enchs.toString()), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Item-Pickup-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", itemName).replace("%enchantment%", enchs.toString()) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.itemPickup(Data.serverName, player, item, amount, blockX, blockY, blockZ, itemName, enchs, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertItemPickup(Data.serverName, player, item, blockX, amount, blockY, blockZ, itemName, enchs, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemPickupFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Item-Pickup")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", itemName).replace("%enchantment%", enchs.toString()) + "\n");
                    out.close();

                } catch (IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Pickup-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Pickup-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", itemName), false);

                    }
                } else {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Pickup")).isEmpty()) {

                        this.main.getDiscord().itemPickup(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Pickup")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", itemName).replace("%enchantment%", enchs.toString()), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.itemPickup(Data.serverName, player, item, amount, blockX, blockY, blockZ, itemName, enchs, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertItemPickup(Data.serverName, player, item, amount, blockX, blockY, blockZ, itemName, enchs, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
