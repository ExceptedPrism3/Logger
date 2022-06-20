package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Enchanting-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Enchanting-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replace("%level%", String.valueOf(cost)).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%item%", item).replace("%enchantment%", String.valueOf(enchs)).replace("%enchlevel%", String.valueOf(enchantmentLevel)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Enchanting-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", item).replace("%level%", String.valueOf(cost)).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%enchantment%", String.valueOf(enchs)).replace("%enchlevel%", String.valueOf(enchantmentLevel)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.enchant(Data.serverName, player, enchs, enchantmentLevel, item, cost, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertEnchant(Data.serverName, player, enchs, enchantmentLevel, item, cost, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getEnchantFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Enchanting")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%item%", item).replace("%enchantment%", String.valueOf(enchs)).replace("%level%", String.valueOf(cost)).replace("%enchlevel%", String.valueOf(enchantmentLevel)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Enchanting-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Enchanting-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replace("%level%", String.valueOf(cost)).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%item%", item).replace("%enchantment%", String.valueOf(enchs)).replace("%enchlevel%", String.valueOf(enchantmentLevel)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Enchanting")).isEmpty()) {

                        this.main.getDiscord().enchanting(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Enchanting")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replace("%level%", String.valueOf(cost)).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%item%", item).replace("%enchantment%", String.valueOf(enchs)).replace("%enchlevel%", String.valueOf(enchantmentLevel)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.enchant(Data.serverName, player, enchs, enchantmentLevel, item, cost, player.hasPermission(Data.loggerStaffLog));

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
