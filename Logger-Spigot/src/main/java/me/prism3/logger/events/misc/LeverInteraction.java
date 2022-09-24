package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class LeverInteraction implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLever(final PlayerInteractEvent event) {

        if (!event.isCancelled() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock().getType().equals(Material.LEVER)) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final UUID playerUUID = player.getUniqueId();
            final String worldName = player.getWorld().getName();
            final String playerName = player.getName();
            final int x = event.getClickedBlock().getX();
            final int y = event.getClickedBlock().getY();
            final int z = event.getClickedBlock().getZ();

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Lever-Interaction-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeverInteractionFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Lever-Interaction").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Lever-Interaction-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Lever-Interaction-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);
                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Lever-Interaction").isEmpty()) {

                        this.main.getDiscord().leverInteraction(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Lever-Interaction").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertLeverInteraction(Data.serverName, playerUUID.toString(), worldName, playerName, x, y, z, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertLeverInteraction(Data.serverName, playerUUID.toString(), worldName, playerName, x, y, z, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
