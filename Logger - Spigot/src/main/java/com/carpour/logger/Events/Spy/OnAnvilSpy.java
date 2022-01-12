package com.carpour.logger.Events.Spy;

import com.carpour.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class OnAnvilSpy implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilSpy(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();

        if (player.hasPermission("logger.exempt") || player.hasPermission("logger.spy")) return;

        if (main.getConfig().getBoolean("Log-Player.Anvil") && main.getConfig().getBoolean("Spy-Features.Anvil-Spy.Enable")) {

            if (inv instanceof AnvilInventory) {

                InventoryView view = event.getView();

                int rawSlot = event.getRawSlot();

                if (rawSlot == view.convertSlot(rawSlot)) {

                    if (rawSlot == 2) {

                        ItemStack item = event.getCurrentItem();

                        if (item != null) {

                            ItemMeta meta = item.getItemMeta();

                            if (meta != null) {

                                if (meta.hasDisplayName()) {

                                    String displayName = meta.getDisplayName();

                                    for (Player players : Bukkit.getOnlinePlayers()) {

                                        if (players.hasPermission("logger.spy")) {

                                            players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                    Objects.requireNonNull(main.getConfig().getString("Spy-Features.Anvil-Spy.Message")).
                                                            replace("%player%", player.getName()).
                                                            replace("%renamed%", displayName)));

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
