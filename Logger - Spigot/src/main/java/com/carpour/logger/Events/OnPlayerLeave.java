package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnPlayerLeave implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        double x = Math.floor(player.getLocation().getX());
        double y = Math.floor(player.getLocation().getY());
        double z = Math.floor(player.getLocation().getZ());
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (main.getConfig().getBoolean("Log.Player-Leave")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Leave-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Leave-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.playerLeave(serverName, worldName, playerName, x, y, z, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerLeave(serverName, player, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerLeaveLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Leave")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Leave-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

            } else {

                Discord.playerLeave(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Leave")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

            }

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.playerLeave(serverName, worldName, playerName, x, y, z, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerLeave(serverName, player, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
