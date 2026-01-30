package me.prism3.logger.utils;

import me.prism3.logger.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpyManager {

    private static final Main main = Main.getInstance();
    private static final File file = new File(main.getDataFolder(), "spy-data.yml");
    private static YamlConfiguration config;

    // Map<UUID, Set<SpyType>>
    // If a spy type is in the set, it means it is DISABLED for that user.
    private static final Map<UUID, Set<String>> disabledSpies = new HashMap<>();

    public static void load() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);

        disabledSpies.clear();
        for (String uuidString : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidString);
                List<String> disabled = config.getStringList(uuidString);
                disabledSpies.put(uuid, new HashSet<>(disabled));
            } catch (IllegalArgumentException e) {
                // Ignore invalid UUIDs
            }
        }
    }

    public static void save() {
        for (Map.Entry<UUID, Set<String>> entry : disabledSpies.entrySet()) {
            config.set(entry.getKey().toString(), new ArrayList<>(entry.getValue()));
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean toggleSpy(Player player, String spyType) {
        UUID uuid = player.getUniqueId();
        Set<String> disabled = disabledSpies.computeIfAbsent(uuid, k -> new HashSet<>());

        spyType = spyType.toLowerCase();

        boolean nowDisabled;
        if (disabled.contains(spyType)) {
            disabled.remove(spyType);
            nowDisabled = false; // It is now ENABLED (removed from disabled list)
        } else {
            disabled.add(spyType);
            nowDisabled = true; // It is now DISABLED (added to disabled list)
        }

        save();
        return !nowDisabled; // Return true if ENABLED, false if DISABLED
    }

    public static boolean isSpyDisabled(Player player, String spyType) {
        Set<String> disabled = disabledSpies.get(player.getUniqueId());
        return disabled != null && disabled.contains(spyType.toLowerCase());
    }
}
