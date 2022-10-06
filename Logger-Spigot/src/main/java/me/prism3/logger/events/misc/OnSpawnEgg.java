package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.logger.utils.enums.UMaterial;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnSpawnEgg implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onEggSpawn(final PlayerInteractEvent event) {

        if (event.getPlayer().hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(event.getPlayer().getUniqueId())) return;

        if (!event.isCancelled() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.hasItem()) {

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

        // Log To Files
        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Spawn-Egg-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%entity%", entity) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getSpawnEggFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Spawn-Egg").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%entity%", entity) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }
        }

        // Discord Integration
        if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("Discord.Spawn-Egg-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Spawn-Egg-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%entity%", entity), false);
                }
            } else {

                if (!this.main.getMessages().get().getString("Discord.Spawn-Egg").isEmpty()) {

                    this.main.getDiscord().spawnEgg(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Spawn-Egg").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%entity%", entity), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertSpawnEgg(Data.serverName, playerUUID.toString(), worldName, playerName, x, y, z, entity, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertSpawnEgg(Data.serverName, playerUUID.toString(), worldName, playerName, x, y, z, entity, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}

