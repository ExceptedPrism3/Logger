package me.prism3.logger.events.versioncompatibility;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

    private static final EnumSet<Material> logs = EnumSet.of(
            Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG, Material.SPRUCE_LOG, Material.OAK_LOG,
            Material.ACACIA_WOOD, Material.BIRCH_WOOD, Material.DARK_OAK_WOOD,
            Material.JUNGLE_WOOD, Material.SPRUCE_WOOD, Material.OAK_WOOD
    );

    private static final EnumSet<Material> axes = EnumSet.of(
            Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE,
            Material.GOLDEN_AXE, Material.DIAMOND_AXE
    );

    static {
        if (version.isAtLeast(NmsVersions.v1_16_R1))
            axes.add(Material.NETHERITE_AXE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onWoodStripped(final PlayerInteractEvent event) {

        if (event.isCancelled() || event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                event.getPlayer().hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(event.getPlayer().getUniqueId()) ||
                event.getItem() == null || event.getClickedBlock() == null || !logs.contains(event.getClickedBlock().getType()) ||
                (!axes.contains(event.getPlayer().getInventory().getItemInMainHand().getType()) &&
                        !axes.contains(event.getPlayer().getInventory().getItemInOffHand().getType())))
            return;

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId()))
            return;

        final Block clickedBlock = event.getClickedBlock();

        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final String worldName = player.getWorld().getName();
        final int x = clickedBlock.getX();
        final int y = clickedBlock.getY();
        final int z = clickedBlock.getZ();
        final String logName = clickedBlock.getType().name();

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

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {
            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                this.main.getDiscord().handleDiscordLog("Discord.Version-Exceptions.Wood-Stripping-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {
                this.main.getDiscord().handleDiscordLog("Discord.Version-Exceptions.Wood-Stripping", placeholders, DiscordChannels.WOOD_STRIPPING, playerName, playerUUID);
            }
        }

        // External and SQLite Handling
        if (Data.isExternal || Data.isSqlite)
            this.handleWoodStripping(playerName, playerUUID.toString(), logName, coordinates, player.hasPermission(loggerStaffLog));
    }

    private void handleWoodStripping(String playerName, String playerUUID, String logName, Coordinates coordinates, boolean isStaff) {

        this.main.getDatabase().getDatabaseQueue().queueWoodStripping(Data.serverName, playerName, playerUUID, logName, coordinates, isStaff);
    }
}
