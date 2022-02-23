package com.carpour.logger.Events;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class OnItemPickup implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPick(final EntityPickupItemEvent event) {

        if (event.getEntity() instanceof Player && !event.isCancelled()
                && this.main.getConfig().getBoolean("Log-Player.Item-Pickup")) {

            final Player player = (Player) event.getEntity();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final Material item = event.getItem().getItemStack().getType();
            String itemName = Objects.requireNonNull(event.getItem().getItemStack().getItemMeta()).getDisplayName();

            if (itemName != null){

                itemName = itemName.contains("\\") ? itemName : itemName.replace("\\", "\\\\");

            } else itemName =  " ";

            final int amount = event.getItem().getItemStack().getAmount();
            final World world = player.getWorld();
            final String worldName = world.getName();
            final int blockX = event.getItem().getLocation().getBlockX();
            final int blockY = event.getItem().getLocation().getBlockY();
            final int blockZ = event.getItem().getLocation().getBlockZ();

            // Log To Files Handling
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", String.valueOf(item)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Item-Pickup-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%item%", String.valueOf(item)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && this.main.external.isConnected()) {

                        ExternalData.itemPickup(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, itemName, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertItemPickup(serverName, player, item, blockX, amount, blockY, blockZ, itemName, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemPickupFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Item-Pickup")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%item%", String.valueOf(item)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", String.valueOf(item)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup")).isEmpty()) {

                        Discord.itemPickup(player, Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", String.valueOf(item)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%renamed%", String.valueOf(itemName)), false);
                    }
                }
            }

            // MySQL
            if (isExternal && this.main.external.isConnected()) {

                try {

                    ExternalData.itemPickup(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, itemName, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertItemPickup(serverName, player, item, amount, blockX, blockY, blockZ, itemName, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
