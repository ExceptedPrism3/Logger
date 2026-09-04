package me.prism3.logger.events.player;

import me.prism3.logger.LoggerAPI;
import me.prism3.logger.managers.PermissionManager;
import me.prism3.logger.managers.PlayerManager;
import me.prism3.logger.utils.VersionUtil;
import me.prism3.logger.utils.enums.LogType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class listens for spawn egg usage and spawns.
 * It logs the player who used the egg and the type of mob spawned.
 */
public class SpawnEggListener implements Listener {

    private final LoggerAPI plugin;
    private final boolean isLegacy = VersionUtil.CURRENT.isLegacy();
    private static final Method AS_NMS_COPY_METHOD;

    // TTL for pending spawns
    private static final long EXPIRY_MILLIS = 1_000;

    // correlate player → most recent “used spawn-egg”
    private final Map<UUID, PendingSpawn> pendingSpawns = new ConcurrentHashMap<>();

    // static initializer to find the NMS method for legacy versions
    static {
        // figure out “v1_12_R1” from org.bukkit.craftbukkit.vX_Y_RZ
        final String pkg = Bukkit.getServer().getClass().getPackage().getName();
        final String suffix = pkg.substring(pkg.lastIndexOf('.') + 1);

        Class<?> cls;
        Method method = null;
        // if we are on a legacy version, try to find the NMS method
        if (VersionUtil.CURRENT.isLegacy()) {
            try {
                cls = Class.forName("org.bukkit.craftbukkit." + suffix + ".inventory.CraftItemStack");
                method = cls.getMethod("asNMSCopy", ItemStack.class);
            } catch (final Exception e) {
                // if we fail here, legacy NBT lookup will simply not work
                e.printStackTrace();
            }
        }
        AS_NMS_COPY_METHOD = method;
    }

    /**
     * Constructor for the SpawnEggListener.
     * It initializes the plugin and sets up a task to clean up stale pending
     * entries.
     *
     * @param plugin The LoggerAPI plugin instance.
     */
    public SpawnEggListener(final LoggerAPI plugin) {
        this.plugin = plugin;

        // Cleanup stale pending entries every 30s
        me.prism3.logger.utils.SchedulerAdapter.runAsyncTimer(plugin, () -> {
            final long now = System.currentTimeMillis();
            this.pendingSpawns.values().removeIf(p -> now - p.timestamp > EXPIRY_MILLIS * 5);
        }, 600L, 600L);
    }

    /**
     * This event is fired when a player uses a spawn egg.
     * We use this to log the player who used the egg and the type of mob spawned.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerUseEgg(final PlayerInteractEvent event) {

        if (event.isCancelled())
            return;

        // Check if the player is using a spawn egg
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        final Player p = event.getPlayer();

        if (PermissionManager.isExempt(p))
            return;

        final ItemStack item = event.getItem();

        if (item == null)
            return;

        // Check if the item is a spawn egg
        this.getMobType(item).ifPresent(mob -> {
            this.pendingSpawns.put(p.getUniqueId(), new PendingSpawn(mob));
        });
    }

    /**
     * This event is fired when a creature is spawned from a spawn egg.
     * We use this to log the player who used the egg and the type of mob spawned.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEggHatch(final CreatureSpawnEvent event) {

        if (event.isCancelled())
            return;

        // Check if the creature was spawned from a spawn egg
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG)
            return;

        final Location loc = event.getLocation();
        final long now = System.currentTimeMillis();

        // Check if the player is using a spawn egg
        for (Iterator<Map.Entry<UUID, PendingSpawn>> it = this.pendingSpawns.entrySet().iterator(); it.hasNext();) {

            final Map.Entry<UUID, PendingSpawn> entry = it.next();
            final PendingSpawn ps = entry.getValue();

            // Check if the pending spawn is expired
            if (now - ps.timestamp > EXPIRY_MILLIS) {
                it.remove();
                continue;
            }

            final Player p = Bukkit.getPlayer(entry.getKey());
            if (p == null) {
                it.remove();
                continue;
            }

            final Map<String, String> placeholders = PlayerManager.getPlayerManager(p).populatePlaceholders(p);
            placeholders.put("mob_type", ps.mobType);
            placeholders.put("mob_x", String.valueOf(loc.getBlockX()));
            placeholders.put("mob_y", String.valueOf(loc.getBlockY()));
            placeholders.put("mob_z", String.valueOf(loc.getBlockZ()));

            this.plugin.getLoggerManager().logEvent(LogType.PLAYER_PLAYER_SPAWN_EGG, p, placeholders);

            it.remove();
            break;
        }
    }

    /**
     * Get the mob type from the item stack.
     * This method is used to get the mob type from the spawn egg.
     *
     * @param item The item stack to get the mob type from.
     * @return The mob type as a string.
     */
    private Optional<String> getMobType(final ItemStack item) {
        return isLegacy ? this.getLegacy(this.typeSafe(item)) : this.getModern(item);
    }

    /**
     * This method is used to convert the item stack to a type safe item stack.
     * This is used to avoid reflection issues with NMS.
     *
     * @param item The item stack to convert.
     * @return The type safe item stack.
     */
    private ItemStack typeSafe(final ItemStack item) {
        return item;
    }

    /**
     * This method is used to get the legacy mob type from the item stack.
     * This is used to get the mob type from the spawn egg.
     *
     * @param item The item stack to get the mob type from.
     * @return The mob type as a string.
     */
    private Optional<String> getLegacy(final ItemStack item) {

        // Check if the item is a spawn egg
        if (!item.getType().name().equals("MONSTER_EGG"))
            return Optional.empty();

        // Check if the item is a spawn egg
        try {
            // NMS-based lookup
            final Object nms = AS_NMS_COPY_METHOD.invoke(null, item);
            final Object tag = nms.getClass().getMethod("getTag").invoke(nms);
            final Object ent = tag.getClass().getMethod("getCompound", String.class).invoke(tag, "EntityTag");
            final String id = (String) ent.getClass().getMethod("getString", String.class).invoke(ent, "id");

            // Check if the id is not null or empty
            if (id != null && !id.isEmpty()) {
                return Optional.of(id.replace("minecraft:", "").toUpperCase());
            }
        } catch (final Exception ignored) {
        }

        return Optional.empty();
    }

    /**
     * This method is used to get the modern mob type from the item stack.
     * This is used to get the mob type from the spawn egg.
     *
     * @param item The item stack to get the mob type from.
     * @return The mob type as a string.
     */
    private Optional<String> getModern(final ItemStack item) {

        final String nm = item.getType().name();

        // Check if the item is a spawn egg
        if (nm.endsWith("_SPAWN_EGG")) {
            return Optional.of(nm.replace("_SPAWN_EGG", ""));
        }

        return Optional.empty();
    }

    /**
     * This class is used to store the pending spawn.
     * It stores the mob type and the timestamp of when the spawn was created.
     */
    private static class PendingSpawn {

        final String mobType;
        final long timestamp;

        /**
         * Constructor for the PendingSpawn class.
         * It initializes the mob type and the timestamp of when the spawn was created.
         *
         * @param mobType The mob type as a string.
         */
        PendingSpawn(final String mobType) {
            this.mobType = mobType;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
