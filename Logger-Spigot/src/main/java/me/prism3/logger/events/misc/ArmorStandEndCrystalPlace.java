package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.loggercore.database.entity.enums.ArmorStandActionType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class ArmorStandEndCrystalPlace implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorStandEndCrystalPlace(final PlayerInteractEvent event) {

        if (event.getPlayer().hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(event.getPlayer().getUniqueId()))
            return;

        if (!event.isCancelled() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.hasItem()
                && (event.getItem().getType().equals(Material.ARMOR_STAND) || event.getItem().getType().equals(Material.END_CRYSTAL))) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String worldName = player.getWorld().getName();
            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final Material entity = event.getItem().getType();
            final int x = event.getClickedBlock().getLocation().getBlockX();
            final int y = event.getClickedBlock().getLocation().getBlockY();
            final int z = event.getClickedBlock().getLocation().getBlockZ();

            final String path;
            final File getter;

            if (entity.equals(Material.ARMOR_STAND)) {
                path = "ArmorStand-Place";
                getter = FileHandler.getArmorStandPlaceFile();
            } else {
                path = "EndCrystal-Place";
                getter = FileHandler.getEndCrystalPlaceFile();
            }

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files." + path + "-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(getter, true))) {

                        out.write(this.main.getMessages().get().getString("Files." + path).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord." + path + "-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord." + path + "-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);
                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord." + path).isEmpty()) {

                        if (entity.equals(Material.ARMOR_STAND))
                            this.main.getDiscord().armorStandPlace(playerName, playerUUID, this.main.getMessages().get().getString("Discord." + path).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);
                        else
                            this.main.getDiscord().endCrystalPlace(playerName, playerUUID, this.main.getMessages().get().getString("Discord." + path).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueStandCrystal(Data.serverName, playerName, playerUUID.toString(), worldName, x, y, z, player.hasPermission(loggerStaffLog), ArmorStandActionType.ARMORSTAND_PLACE, event.getItem().getType().name());

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueStandCrystal(Data.serverName, playerName, playerUUID.toString(), worldName, x, y, z, player.hasPermission(loggerStaffLog), ArmorStandActionType.ARMORSTAND_PLACE, event.getItem().getType().name());

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}