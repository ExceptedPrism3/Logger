package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.logger.utils.enums.UMaterial;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class SpawnEgg implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onEggSpawn(final PlayerInteractEvent event) {

        if (event.isCancelled() || event.getPlayer().hasPermission(Data.loggerExempt)
                || BedrockChecker.isBedrock(event.getPlayer().getUniqueId())) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.hasItem()) {

            if (this.main.getVersion().isAtLeast(NmsVersions.v1_8_R1) && event.getItem().getType().name().contains("MONSTER_EGG")) {

                final SpawnEggMeta mob = (SpawnEggMeta) event.getItem().getItemMeta();
                final EntityType mobType = mob.getSpawnedType();

                this.initializeLog(event.getPlayer(), mobType.toString(), event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ());

            } else if (this.main.getVersion().isAtLeast(NmsVersions.v1_13_R1) && event.getItem().getType().name().contains("SPAWN_EGG")) {

                final UMaterial u = UMaterial.matchSpawnEgg(event.getItem());

                final String entityType = u.name().replace("_SPAWN_EGG", "");

                this.initializeLog(event.getPlayer(), entityType, event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ());

            } else {

                if (event.getItem().getType().name().contains("MONSTER_EGG")) {

                    final UMaterial u = UMaterial.matchSpawnEgg(event.getItem());

                    if (u.name().contains("SPAWN_EGG")) {

                        final String entityType = u.name().replace("_SPAWN_EGG", "");

                        this.initializeLog(event.getPlayer(), entityType, event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ());
                    }
                }
            }
        }
    }

    private void initializeLog(Player player, String entity, int x, int y, int z) {

        final String worldName = player.getWorld().getName();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();

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
                FileHandler.handleFileLog("Files.Spawn-Egg-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Spawn-Egg", placeholders, FileHandler.getSpawnEggFile());
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Spawn-Egg-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Spawn-Egg", placeholders, DiscordChannels.SPAWN_EGG, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueSpawnEgg(Data.serverName, playerUUID.toString(), worldName, playerName, x, y, z, entity, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueSpawnEgg(Data.serverName, playerUUID.toString(), worldName, playerName, x, y, z, entity, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}

