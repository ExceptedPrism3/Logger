package me.prism3.logger.events.misc;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnSpawnEgg implements Listener {

//    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(final PlayerInteractEvent event) {

        /*if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Objects.requireNonNull(event.getItem()).getType().name().contains("MONSTER_EGG")) {

            Player player = event.getPlayer();
            String playerName = player.getName();

            System.out.println(playerName + event.getItem().getType().name());

        }*/
    }
}
