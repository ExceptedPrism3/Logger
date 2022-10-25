package me.prism3.logger.events.inventories;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnChestInteraction implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpenEvent(final InventoryOpenEvent event) {

        if (event.getInventory().getLocation() == null) return; //TODO FIX

        if (!event.isCancelled()) {

            final Player player = (Player) event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String chestName;

            if (event.getInventory().getType() == InventoryType.CHEST && event.getInventory().getSize() == 54) {

                chestName = "DoubleChest";

            } else if (event.getInventory().getLocation().getBlock().getType() == Material.TRAPPED_CHEST) {

                chestName = "Trapped Chest";

            } else if (event.getInventory().getType() == InventoryType.CHEST) {

                chestName = "Chest";

            } else if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {

                chestName = "EnderChest";

            } else if (Main.getInstance().getVersion().isAtLeast(NmsVersions.v1_11_R1) && event.getInventory().getType() == InventoryType.SHULKER_BOX) {

                chestName = "ShulkerBox";

            } else if (Main.getInstance().getVersion().isAtLeast(NmsVersions.v1_14_R1) && event.getInventory().getType() == InventoryType.BARREL) {

                chestName = "Barrel";

            } else return;

            final UUID playerUUID = player.getUniqueId();
            final String playerName = player.getName();
            final String worldName = player.getWorld().getName();
            final int x = event.getInventory().getLocation().getBlockX();
            final int y = event.getInventory().getLocation().getBlockY();
            final int z = event.getInventory().getLocation().getBlockZ();

            final String[] items = Arrays.stream(event.getInventory().getContents()).filter(Objects::nonNull)
                    .map(stack -> MessageFormat.format("{0} x {1}", stack.getType(), stack.getAmount())).toArray(String[]::new);

            final Coordinates coordinates = new Coordinates(x, y, z, worldName);

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Chest-Interaction-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%chest%", chestName).replace("%items%", Arrays.toString(items)) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChestInteractionFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Chest-Interaction").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%chest%", chestName).replace("%items%", Arrays.toString(items)) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Chest-Interaction-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Chest-Interaction-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%chest%", chestName).replace("%items%", Arrays.toString(items)), false);
                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Chest-Interaction").isEmpty()) {

                        this.main.getDiscord().chestInteraction(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Chest-Interaction").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%chest%", chestName).replace("%items%", Arrays.toString(items)), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getQueueManager().queueChestInteraction(Data.serverName, playerName, playerUUID.toString(), coordinates, items, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getQueueManager().queueChestInteraction(Data.serverName, playerName, playerUUID.toString(), coordinates, items, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}