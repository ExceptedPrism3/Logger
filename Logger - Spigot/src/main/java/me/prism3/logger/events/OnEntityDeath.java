package me.prism3.logger.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnEntityDeath implements Listener {

    /*@EventHandler(priority = EventPriority.HIGHEST)
    public void onMobDeath(EntityDeathEvent event) {

        Player player = (Player) event.getEntity();
        LivingEntity entity = event.getEntity();

        if (entity.getKiller() instanceof Player) {



        }
    }*/
}
