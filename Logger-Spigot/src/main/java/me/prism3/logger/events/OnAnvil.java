package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;
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

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;


public class OnAnvil implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(final InventoryClickEvent event) {

        if (event.isCancelled())
            return;

        final Player player = (Player) event.getWhoClicked();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String worldName = player.getWorld().getName();
        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final Inventory inv = event.getInventory();

        if (inv instanceof AnvilInventory) {

            final InventoryView view = event.getView();

            final int rawSlot = event.getRawSlot();

            if (rawSlot == view.convertSlot(rawSlot) && rawSlot == 2) {

                final ItemStack item = event.getCurrentItem();

                if (item != null) {

                    final ItemMeta meta = item.getItemMeta();

                    if (meta != null && meta.hasDisplayName()) {

                        final String displayName = meta.getDisplayName().replace("\\", "\\\\");

                        final Map<String, String> placeholders = new HashMap<>();
                        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
                        placeholders.put("%world%", worldName);
                        placeholders.put("%uuid%", playerUUID.toString());
                        placeholders.put("%player%", playerName);
                        placeholders.put("%renamed%", displayName);

                        // Log To Files
                        if (Data.isLogToFiles) {
                            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                                this.main.getFileHandler().handleFileLog(LogCategory.STAFF, "Files.Anvil-Staff", placeholders);
                            } else {
                                this.main.getFileHandler().handleFileLog(LogCategory.ANVIL, "Files.Anvil", placeholders);
                            }
                        }

                        // Discord
                        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

                            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                                this.main.getDiscord().handleDiscordLog("Discord.Anvil-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                            } else {

                                this.main.getDiscord().handleDiscordLog("Discord.Anvil", placeholders, DiscordChannels.ANVIL, playerName, playerUUID);
                            }
                        }

                        // External
                        if (Data.isExternal) {

                            try {

                                Main.getInstance().getDatabase().getDatabaseQueue().queueAnvil(Data.serverName, playerName, playerUUID.toString(), displayName, player.hasPermission(loggerStaffLog));

                            } catch (final Exception e) { e.printStackTrace(); }
                        }

                        // SQLite
                        if (Data.isSqlite) {

                            try {

                                Main.getInstance().getDatabase().getDatabaseQueue().queueAnvil(Data.serverName, playerName, playerUUID.toString(), displayName, player.hasPermission(loggerStaffLog));

                            } catch (final Exception e) { e.printStackTrace(); }
                        }
                    }
                }
            }
        }
    }
}
