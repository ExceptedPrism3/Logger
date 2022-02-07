package com.carpour.loggervelocity.Events;

import com.carpour.loggervelocity.Database.MySQL.MySQL;
import com.carpour.loggervelocity.Database.MySQL.MySQLData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.FileHandler;
import com.carpour.loggervelocity.Utils.Messages;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class OnChat{

    @Subscribe
    public void onChat(PlayerChatEvent event){

        Main main = Main.getInstance();
        Messages messages = new Messages();

        Player player = event.getPlayer();
        String playerName = player.getUsername();
        String server = String.valueOf(player.getCurrentServer().get().getServerInfo().getName());
        String message = event.getMessage();
        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (player.hasPermission("loggerproxy.exempt")) return;

        if (main.getConfig().getBoolean("Log-Player.Chat")) {

            // Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                    if (!messages.getString("Discord.Player-Chat-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Chat-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%msg%", message), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(messages.getString("Files.Player-Chat-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%msg%", message) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && MySQL.isConnected()) {

                        MySQLData.playerChat(serverName, playerName, message, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                        SQLiteData.insertPlayerChat(serverName, playerName, message, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true));
                    out.write(messages.getString("Files.Player-Chat").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%msg%", message) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                if (!messages.getString("Discord.Player-Chat-Staff").isEmpty()) {

                    Discord.staffChat(player, messages.getString("Discord.Player-Chat-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%msg%", message), false);

                }

            } else {

                if (!messages.getString("Discord.Player-Chat").isEmpty()) {

                    Discord.playerChat(player, messages.getString("Discord.Player-Chat").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%msg%", message), false);

                }
            }


            // MySQL Handling
            if (main.getConfig().getBoolean("MySQL.Enable") && MySQL.isConnected()) {

                try {

                    MySQLData.playerChat(serverName, playerName, message, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite Handling
            if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                try {

                    SQLiteData.insertPlayerChat(serverName, playerName, message, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
