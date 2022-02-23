package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Utils.Enum.FriendlyEnchants;
import com.carpour.logger.Main;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static com.carpour.logger.Utils.Data.*;

public class OnItemDrop implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(final PlayerDropItemEvent event){

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Item-Drop")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final World world = player.getWorld();
            final String worldName = world.getName();
            final String item = event.getItemDrop().getItemStack().getType().name().replace("_", " ");
            String itemName = Objects.requireNonNull(event.getItemDrop().getItemStack().getItemMeta()).getDisplayName();

            if (itemName != null) {

                itemName = itemName.contains("\\") ? itemName : itemName.replace("\\", "\\\\");

            } else itemName = " ";

            final int amount = event.getItemDrop().getItemStack().getAmount();
            final int blockX = event.getItemDrop().getLocation().getBlockX();
            final int blockY = event.getItemDrop().getLocation().getBlockY();
            final int blockZ = event.getItemDrop().getLocation().getBlockZ();
            final List<String> enchs = new ArrayList<>();

            for (Enchantment ench : event.getItemDrop().getItemStack().getEnchantments().keySet()) {

                enchs.add(FriendlyEnchants.getFriendlyEnchantment(ench).getFriendlyName());

            }

            // Log To Files Handling
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Item-Drop-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Item-Drop-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)).replaceAll("%enchantment%", String.valueOf(enchs)), false);
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Item-Drop-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)).replaceAll("%enchantment%", String.valueOf(enchs)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && this.main.external.isConnected()) {

                        ExternalData.itemDrop(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, enchs, itemName, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertItemDrop(serverName, player, item, blockX, amount, blockY, blockZ, enchs, itemName, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemDropFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Item-Drop")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)).replaceAll("%enchantment%", String.valueOf(enchs)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Item-Drop-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Item-Drop-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)).replaceAll("%enchantment%", String.valueOf(enchs)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Item-Drop")).isEmpty()) {

                        Discord.itemDrop(player, Objects.requireNonNull(Messages.get().getString("Discord.Item-Drop")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)).replaceAll("%enchantment%", String.valueOf(enchs)), false);
                    }
                }
            }

            // MySQL
            if (isExternal && this.main.external.isConnected()) {

                try {

                    ExternalData.itemDrop(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, enchs, itemName, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertItemDrop(serverName, player, item, amount, blockX, blockY, blockZ, enchs, itemName, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
