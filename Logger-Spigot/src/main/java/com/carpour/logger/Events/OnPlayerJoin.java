package com.carpour.logger.Events;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import org.carour.loggercore.database.mysql.MySQLData;
import org.carour.loggercore.database.sqlite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnPlayerJoin implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        InetSocketAddress ip = player.getAddress();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")
                && main.getConfig().getBoolean("Player-Commands.Blacklist-Commands") &&
            player.hasPermission("logger.staff")) {
            player.sendMessage(ChatColor.GRAY + "[" +
                    ChatColor.AQUA + "Logger" + ChatColor.GRAY + "]" + ChatColor.WHITE + " | " +
                    ChatColor.RED + "Enabling both Whitelist and Blacklist isn't supported. " +
                    "Please disable one of them to continue logging Player Commands");

        }

        if (!main.getConfig().getBoolean("Player-Join.Player-IP")) ip = null;

        if (player.hasPermission("logger.exempt")) return;

        if (main.getConfig().getBoolean("Log.Player-Join")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {


                    main.getDiscord().sendStaffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Join-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%IP%", String.valueOf(ip)), false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Join-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%IP%", String.valueOf(ip)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                        MySQLData.playerJoin(serverName, worldName, playerName, x, y, z, ip, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerJoin(serverName, player, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerJoinLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Join")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%IP%", String.valueOf(ip)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                main.getDiscord().sendStaffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Join-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%IP%", String.valueOf(ip)), false);

            } else {

                main.getDiscord().sendPlayerJoin(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Join")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%IP%", String.valueOf(ip)), false);

            }

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.playerJoin(serverName, worldName, playerName, x, y, z, ip, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerJoin(serverName, player, player.hasPermission("logger.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
