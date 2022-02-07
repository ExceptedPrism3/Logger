package com.carpour.loggerbungeecord.Events;

import com.carpour.loggerbungeecord.Database.MySQL.MySQLData;
import com.carpour.loggerbungeecord.Database.SQLite.SQLiteData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OnLeave implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event){

        ProxiedPlayer player = event.getPlayer();
        String playerName = player.getName();

        // This resolves an error showing up if the targeted server is offline whist connecting
        if (player.getServer() == null) return;

        String playerServerName = player.getServer().getInfo().getName();
        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (player.getServer() == null || playerServerName == null) return;

        if (player.hasPermission("loggerproxy.exempt")) return;

        if (main.getConfig().getBoolean("Log-Player.Leave")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                    if (!Messages.getString("Discord.Player-Leave-Staff").isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Leave-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", playerServerName), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(Messages.getString("Files.Player-Leave-Staff").replaceAll("%server%", playerServerName).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.playerLeave(serverName, playerName, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerLeave(serverName, playerName, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeaveLogFile(), true));
                    out.write(Messages.getString("Files.Player-Leave").replaceAll("%server%", playerServerName).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord Integration
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                if (!Messages.getString("Discord.Player-Leave-Staff").isEmpty()) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Leave-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", playerServerName), false);

                }

            } else {

                if (!Messages.getString("Discord.Player-Leave").isEmpty()) {

                    Discord.playerLeave(player, Objects.requireNonNull(Messages.getString("Discord.Player-Leave")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", playerServerName), false);

                }
            }

            //MySQL Handling
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.playerLeave(serverName, playerName, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite Handling
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerLeave(serverName, playerName, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
