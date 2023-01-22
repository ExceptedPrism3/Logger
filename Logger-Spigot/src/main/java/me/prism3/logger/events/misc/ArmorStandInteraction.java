package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import java.time.ZonedDateTime;
import java.util.*;

import static me.prism3.logger.utils.Data.*;

public class ArmorStandInteraction implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorStandInteraction(final PlayerArmorStandManipulateEvent event) {

        if (event.isCancelled() || event.getArmorStandItem().equals(event.getPlayerItem()) // If the armor stand already has the item then no need to log it to prevent useless logs
                || event.getPlayer().hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(event.getPlayer().getUniqueId()))
            return;

        final Player player = event.getPlayer();

        final String worldName = player.getWorld().getName();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final int x = event.getRightClicked().getLocation().getBlockX();
        final int y = event.getRightClicked().getLocation().getBlockY();
        final int z = event.getRightClicked().getLocation().getBlockZ();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%x%", String.valueOf(x));
        placeholders.put("%y%", String.valueOf(y));
        placeholders.put("%z%", String.valueOf(z));

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.ArmorStand-Interaction-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.ArmorStand-Interaction", placeholders, FileHandler.getArmorStandInteractionFile());
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.ArmorStand-Interaction-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.ArmorStand-Interaction", placeholders, DiscordChannels.ARMOR_STAND_INTERACTION, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueArmorStandInteraction(Data.serverName, playerName, playerUUID.toString(), worldName, x, y, z, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueArmorStandInteraction(Data.serverName, playerName, playerUUID.toString(), worldName, x, y, z, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
