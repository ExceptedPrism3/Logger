package me.prism3.logger.events.misc;

import me.prism3.logger.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class ArmorStandPlace implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorStandPlace(CreatureSpawnEvent event) {

        if (event.getEntity().getType().equals(EntityType.ARMOR_STAND)) {

//            event.get

            System.out.println(event.getSpawnReason());

            System.out.println(event.getLocation().getBlockX() + " " + event.getLocation().getBlockY() + " " + event.getLocation().getBlockZ());
        }
    }
}
