package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.enums.FriendlyEnchants;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnItemDrop implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(final PlayerDropItemEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Item-Drop")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

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

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Drop-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Drop-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)).replace("%enchantment%", String.valueOf(enchs)), false);
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Item-Drop-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", item).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)).replace("%enchantment%", String.valueOf(enchs)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.itemDrop(Data.serverName, player, item, amount, blockX, blockY, blockZ, enchs, itemName, true);

                    }

                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertItemDrop(Data.serverName, player, item, blockX, amount, blockY, blockZ, enchs, itemName, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getItemDropFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Item-Drop")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", item).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)).replace("%enchantment%", String.valueOf(enchs)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Drop-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Drop-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)).replace("%enchantment%", String.valueOf(enchs)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Drop")).isEmpty()) {

                        this.main.getDiscord().itemDrop(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Item-Drop")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replace("%amount%", String.valueOf(amount)).replace("%x%", String.valueOf(blockX)).replace("%y%", String.valueOf(blockY)).replace("%z%", String.valueOf(blockZ)).replace("%renamed%", String.valueOf(itemName)).replace("%enchantment%", String.valueOf(enchs)), false);
                    }
                }
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.itemDrop(Data.serverName, player, item, amount, blockX, blockY, blockZ, enchs, itemName, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertItemDrop(Data.serverName, player, item, amount, blockX, blockY, blockZ, enchs, itemName, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
