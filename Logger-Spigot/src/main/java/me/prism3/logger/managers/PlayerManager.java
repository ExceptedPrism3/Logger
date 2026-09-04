package me.prism3.logger.managers;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

/**
 * Improved PlayerManager:
 *  - Keys by UUID in a ConcurrentHashMap to avoid memory leaks
 *  - Populates dynamic data (location, world) on demand
 */
public class PlayerManager {

    private static final Map<UUID, PlayerManager> cache = new ConcurrentHashMap<>();

    private final UUID uuid;
    private final String name;

    private PlayerManager(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public static PlayerManager getPlayerManager(Player player) {
        return cache.computeIfAbsent(
                player.getUniqueId(),
                id -> new PlayerManager(player)
        );
    }

    public static void removePlayerManager(Player player) {
        cache.remove(player.getUniqueId());
    }

    /**
     * Returns a fresh map of placeholders using the player's current state.
     */
    public Map<String, String> populatePlaceholders(Player player) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", name);
        placeholders.put("uuid", uuid.toString());
        placeholders.put("world", player.getWorld().getName());
        placeholders.put("x", String.valueOf(player.getLocation().getBlockX()));
        placeholders.put("y", String.valueOf(player.getLocation().getBlockY()));
        placeholders.put("z", String.valueOf(player.getLocation().getBlockZ()));
        return placeholders;
    }

    public String getPlayerUUID() { return uuid.toString(); }
    public String getPlayerName() { return name; }
}