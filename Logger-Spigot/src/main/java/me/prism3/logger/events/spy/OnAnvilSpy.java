package me.prism3.logger.events.spy;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
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

public class OnAnvilSpy implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilSpy(final InventoryClickEvent event) {

        final Player player = (Player) event.getWhoClicked();

        if (player.hasPermission(Data.loggerExempt) || player.hasPermission(Data.loggerSpyBypass)) return;

        final Inventory inv = event.getInventory();

        if (inv instanceof AnvilInventory) {

            final InventoryView view = event.getView();

            final int rawSlot = event.getRawSlot();

            if (rawSlot == view.convertSlot(rawSlot) && (rawSlot == 2)) {

                final ItemStack item = event.getCurrentItem();

                if (item != null) {

                    final ItemMeta meta = item.getItemMeta();

                    if (meta != null && meta.hasDisplayName()) {

                        final String displayName = meta.getDisplayName().replace("\\", "\\\\");

                        for (Player players : Bukkit.getOnlinePlayers()) {

                            if (players.hasPermission(Data.loggerSpy)) {

                                players.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                                this.main.getConfig().getString("Spy-Features.Anvil-Spy.Message")).
                                        replace("%player%", player.getName()).
                                            replace("%renamed%", displayName));

                            }
                        }
                    }
                }
            }
        }
    }
}
