package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Logs detailed player-container interaction information: original and modified
 * inventory contents.
 */
public class ContainerInteractionListener implements Listener {

    private final LoggerAPI plugin;
    private final Map<Player, Map<String, Integer>> snapshots = new HashMap<>();

    public ContainerInteractionListener(final LoggerAPI plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles inventory open events.
     * Takes a snapshot of the inventory contents when a player opens a container.
     *
     * @param event the InventoryOpenEvent to handle
     */
    @EventHandler
    public void onContainerOpen(final InventoryOpenEvent event) {

        if (!(event.getPlayer() instanceof Player))
            return;
        final Player player = (Player) event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        // Check if the inventory is a container
        this.snapshots.put(player, snapshot(event.getInventory()));
    }

    /**
     * Handles inventory close events.
     * Compares the original and modified inventory contents and logs the changes.
     *
     * @param event the InventoryCloseEvent to handle
     */
    @EventHandler
    public void onContainerClose(final InventoryCloseEvent event) {

        if (!(event.getPlayer() instanceof Player))
            return;
        final Player player = (Player) event.getPlayer();

        if (PermissionManager.isExempt(player))
            return;

        final Map<String, Integer> before = this.snapshots.remove(player);

        if (before == null)
            return;

        final Inventory inv = event.getInventory();
        final Map<String, Integer> after = this.snapshot(inv);

        final String original = this.formatSnapshot(before);
        final String modified = this.formatSnapshot(after);

        // Check for added items
        final String added = after.entrySet().stream()
                .filter(e -> e.getValue() > before.getOrDefault(e.getKey(), 0))
                .map(e -> (e.getValue() - before.getOrDefault(e.getKey(), 0)) + "× " + e.getKey())
                .collect(Collectors.joining(", "));

        // Check for removed items
        final String removed = before.entrySet().stream()
                .filter(e -> e.getValue() > after.getOrDefault(e.getKey(), 0))
                .map(e -> (e.getValue() - after.getOrDefault(e.getKey(), 0)) + "× " + e.getKey())
                .collect(Collectors.joining(", "));

        // If no changes, return back to base Sergeant
        if (added.isEmpty() && removed.isEmpty())
            return;

        final InventoryHolder holder = inv.getHolder();
        final String type = inv.getType().name();
        final Location loc = (holder instanceof BlockState) ? ((BlockState) holder).getLocation()
                : player.getLocation();

        final Map<String, String> ph = PlayerManager.getPlayerManager(player).populatePlaceholders(player);

        ph.put("container", type);
        ph.put("container_x", String.valueOf(loc.getBlockX()));
        ph.put("container_y", String.valueOf(loc.getBlockY()));
        ph.put("container_z", String.valueOf(loc.getBlockZ()));
        ph.put("original", original);
        ph.put("modified", modified);
        ph.put("added_items", added.isEmpty() ? "none" : added);
        ph.put("removed_items", removed.isEmpty() ? "none" : removed);

        this.plugin.getLoggerManager().logEvent(LogType.PLAYER_CONTAINER_INTERACTION, player, ph);
    }

    private Map<String, Integer> snapshot(final Inventory inv) {

        final Map<String, Integer> map = new HashMap<>();

        for (final ItemStack item : inv.getContents()) {

            if (item == null || item.getType() == Material.AIR)
                continue;

            final String key = item.getType().name();

            map.put(key, map.getOrDefault(key, 0) + item.getAmount());
        }
        return map;
    }

    private String formatSnapshot(final Map<String, Integer> snap) {

        if (snap.isEmpty())
            return "none";

        return snap.entrySet().stream()
                .map(e -> e.getValue() + "× " + e.getKey())
                .collect(Collectors.joining(", "));
    }
}
