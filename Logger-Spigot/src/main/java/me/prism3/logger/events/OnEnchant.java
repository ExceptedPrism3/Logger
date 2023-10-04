package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.FriendlyEnchants;
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
import java.util.List;
import java.util.stream.Collectors;

public class OnEnchant implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchanting(final EnchantItemEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Enchanting")) {

            final Player player = event.getEnchanter();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String playerName = player.getName();
            final World world = player.getWorld();
            final String worldName = world.getName();
            final String item = event.getItem().getType().toString();
            final int cost = event.getExpLevelCost();
            final int x = player.getLocation().getBlockX();
            final int y = player.getLocation().getBlockY();
            final int z = player.getLocation().getBlockZ();

            final List<String> enchs = event.getEnchantsToAdd().entrySet().stream()
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

                    if (!this.main.getMessages().get().getString("Discord.Enchanting-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(player, this.main.getMessages().get().getString("Discord.Enchanting-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replace("%level%", String.valueOf(cost)).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%item%", item).replace("%enchantment%", String.valueOf(enchs)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(this.main.getMessages().get().getString("Files.Enchanting-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", item).replace("%level%", String.valueOf(cost)).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%enchantment%", String.valueOf(enchs)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.enchant(Data.serverName, player, enchs, item, cost, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertEnchant(Data.serverName, player, enchs, item, cost, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getEnchantFile(), true));
                    out.write(this.main.getMessages().get().getString("Files.Enchanting").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%item%", item).replace("%enchantment%", String.valueOf(enchs)).replace("%level%", String.valueOf(cost)) + "\n");
                    out.close();

                } catch (IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Enchanting-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(player, this.main.getMessages().get().getString("Discord.Enchanting-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replace("%level%", String.valueOf(cost)).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%item%", item).replace("%enchantment%", String.valueOf(enchs)), false);

                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Enchanting").isEmpty()) {

                        this.main.getDiscord().enchanting(player, this.main.getMessages().get().getString("Discord.Enchanting").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replace("%level%", String.valueOf(cost)).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%item%", item).replace("%enchantment%", String.valueOf(enchs)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.enchant(Data.serverName, player, enchs, item, cost, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertEnchant(Data.serverName, player, enchs, item, cost, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
