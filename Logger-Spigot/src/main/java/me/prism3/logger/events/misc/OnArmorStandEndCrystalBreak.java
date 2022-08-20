package me.prism3.logger.events.misc;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerExempt;

public class OnArmorStandEndCrystalBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        final Entity damager = event.getDamager();

        if ((event.getEntity() instanceof ArmorStand || event.getEntity() instanceof EnderCrystal)
                && (damager != null && damager instanceof Player)) {

            final Player player = (Player) damager;

            if (player.hasPermission(loggerExempt)) return;

            final String worldName = player.getWorld().getName();
            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final Entity entity = event.getEntity();
            final String entityName = entity.getName();
            final int x = entity.getLocation().getBlockX();
            final int y = entity.getLocation().getBlockY();
            final int z = entity.getLocation().getBlockZ();

            System.out.println(worldName + " " + playerUUID + " " + playerName + " " + entityName + " " + x + " " + y + " " + z);

        }
    }
}
