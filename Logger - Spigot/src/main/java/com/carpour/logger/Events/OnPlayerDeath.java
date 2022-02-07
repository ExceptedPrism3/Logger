package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OnPlayerDeath implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        String cause = Objects.requireNonNull(player.getLastDamageCause()).getCause().name();
        String killer = "";
        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (player.getKiller() != null){

            if (player.getLastDamageCause().getEntity() instanceof Player) {
                cause = "Player";
            }

                killer = player.getKiller().getName();

        }

        if (main.getConfig().getBoolean("Log-Player.Death")) {

            // Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer), false);
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Death-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                        ExternalData.playerDeath(serverName, worldName, playerName, x, y, z, cause, killer, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerDeath(serverName, player, cause, killer, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerDeathLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Death")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).isEmpty()) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer), false);

                }

            } else {

                if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Death")).isEmpty()) {

                    Discord.playerDeath(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Death")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer), false);
                }
            }

            // MySQL
            if (main.getConfig().getBoolean("Database.Enable") && (main.external.isConnected())) {

                try {

                    ExternalData.playerDeath(serverName, worldName, playerName, x, y, z, cause, killer, player.hasPermission("logger.staff.log"));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerDeath(serverName, player, cause, killer, player.hasPermission("logger.staff.log"));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
