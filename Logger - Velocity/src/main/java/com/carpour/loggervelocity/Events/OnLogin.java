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
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OnLogin {

    @Subscribe
    public void onJoin(PostLoginEvent event) {

        Main main = Main.getInstance();
        Messages messages = new Messages();

        Player player = event.getPlayer();
        String playerName = player.getUsername();
        InetSocketAddress playerIP = player.getRemoteAddress();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("loggerproxy.exempt")) return;

        if (main.getConfig().getBoolean("Log-Player.Login")) {

            if (!main.getConfig().getBoolean("Player-Login.Player-IP")) playerIP = null;

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                    if (!messages.getString("Discord.Player-Login-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Login-Staff").replaceAll("%time%", dateFormat.format(date)).replaceAll("%IP%", String.valueOf(playerIP)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(messages.getString("Files.Player-Login-Staff").replaceAll("%time%", dateFormat.format(date)).replaceAll("%player%", playerName).replaceAll("%IP%", String.valueOf(playerIP)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && MySQL.isConnected()) {

                        MySQLData.playerLogin(serverName, playerName, playerIP, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                        SQLiteData.insertPlayerLogin(serverName, playerName, playerIP, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLoginLogFile(), true));
                    out.write(messages.getString("Files.Player-Login").replaceAll("%time%", dateFormat.format(date)).replaceAll("%player%", playerName).replaceAll("%IP%", String.valueOf(playerIP)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord Integration
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                if (!messages.getString("Discord.Player-Login-Staff").isEmpty()) {

                    Discord.staffChat(player, messages.getString("Discord.Player-Login-Staff").replaceAll("%time%", dateFormat.format(date)).replaceAll("%IP%", String.valueOf(playerIP)), false);

                }

            } else {

                if (!messages.getString("Discord.Player-Login").isEmpty()) {

                    Discord.playerLogin(player, messages.getString("Discord.Player-Login").replaceAll("%time%", dateFormat.format(date)).replaceAll("%IP%", String.valueOf(playerIP)), false);

                }
            }


            //MySQL Handling
            if (main.getConfig().getBoolean("MySQL.Enable") && MySQL.isConnected()) {

                try {

                    MySQLData.playerLogin(serverName, playerName, playerIP, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite Handling
            if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                try {

                    SQLiteData.insertPlayerLogin(serverName, playerName, playerIP, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
