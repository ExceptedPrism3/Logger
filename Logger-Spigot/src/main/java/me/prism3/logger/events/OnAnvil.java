package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.events.spy.OnAnvilSpy;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
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
import java.util.Objects;

public class OnAnvil implements Listener {

private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(final InventoryClickEvent event) {

        // Anvil Spy
        if (this.main.getConfig().getBoolean("Spy-Features.Anvil-Spy.Enable")) {

            new OnAnvilSpy().onAnvilSpy(event);

        }

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Anvil")) {

            final Player player = (Player) event.getWhoClicked();

            if (player.hasPermission(Data.loggerExempt)) return;

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

                                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Anvil-Staff")).isEmpty()) {

                                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Anvil-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%renamed%", displayName), false);

                                    }

                                    try {

                                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Anvil-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%renamed%", displayName) + "\n");
                                        out.close();

                                    } catch (IOException e) {

                                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                        e.printStackTrace();

                                    }

                                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                                        ExternalData.anvil(Data.serverName, playerName, displayName, true);

                                    }

                                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                                        SQLiteData.insertAnvil(Data.serverName, player, displayName, true);

                                    }

                                    return;

                                }

                                try {

                                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAnvilFile(), true));
                                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Anvil")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%renamed%", displayName) + "\n");
                                    out.close();

                                } catch (IOException e) {

                                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                    e.printStackTrace();

                                }
                            }

                            // Discord
                            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Anvil-Staff")).isEmpty()) {

                                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Anvil-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%renamed%", displayName), false);

                                    }
                                } else {

                                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Anvil")).isEmpty()) {

                                        this.main.getDiscord().anvil(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Anvil")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%renamed%", displayName), false);
                                    }
                                }
                            }

                            // External
                            if (Data.isExternal && this.main.getExternal().isConnected()) {

                                try {

                                    ExternalData.anvil(Data.serverName, playerName, displayName, player.hasPermission(Data.loggerStaffLog));

                                } catch (Exception e) { e.printStackTrace(); }
                            }

                            // SQLite
                            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                                try {

                                    SQLiteData.insertAnvil(Data.serverName, player, displayName, player.hasPermission(Data.loggerStaffLog));

                                } catch (Exception e) { e.printStackTrace(); }
                            }
                        }
                    }
                }
            }
        }
    }
}
