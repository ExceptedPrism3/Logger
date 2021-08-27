package com.carpour.logger.Events;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.database.MySQL.MySQLData;
import com.carpour.logger.database.SQLite.SQLiteData;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onPlayerJoin implements Listener {

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
                && main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {
            if (player.hasPermission("logger.warning")) {
                player.sendMessage(ChatColor.GRAY + "[" +
                        ChatColor.AQUA + "Logger" + ChatColor.GRAY + "] " +
                        ChatColor.RED + "Enabling both Whitelist and Blacklist isn't supported. " +
                        "Please disable one of them to continue logging Player Commands");
            }
        }

        if (player.hasPermission("logger.exempt")){ return; }

            if (main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Join"))) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                    Discord.staffChat(player, "\uD83D\uDC4B **|** \uD83D\uDC6E\u200D♂ [" + worldName + "]" + " X = " + x + " Y = " + y + " Z = " + z + " **IP** ||" + ip + "||", false, Color.red );

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has logged in at X = " + x + " Y = " + y + " Z = " + z + " and their IP is " + ip + "\n");
                        out.close();

                        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Join")) && (main.mySQL.isConnected())) {

                            assert ip != null;
                            MySQLData.playerJoin(serverName, worldName, playerName, x, y, z, ip, true);

                        }

                    } catch (IOException e) {

                        System.out.println("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    return;

                }

                Discord.playerJoin(player, "\uD83D\uDC4B [" + worldName + "]" + " X = " + x + " Y = " + y + " Z = " + z + " **IP** ||" + ip + "||", false, Color.red );

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerJoinLogFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has logged in at X = " + x + " Y = " + y + " Z = " + z + " and their IP is " + ip + "\n");
                    out.close();

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

            }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Join")) && (main.mySQL.isConnected())){

            try {

                MySQLData.playerJoin(serverName, worldName, playerName, x, y, z, ip, player.hasPermission("logger.staff"));

            }catch (Exception e) {

                e.printStackTrace();

            }
        }
        if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Player-Join") && main.getSqLite().isConnected()) {
            try {
                SQLiteData.insertPlayerJoin(serverName, player, player.hasPermission("logger.staff"));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
