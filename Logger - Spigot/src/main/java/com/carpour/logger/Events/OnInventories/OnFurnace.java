package com.carpour.logger.Events.OnInventories;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class OnFurnace implements Listener {

    public final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInv(final FurnaceExtractEvent event){

        if (this.main.getConfig().getBoolean("Log-Player.Furnace")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = event.getPlayer().getName();
            final String worldName = player.getWorld().getName();
            final int blockX = event.getBlock().getLocation().getBlockX();
            final int blockY = event.getBlock().getLocation().getBlockY();
            final int blockZ = event.getBlock().getLocation().getBlockZ();
            final Material item = event.getItemType();
            final int amount = event.getItemAmount();

            //Log To Files Handling
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Furnace-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Furnace-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%item%", String.valueOf(item)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Furnace-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%item%", String.valueOf(item)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.furnace(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertFurnace(serverName, player, item, amount, blockX, blockY, blockZ, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getFurnaceFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Furnace")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%item%", String.valueOf(item)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Furnace-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Furnace-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%item%", String.valueOf(item)), false);

                    }

                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Furnace")).isEmpty()) {

                        Discord.furnace(player, Objects.requireNonNull(Messages.get().getString("Discord.Furnace")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(blockX)).replaceAll("%y%", String.valueOf(blockY)).replaceAll("%z%", String.valueOf(blockZ)).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%item%", String.valueOf(item)), false);
                    }
                }
            }

            // MySQL Handling
            if (isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.furnace(serverName, worldName, playerName, item, amount, blockX, blockY, blockZ, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite Handling
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertFurnace(serverName, player, item, amount, blockX, blockY, blockZ, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
