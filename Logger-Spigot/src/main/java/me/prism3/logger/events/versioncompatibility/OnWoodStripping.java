package me.prism3.logger.events.versioncompatibility;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.time.ZonedDateTime;
import java.util.*;

import static me.prism3.logger.utils.Data.*;

public class OnWoodStripping implements Listener {

    private final Main main = Main.getInstance();
    private final List<Material> logs;
    private final List<Material> axes;

    public OnWoodStripping() {

        logs = new ArrayList<>();
        axes = new ArrayList<>();

        logs.add(Material.ACACIA_LOG);
        logs.add(Material.BIRCH_LOG);
        logs.add(Material.DARK_OAK_LOG);
        logs.add(Material.JUNGLE_LOG);
        logs.add(Material.SPRUCE_LOG);
        logs.add(Material.OAK_LOG);
        logs.add(Material.ACACIA_WOOD);
        logs.add(Material.BIRCH_WOOD);
        logs.add(Material.DARK_OAK_WOOD);
        logs.add(Material.JUNGLE_WOOD);
        logs.add(Material.SPRUCE_WOOD);
        logs.add(Material.OAK_WOOD);

        axes.add(Material.WOODEN_AXE);
        axes.add(Material.STONE_AXE);
        axes.add(Material.IRON_AXE);
        axes.add(Material.GOLDEN_AXE);
        axes.add(Material.DIAMOND_AXE);
        if (Main.getInstance().getVersion().isAtLeast(NmsVersions.v1_16_R1))
            axes.add(Material.NETHERITE_AXE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onWoodStripped(final PlayerInteractEvent event) {

        if (!event.isCancelled() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            if (event.getItem() == null || event.getClickedBlock() == null
                    || !logs.contains(event.getClickedBlock().getType()) // If The Block Is NOT A Log, Stop
                    || (!axes.contains(event.getPlayer().getInventory().getItemInMainHand().getType())
                    && !axes.contains(event.getPlayer().getInventory().getItemInOffHand().getType())))
                return;

            final UUID playerUUID = player.getUniqueId();
            final String playerName = player.getName();
            final String worldName = player.getWorld().getName();
            final int x = event.getClickedBlock().getX();
            final int y = event.getClickedBlock().getY();
            final int z = event.getClickedBlock().getZ();
            final String logName = event.getClickedBlock().getType().name();

            final Coordinates coordinates = new Coordinates(x, y, z, worldName);

            final Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
            placeholders.put("%world%", worldName);
            placeholders.put("%uuid%", playerUUID.toString());
            placeholders.put("%player%", playerName);
            placeholders.put("%x%", String.valueOf(x));
            placeholders.put("%y%", String.valueOf(y));
            placeholders.put("%z%", String.valueOf(z));
            placeholders.put("%logname%", logName);

            // Log To Files
            if (Data.isLogToFiles) {
                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                    FileHandler.handleFileLog("Files.Version-Exceptions.Wood-Stripping-Staff", placeholders, FileHandler.getStaffFile());
                } else {
                    FileHandler.handleFileLog("Files.Version-Exceptions.Wood-Stripping", placeholders, FileHandler.getWoodStrippingFile());
                }
            }

            // DiscordManager Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("DiscordManager.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    this.main.getDiscord().handleDiscordLog("DiscordManager.Version-Exceptions.Wood-Stripping-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                } else {

                    this.main.getDiscord().handleDiscordLog("DiscordManager.Version-Exceptions.Wood-Stripping", placeholders, DiscordChannels.WOOD_STRIPPING, playerName, playerUUID);
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueWoodStripping(Data.serverName, playerName, playerUUID.toString(), logName, coordinates, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueWoodStripping(Data.serverName, playerName, playerUUID.toString(), logName, coordinates, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
