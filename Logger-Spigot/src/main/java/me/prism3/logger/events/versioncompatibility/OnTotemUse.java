package me.prism3.logger.events.versioncompatibility;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class OnTotemUse implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTotem(final EntityResurrectEvent event) {

        if (event.getEntity() instanceof Player) {

            final Player player = (Player) event.getEntity();
            final String playerName = player.getName();
            final String worldName = player.getWorld().getName();
            final int x = player.getLocation().getBlockX();
            final int y = player.getLocation().getBlockY();
            final int z = player.getLocation().getBlockZ();


            System.out.println(playerName + " " + worldName + " " + x + " " +  y + " " + z);
        }
    }
}
