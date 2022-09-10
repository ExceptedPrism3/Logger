package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.events.spy.OnAnvilSpy;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnAnvil implements Listener {

private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(final InventoryClickEvent event) {

        // Anvil Spy
        if (this.main.getConfig().getBoolean("Spy-Features.Anvil-Spy.Enable")) {
            new OnAnvilSpy().onAnvilSpy(event);
        }

        if (!event.isCancelled()) {

            final Player player = (Player) event.getWhoClicked();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

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

                            // Log To Files
                            if (Data.isLogToFiles) {

                                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                                        out.write(this.main.getMessages().get().getString("Files.Anvil-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%renamed%", displayName) + "\n");

                                    } catch (final IOException e) {

                                        Log.warning("An error occurred while logging into the appropriate file.");
                                        e.printStackTrace();

                                    }
                                } else {

                                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAnvilFile(), true))) {

                                        out.write(this.main.getMessages().get().getString("Files.Anvil").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%renamed%", displayName) + "\n");

                                    } catch (final IOException e) {

                                        Log.warning("An error occurred while logging into the appropriate file.");
                                        e.printStackTrace();

                                    }
                                }
                            }

                            // Discord
                            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                                    if (!this.main.getMessages().get().getString("Discord.Anvil-Staff").isEmpty()) {

                                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Anvil-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%renamed%", displayName), false);

                                    }
                                } else {

                                    if (!this.main.getMessages().get().getString("Discord.Anvil").isEmpty()) {

                                        this.main.getDiscord().anvil(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Anvil").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%renamed%", displayName), false);
                                    }
                                }
                            }

                            // External
                            if (Data.isExternal ) {

                                try {

                                    Main.getInstance().getDatabase().insertAnvil(Data.serverName, playerName, playerUUID.toString(), displayName, player.hasPermission(loggerStaffLog));

                                } catch (final Exception e) { e.printStackTrace(); }
                            }

                            // SQLite
                            if (Data.isSqlite) {

                                try {

                                    Main.getInstance().getSqLite().insertAnvil(Data.serverName, playerName, playerUUID.toString(), displayName, player.hasPermission(loggerStaffLog));

                                } catch (final Exception e) { e.printStackTrace(); }
                            }
                        }
                    }
                }
            }
        }
    }
}
