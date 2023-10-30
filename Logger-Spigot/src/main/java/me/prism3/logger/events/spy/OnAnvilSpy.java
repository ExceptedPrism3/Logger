package me.prism3.logger.events.spy;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;
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

import java.util.List;
import java.util.stream.Collectors;

public class OnAnvilSpy implements Listener {

    private final String anvilSpyMessage;

    public OnAnvilSpy() {
        this.anvilSpyMessage = ChatColor.translateAlternateColorCodes('&',
                Main.getInstance().getConfig().getString("Spy-Features.Anvil-Spy.Message"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilSpy(final InventoryClickEvent event) {

        final Player player = (Player) event.getWhoClicked();

        if (player.hasPermission(Data.loggerExempt) || player.hasPermission(Data.loggerSpyBypass))
            return;

        final Inventory inv = event.getInventory();

        if (!(inv instanceof AnvilInventory))
            return;

        final InventoryView view = event.getView();
        final int rawSlot = event.getRawSlot();

        if (rawSlot != view.convertSlot(rawSlot) || rawSlot != 2)
            return;

        final ItemStack item = event.getCurrentItem();

        if (item == null)
            return;

        final ItemMeta meta = item.getItemMeta();

        if (meta == null || !meta.hasDisplayName())
            return;

        final String displayName = meta.getDisplayName().replace("\\", "\\\\");

        final List<Player> playersWithSpyPermission = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(Data.loggerSpy))
                .collect(Collectors.toList());

        for (Player spyPlayer : playersWithSpyPermission) {
            spyPlayer.sendMessage(this.anvilSpyMessage
                    .replace("%player%", player.getName())
                    .replace("%renamed%", displayName));
        }
    }
}
