package me.prism3.logger.events.inventories;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.loggercore.database.data.Coordinates;
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
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

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

        //Log To Files Handling
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {
                   
                    out.write(this.main.getMessages().get().getString("Files.Furnace-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%amount%", String.valueOf(amount)).replace("%item%", String.valueOf(item)) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getFurnaceFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Furnace").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%amount%", String.valueOf(amount)).replace("%item%", String.valueOf(item)) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }
        }

        // Discord Integration
        if (!player.hasPermission(Data.loggerExemptDiscord)) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("Discord.Furnace-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Furnace-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%amount%", String.valueOf(amount)).replace("%item%", String.valueOf(item)), false);

                }

            } else {

                if (!this.main.getMessages().get().getString("Discord.Furnace").isEmpty()) {

                    this.main.getDiscord().furnace(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Furnace").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%amount%", String.valueOf(amount)).replace("%item%", String.valueOf(item)), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertFurnace(Data.serverName, playerName, playerUUID.toString(), item.toString(), amount, coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertFurnace(Data.serverName, playerName, playerUUID.toString(), item.toString(), amount, coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}