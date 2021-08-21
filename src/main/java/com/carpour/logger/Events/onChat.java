package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.database.MySQL.MySQLData;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.database.SQLite.SQLiteData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onChat implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        System.out.println("YEEHAW");


            final Player player = event.getPlayer();
            World world = player.getWorld();
            final String worldName = world.getName();
            final String playerName = player.getName();
            String msg = event.getMessage();
            String serverName = main.getConfig().getString("Server-Name");
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


        if (player.hasPermission("logger.exempt")) {
            return;
        }

        //Log To Files Handling
        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Chat"))) {



            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff")){

                Discord.staffChat(player, "\uD83D\uDCAC **|** \uD83D\uDC6E\u200D♂️ " + msg, false, Color.GREEN);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has said => " + msg +"\n");
                    out.close();

                    /*if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Chat")) && (main.mySQL.isConnected())) {


                        MySQLData.playerChat(serverName, worldName, playerName, msg, true);

                    }*/

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                /*return;*/

            }

            Discord.playerChat(player, "\uD83D\uDCAC " + msg, false, Color.GREEN);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has said => " + msg +"\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        //MySQL Handling
        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Chat"))
                && (main.mySQL.isConnected())) {
            try {

                MySQLData.playerChat(serverName, worldName, playerName, msg, player.hasPermission("logger.staff"));

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

        //SQLite Handling
        if (main.getConfig().getBoolean("SQLite.Enable") && (main.getConfig().getBoolean("Log.Player-Chat"))
                && (main.getSqLite().isConnected())) {
            try {
                SQLiteData.insertPlayerChat(serverName, player, msg, player.hasPermission("logger.staff"));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
