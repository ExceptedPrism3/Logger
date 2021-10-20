/*
package com.carpour.logger.Events.onInventories;

import com.carpour.logger.Main;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class onChest implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onInvClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if(e.getView().getTopInventory().getType().equals(InventoryType.CHEST) ||
                e.getView().getTopInventory().getType().equals(InventoryType.DISPENSER) ||
                e.getView().getTopInventory().getType().equals(InventoryType.DROPPER) ||
                e.getView().getTopInventory().getType().equals(InventoryType.ENCHANTING) ||
                e.getView().getTopInventory().getType().equals(InventoryType.ENDER_CHEST) ||
                e.getView().getTopInventory().getType().equals(InventoryType.HOPPER)) {

            ItemStack item = e.getCurrentItem();

            assert item != null;
            if(item.getType().equals(Material.APPLE)) {

                System.out.println("HADA");

            }
        }
    }
}
*/
