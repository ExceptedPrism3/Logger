package me.prism3.logger.Events;

import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Utils.Enums.FriendlyEnchants;
import me.prism3.logger.Utils.FileHandler;
import me.prism3.logger.Database.SQLite.Global.SQLiteData;
import me.prism3.logger.Utils.Messages;
import me.prism3.logger.Utils.Data;
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

public class OnEnchant implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchanting(final EnchantItemEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Enchanting")) {

            final Player player = event.getEnchanter();

            if (player.hasPermission(Data.loggerExempt)) return;

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

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Enchanting-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Enchanting-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", item).replaceAll("%level%", String.valueOf(cost)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%item%", item).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Enchanting-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%item%", item).replaceAll("%level%", String.valueOf(cost)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.enchant(Data.serverName, player, x, y, z, enchs, enchantmentLevel, item, cost, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertEnchant(Data.serverName, player, enchs, enchantmentLevel, item, cost, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getenchantFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Enchanting")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%item%", item).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%level%", String.valueOf(cost)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Enchanting-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Enchanting-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", item).replaceAll("%level%", String.valueOf(cost)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%item%", item).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Enchanting")).isEmpty()) {

                        Discord.enchanting(player, Objects.requireNonNull(Messages.get().getString("Discord.Enchanting")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%item%", item).replaceAll("%level%", String.valueOf(cost)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%item%", item).replaceAll("%enchantment%", String.valueOf(enchs)).replaceAll("%enchlevel%", String.valueOf(enchantmentLevel)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.enchant(Data.serverName, player, x, y, z, enchs, enchantmentLevel, item, cost, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertEnchant(Data.serverName, player, enchs, enchantmentLevel, item, cost, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
