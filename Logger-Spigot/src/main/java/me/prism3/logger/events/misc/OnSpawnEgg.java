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

            if (this.main.getVersion().isAtLeast(NmsVersions.v1_8_R1)) {

                if (event.getItem().getType().name().contains("MONSTER_EGG")) {

                    final Player player = event.getPlayer();

                    if (player.hasPermission(loggerExempt)) return;

                    final String worldName = player.getWorld().getName();
                    final UUID playerUUID = player.getUniqueId();
                    final String playerName = player.getName();
                    final EntityType entityType = EntityType.fromId(event.getItem().getDurability());

                    final int x = event.getClickedBlock().getLocation().getBlockX();
                    final int y = event.getClickedBlock().getLocation().getBlockY();
                    final int z = event.getClickedBlock().getLocation().getBlockZ();

//                    System.out.println(worldName + " " + playerUUID + " " + playerName + " " + entityType.name() + " " + x + " " + y + " " + z);

                }

            } else if (this.main.getVersion().isAtLeast(NmsVersions.v1_13_R1)) {

                if (event.getItem().getType().name().contains("SPAWN_EGG")) {

                    final UMaterial u = UMaterial.matchSpawnEgg(event.getItem());

                    final String entityType = u.name().replace("_SPAWN_EGG", "");

                    System.out.println("2");

//                    ServerLogEvent logEvent = new ServerLogEvent(
//                            methods.getConfigFile().getString("spawn-egg")
//                                    .replace("[time]: ", "")
//                                    .replace("[player]", e.getPlayer().getName())
//                                    .replace("[entity]", entityType)
//                                    .replace("[x]", "" + e.getClickedBlock().getLocation().getBlockX())
//                                    .replace("[y]", "" + e.getClickedBlock().getLocation().getBlockY())
//                                    .replace("[z]", "" + e.getClickedBlock().getLocation().getBlockZ()),
//                            methods.getTime(),
//                            methods.getDate(),
//                            "plugins/ServerLog/Players/Spawn Mob Egg/",
//                            "PlayerInteractEvent");
//                    Bukkit.getPluginManager().callEvent(logEvent);
//
//                    methods.appendString("/Players/Spawn Mob Egg/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
//                    methods.appendString("/Compiled Log/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
                }
            } else {

                if (event.getItem().getType().name().contains("MONSTER_EGG")) {

                    final UMaterial u = UMaterial.matchSpawnEgg(event.getItem());

                    if (u.name().contains("SPAWN_EGG")) {

                        final String entityType = u.name().replace("_SPAWN_EGG", "");

                        System.out.println("3");

//                            ServerLogEvent logEvent = new ServerLogEvent(
//                                    methods.getConfigFile().getString("spawn-egg")
//                                            .replace("[time]: ", "")
//                                            .replace("[player]", e.getPlayer().getName())
//                                            .replace("[entity]", entityType)
//                                            .replace("[x]", "" + e.getClickedBlock().getLocation().getBlockX())
//                                            .replace("[y]", "" + e.getClickedBlock().getLocation().getBlockY())
//                                            .replace("[z]", "" + e.getClickedBlock().getLocation().getBlockZ()),
//                                    methods.getTime(),
//                                    methods.getDate(),
//                                    "plugins/ServerLog/Players/Spawn Mob Egg/",
//                                    "PlayerInteractEvent");
//                            Bukkit.getPluginManager().callEvent(logEvent);
//
//                            methods.appendString("/Players/Spawn Mob Egg/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
//                            methods.appendString("/Compiled Log/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
                    }
                }
            }
        }
    }
}

