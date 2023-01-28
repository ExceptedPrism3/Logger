package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.entity.enums.ArmorStandActionType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class ArmorStandEndCrystalPlace implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorStandEndCrystalPlace(final PlayerInteractEvent event) {

        if (event.isCancelled() || event.getPlayer().hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(event.getPlayer().getUniqueId())
                || event.getItem() == null)
            return;

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.hasItem()
                && (event.getItem().getType().equals(Material.ARMOR_STAND) || event.getItem().getType().equals(Material.END_CRYSTAL))) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String worldName = player.getWorld().getName();
            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final Material entity = event.getItem().getType();

            if (event.getClickedBlock() == null)
                return;

            final int x = event.getClickedBlock().getLocation().getBlockX();
            final int y = event.getClickedBlock().getLocation().getBlockY();
            final int z = event.getClickedBlock().getLocation().getBlockZ();

            final String path;
            final File getter;
            final DiscordChannels discordChannels;

            if (entity.equals(Material.ARMOR_STAND)) {
                path = "ArmorStand-Place";
                getter = FileHandler.getArmorStandPlaceFile();
                discordChannels = DiscordChannels.ARMOR_STAND_PLACE;
            } else {
                path = "EndCrystal-Place";
                getter = FileHandler.getEndCrystalPlaceFile();
                discordChannels = DiscordChannels.END_CRYSTAL_PLACE;
            }

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
                    FileHandler.handleFileLog("Files." + path + "-Staff", placeholders, FileHandler.getStaffFile());
                } else {
                    FileHandler.handleFileLog("Files." + path, placeholders, getter);
                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    this.main.getDiscord().handleDiscordLog("Discord." + path + "-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                } else {

                    this.main.getDiscord().handleDiscordLog("Discord." + path, placeholders, discordChannels, playerName, playerUUID);
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