package com.carpour.logger.Events.OnInventories;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class OnCraft implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrafting(final CraftItemEvent event){

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Craft")) {

            final Player player = (Player) event.getWhoClicked();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final World world = player.getWorld();
            final String worldName = world.getName();
            final String item = Objects.requireNonNull(event.getCurrentItem()).getType().name().replace("_", " ");
            final int amount = event.getCurrentItem().getAmount();
            final int x = player.getLocation().getBlockX();
            final int y = player.getLocation().getBlockY();
            final int z = player.getLocation().getBlockZ();

            // Log To Files Handling
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft-Staff")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Craft-Staff")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.playerCraft(serverName, worldName, playerName, item, amount, x, y, z, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerCraft(serverName, player, item, amount, x, y, z, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCraftFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Craft")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft-Staff")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft")).isEmpty()) {

                        Discord.playerCraft(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);
                    }
                }
            }

            // MySQL Handling
            if (isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.playerCraft(serverName, worldName, playerName, item, amount, x, y, z, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite Handling
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerCraft(serverName, player, item, amount, x, y, z, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
