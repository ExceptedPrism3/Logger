package me.prism3.logger.events;

import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Messages;
import me.prism3.logger.utils.Data;
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

public class OnItemPickup implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPick(final EntityPickupItemEvent event) {

        if (event.getEntity() instanceof Player && !event.isCancelled()
                && this.main.getConfig().getBoolean("Log-Player.Item-Pickup")) {

            final Player player = (Player) event.getEntity();

            if (player.hasPermission(Data.loggerExempt)) return;

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

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Item-Pickup-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.itemPickup(Data.serverName, player, item, amount, blockX, blockY, blockZ, itemName, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertItemPickup(Data.serverName, player, item, blockX, amount, blockY, blockZ, itemName, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemPickupFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Item-Pickup")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup")).isEmpty()) {

                        Discord.itemPickup(player, Objects.requireNonNull(Messages.get().getString("Discord.Item-Pickup")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", String.valueOf(item)).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.itemPickup(Data.serverName, player, item, amount, blockX, blockY, blockZ, itemName, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertItemPickup(Data.serverName, player, item, amount, blockX, blockY, blockZ, itemName, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
