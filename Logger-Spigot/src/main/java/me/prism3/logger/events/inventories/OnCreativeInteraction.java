package me.prism3.logger.events.inventories;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OnCreativeInteraction implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreativeMenu(InventoryInteractEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (player.getGameMode().equals(GameMode.CREATIVE)) {

            ItemStack[] iuv = player.getInventory().getContents();



        }

    }
}
