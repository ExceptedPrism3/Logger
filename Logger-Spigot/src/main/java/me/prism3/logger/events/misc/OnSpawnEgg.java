package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.NmsVersions;
import me.prism3.logger.utils.enums.UMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.SpawnEgg;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerExempt;

public class OnSpawnEgg implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onEggSpawn(final PlayerInteractEvent event) {

        if (!event.isCancelled() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.hasItem()) {

            if (this.main.getVersion().isAtLeast(NmsVersions.v1_8_R1) && event.getItem().getType().name().contains("MONSTER_EGG")) {

                    final Player player = event.getPlayer();

                    if (player.hasPermission(loggerExempt)) return;

                    final String worldName = player.getWorld().getName();
                    final UUID playerUUID = player.getUniqueId();
                    final String playerName = player.getName();
                    final SpawnEggMeta mob = (SpawnEggMeta) event.getItem().getItemMeta();
                    final EntityType mobName = mob.getSpawnedType();
                    final int x = event.getClickedBlock().getLocation().getBlockX();
                    final int y = event.getClickedBlock().getLocation().getBlockY();
                    final int z = event.getClickedBlock().getLocation().getBlockZ();

                    System.out.println(worldName + " " + playerUUID + " " + playerName + " " + mobName + " " + x + " " + y + " " + z);

            } else if (this.main.getVersion().isAtLeast(NmsVersions.v1_13_R1) && event.getItem().getType().name().contains("SPAWN_EGG")) {

                final UMaterial u = UMaterial.matchSpawnEgg(event.getItem());

                String entityType = u.name().replace("_SPAWN_EGG", "");

                System.out.println(entityType);

            } else {

                if (event.getItem().getType().name().contains("MONSTER_EGG")) {

                    final UMaterial u = UMaterial.matchSpawnEgg(event.getItem());

                    if (u.name().contains("SPAWN_EGG")) {

                        final String entityType = u.name().replace("_SPAWN_EGG", "");

                        System.out.println("3 " + entityType);
                    }
                }
            }
        }
    }
}

