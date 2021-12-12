package com.carpour.logger.Events;

import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import org.carour.loggercore.database.mysql.MySQLData;
import org.carour.loggercore.database.sqlite.SQLiteData;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnPlayerDeath implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        double x = Math.floor(player.getLocation().getX());
        double y = Math.floor(player.getLocation().getY());
        double z = Math.floor(player.getLocation().getZ());
        String cause = Objects.requireNonNull(event.getEntity().getLastDamageCause()).getCause().name();
        String killer = "";
        String itemInUse = "";
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (!(event.getEntity().getKiller() == null)){

            if (event.getEntity().getLastDamageCause().getEntity() instanceof Player) {
                cause = "Player";
            }

            killer = event.getEntity().getKiller().getName();
            itemInUse = Objects.requireNonNull(Objects.requireNonNull(event.getEntity().getKiller()).getItemInHand().getType().name());

        }

        if (main.getConfig().getBoolean("Log.Player-Death")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    main.getDiscord().sendStaffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%itemUsed%", itemInUse), false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Death-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%itemUsed%", itemInUse) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.playerDeath(serverName, worldName, playerName, x, y, z, cause, killer, itemInUse, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerDeath(serverName, player, cause, killer, itemInUse, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerDeathLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Death")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%itemUsed%", itemInUse) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                main.getDiscord().sendStaffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Death-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%itemUsed%", itemInUse), false);

            } else {

                main.getDiscord().sendPlayerDeath(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Death")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%cause%", cause).replaceAll("%killer%", killer).replaceAll("%itemUsed%", itemInUse), false);

            }

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && (main.mySQL.isConnected())) {

                try {

                    MySQLData.playerDeath(serverName, worldName, playerName, x, y, z, cause, killer, itemInUse, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerDeath(serverName, player, cause, killer, itemInUse, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
