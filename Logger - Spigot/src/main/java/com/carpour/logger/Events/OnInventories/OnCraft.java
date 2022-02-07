package com.carpour.logger.Events.OnInventories;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OnCraft implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCrafting(CraftItemEvent event){

        final Player player = (Player) event.getWhoClicked();
        final String playerName = player.getName();
        final World world = player.getWorld();
        final String worldName = world.getName();
        final String item = Objects.requireNonNull(event.getCurrentItem()).getType().name().replace("_", " ");
        int amount = event.getCurrentItem().getAmount();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-Player.Craft")) {

            // Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft-Staff")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Craft-Staff")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                        ExternalData.playerCraft(serverName, worldName, playerName, item, amount, x, y, z, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerCraft(serverName, player, item, amount, x, y, z,true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCraftFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Craft")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft-Staff")).isEmpty()) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft-Staff")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                }

            } else {

                if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft")).isEmpty()) {

                    Discord.playerCraft(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Craft")).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%item%", item).replaceAll("%amount%", String.valueOf(amount)).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);
                }
            }

            // MySQL Handling
            if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                try {

                    ExternalData.playerCraft(serverName, worldName, playerName, item, amount, x, y, z, true);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite Handling
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerCraft(serverName, player, item, amount, x, y, z, player.hasPermission("logger.staff.log"));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
