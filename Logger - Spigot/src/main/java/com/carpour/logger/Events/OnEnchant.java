package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Utils.Enum.FriendlyEnchants;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static com.carpour.logger.Utils.Data.*;

public class OnEnchant implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchanting(final EnchantItemEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Enchanting")) {

            final Player player = event.getEnchanter();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final World world = player.getWorld();
            final String worldName = world.getName();
            final String item = event.getItem().getType().toString();
            final int cost = event.getExpLevelCost();
            final int x = player.getLocation().getBlockX();
            final int y = player.getLocation().getBlockY();
            final int z = player.getLocation().getBlockZ();
            final List<String> enchs = new ArrayList<>();
            int enchantmentLevel = 0;

            for (Enchantment ench : event.getEnchantsToAdd().keySet()) {

                enchs.add(FriendlyEnchants.getFriendlyEnchantment(ench).getFriendlyName());

            }
            for (Map.Entry<Enchantment, Integer> list : event.getEnchantsToAdd().entrySet()) {

                enchantmentLevel = list.getValue();

            }

            // Log To Files Handling
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Enchanting-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Enchanting-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", item).replaceAll("%level%", String.valueOf(cost)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%item%", item).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Enchanting-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%item%", item).replaceAll("%level%", String.valueOf(cost)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.enchant(serverName, worldName, playerName, x, y, z, enchs, enchantmentLevel, item, cost, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertEnchant(serverName, player, enchs, enchantmentLevel, item, cost, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getenchantFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Enchanting")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%item%", item).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%level%", String.valueOf(cost)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Enchanting-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Enchanting-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", item).replaceAll("%level%", String.valueOf(cost)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%item%", item).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Enchanting")).isEmpty()) {

                        Discord.enchanting(player, Objects.requireNonNull(Messages.get().getString("Discord.Enchanting")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", item).replaceAll("%level%", String.valueOf(cost)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%item%", item).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)), false);
                    }
                }
            }

            // MySQL
            if (isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.enchant(serverName, worldName, playerName, x, y, z, enchs, enchantmentLevel, item, cost, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertEnchant(serverName, player, enchs, enchantmentLevel, item, cost, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
