/*
package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.NmsVersions;
import net.ess3.nms.refl.ReflUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerEggSpawnEvents implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onEggSpawn(PlayerInteractEvent e) {

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.hasItem()
                && main.getVersion().isAtLeast(NmsVersions.v1_8_R1)
                && e.getItem().getType().name().contains("MONSTER_EGG")) {

                        EntityType entityType = EntityType.fromId(e.getItem().getDurability());

                        ServerLogEvent logEvent = new ServerLogEvent(
                                methods.getConfigFile().getString("spawn-egg")
                                        .replace("[time]: ", "")
                                        .replace("[player]", e.getPlayer().getName())
                                        .replace("[entity]", entityType.name())
                                        .replace("[x]", "" + e.getClickedBlock().getLocation().getBlockX())
                                        .replace("[y]", "" + e.getClickedBlock().getLocation().getBlockY())
                                        .replace("[z]", "" + e.getClickedBlock().getLocation().getBlockZ()),
                                methods.getTime(),
                                methods.getDate(),
                                "plugins/ServerLog/Players/Spawn Mob Egg/",
                                "PlayerInteractEvent");
                        Bukkit.getPluginManager().callEvent(logEvent);

                        methods.appendString("/Players/Spawn Mob Egg/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType.name()).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
                        methods.appendString("/Compiled Log/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType.name()).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
                    }
                } else if (Bukkit.getServer().getBukkitVersion().contains("1.13")) {

                    if (e.getItem().getType().name().contains("SPAWN_EGG")) {

                        final UMaterial u = UMaterial.matchSpawnEgg(e.getItem());

                        String entityType = u.name().replace("_SPAWN_EGG", "");

                        ServerLogEvent logEvent = new ServerLogEvent(
                                methods.getConfigFile().getString("spawn-egg")
                                        .replace("[time]: ", "")
                                        .replace("[player]", e.getPlayer().getName())
                                        .replace("[entity]", entityType)
                                        .replace("[x]", "" + e.getClickedBlock().getLocation().getBlockX())
                                        .replace("[y]", "" + e.getClickedBlock().getLocation().getBlockY())
                                        .replace("[z]", "" + e.getClickedBlock().getLocation().getBlockZ()),
                                methods.getTime(),
                                methods.getDate(),
                                "plugins/ServerLog/Players/Spawn Mob Egg/",
                                "PlayerInteractEvent");
                        Bukkit.getPluginManager().callEvent(logEvent);

                        methods.appendString("/Players/Spawn Mob Egg/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
                        methods.appendString("/Compiled Log/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
                    }
                } else {
                    if (e.getItem().getType().name().contains("MONSTER_EGG")) {
                        final UMaterial u = UMaterial.matchSpawnEgg(e.getItem());
                        if (u.name().contains("SPAWN_EGG")) {
                            String entityType = u.name().replace("_SPAWN_EGG", "");
                            ServerLogEvent logEvent = new ServerLogEvent(
                                    methods.getConfigFile().getString("spawn-egg")
                                            .replace("[time]: ", "")
                                            .replace("[player]", e.getPlayer().getName())
                                            .replace("[entity]", entityType)
                                            .replace("[x]", "" + e.getClickedBlock().getLocation().getBlockX())
                                            .replace("[y]", "" + e.getClickedBlock().getLocation().getBlockY())
                                            .replace("[z]", "" + e.getClickedBlock().getLocation().getBlockZ()),
                                    methods.getTime(),
                                    methods.getDate(),
                                    "plugins/ServerLog/Players/Spawn Mob Egg/",
                                    "PlayerInteractEvent");
                            Bukkit.getPluginManager().callEvent(logEvent);

                            methods.appendString("/Players/Spawn Mob Egg/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
                            methods.appendString("/Compiled Log/", methods.getConfigFile().getString("spawn-egg").replace("[player]", e.getPlayer().getName()).replace("[entity]", entityType).replace("[x]", "" + e.getClickedBlock().getX()).replace("[y]", "" + e.getClickedBlock().getY()).replace("[z]", "" + e.getClickedBlock().getZ()));
                        }
                    }

            }

    }

}*/
