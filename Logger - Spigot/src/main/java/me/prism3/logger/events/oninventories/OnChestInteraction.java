package me.prism3.logger.events.oninventories;

import me.prism3.logger.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;

public class OnChestInteraction implements Listener {
/*
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpenEvent(final InventoryOpenEvent event) {

        String chestName;

        if (event.getInventory().getType() == InventoryType.CHEST && event.getInventory().getSize() == 54) {

            chestName = "DoubleChest";

        } else if (event.getInventory().getLocation().getBlock().getType() == Material.TRAPPED_CHEST) {

            chestName = "Trapped Chest";

        } else if (event.getInventory().getType() == InventoryType.CHEST) {

            chestName = "Chest";

        } else if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {

            chestName = "EnderChest";

        } else if (Main.getInstance().getVersion().isAtLeast(NmsVersions.v1_11_R1) && event.getInventory().getType() == InventoryType.SHULKER_BOX) {

            chestName = "ShulkerBox";

        } else if (Main.getInstance().getVersion().isAtLeast(NmsVersions.v1_14_R1) && event.getInventory().getType() == InventoryType.BARREL) {

            chestName = "Barrel";

        } else return;

        final Player player = (Player) event.getPlayer();
        final String playerName = player.getName();
        final String worldName = player.getWorld().getName();
        final int x = event.getInventory().getLocation().getBlockX();
        final int y = event.getInventory().getLocation().getBlockY();
        final int z = event.getInventory().getLocation().getBlockZ();

        String[] strings = Arrays.stream(event.getInventory().getContents()).filter(Objects::nonNull).map(stack -> MessageFormat.format("{0} x {1}", stack.getType(), stack.getAmount())).toArray(String[]::new);

        System.out.println(playerName + " " + worldName + " " + chestName + " " + x + " " + y + " " + z + " " + Arrays.toString(strings));

    }*/
}