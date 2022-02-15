package com.carpour.loggervelocity.Events;

import com.carpour.loggervelocity.Database.External.External;
import com.carpour.loggervelocity.Database.External.ExternalData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.FileHandler;
import com.carpour.loggervelocity.Utils.Messages;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class OnLeave {

    @Subscribe
    public void onQuit(DisconnectEvent event) {

        Main main = Main.getInstance();
        Messages messages = new Messages();

        Player player = event.getPlayer();
        String playerName = player.getUsername();

        // This resolves an error showing up if the targeted server is offline whist connecting
        if (!event.getPlayer().getCurrentServer().isPresent()) return;

        String server = player.getCurrentServer().get().getServerInfo().getName();
        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (player.hasPermission("loggerproxy.exempt")) return;

        if (main.getConfig().getBoolean("Log-Player.Leave")) {

            // Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                    if (!messages.getString("Discord.Player-Leave-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Leave-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(messages.getString("Files.Player-Leave-Staff").replaceAll("%server%", server).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && External.isConnected()) {

                        ExternalData.playerLeave(serverName, playerName, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                        SQLiteData.insertPlayerLeave(serverName, playerName, true);

                    }

                    return;
                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeaveLogFile(), true));
                    out.write(messages.getString("Files.Player-Leave").replaceAll("%server%", server).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (!player.hasPermission("logger.exempt.discord")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                    if (!messages.getString("Discord.Player-Leave-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Leave-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server), false);

                    }

                } else {

                    if (!messages.getString("Discord.Player-Leave").isEmpty()) {

                        Discord.playerLeave(player, messages.getString("Discord.Player-Leave").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server), false);

                    }
                }
            }

            // MySQL Handling
            if (main.getConfig().getBoolean("MySQL.Enable") && External.isConnected()) {

                try {

                    ExternalData.playerLeave(serverName, playerName, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite Handling
            if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                try {

                    SQLiteData.insertPlayerLeave(serverName, playerName, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
