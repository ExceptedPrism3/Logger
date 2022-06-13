package me.prism3.logger.events.misc;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class OnSpawnEgg implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(final PlayerInteractEvent event) {

        /*if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Objects.requireNonNull(event.getItem()).getType().name().contains("MONSTER_EGG")) {

            Player player = event.getPlayer();
            String playerName = player.getName();

            System.out.println(playerName + event.getItem().getType().name());

        }*/
    }
}
