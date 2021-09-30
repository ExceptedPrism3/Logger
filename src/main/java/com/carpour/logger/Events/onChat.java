package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.SQLite.SQLiteData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onChat implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {

        final Player player = event.getPlayer();
        World world = player.getWorld();
        final String worldName = world.getName();
        final String playerName = player.getName();
        String msg = event.getMessage();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        //Log To Files Handling
        if (!event.isCancelled() && main.getConfig().getBoolean("Log.Player-Chat")) {

            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, "\uD83D\uDCAC **|** \uD83D\uDC6E\u200D♂️ " + msg, false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has said => " + msg + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.getConfig().getBoolean("Log.Player-Chat") && main.mySQL.isConnected()) {


                        MySQLData.playerChat(serverName, worldName, playerName, msg, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Player-Chat") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerChat(serverName, player, msg, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has said => " + msg + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord Integration
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                Discord.staffChat(player, "\uD83D\uDCAC **|** \uD83D\uDC6E\u200D♂️ " + msg, false);

            } else {

                Discord.playerChat(player, "\uD83D\uDCAC " + msg, false);
            }

            //MySQL Handling
            if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Chat"))
                    && (main.mySQL.isConnected())) {

                try {

                    MySQLData.playerChat(serverName, worldName, playerName, msg, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite Handling
            if (main.getConfig().getBoolean("SQLite.Enable") && (main.getConfig().getBoolean("Log.Player-Chat"))
                    && (main.getSqLite().isConnected())) {

                try {

                    SQLiteData.insertPlayerChat(serverName, player, msg, player.hasPermission("logger.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
