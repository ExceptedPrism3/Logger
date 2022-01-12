package com.carpour.loggerbungeecord.Events;

import com.carpour.loggerbungeecord.Database.MySQL.MySQLData;
import com.carpour.loggerbungeecord.Database.SQLite.SQLiteData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.ConfigManager;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnLogin implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onLogging(PostLoginEvent event){

        ProxiedPlayer player = event.getPlayer();
        String playerName = player.getName();
        InetSocketAddress playerIP = (InetSocketAddress) event.getPlayer().getSocketAddress();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("loggerproxy.exempt")) return;

        if (main.getConfig().getBoolean("Log-Player.Login")) {

            if (!main.getConfig().getBoolean("Player-Login.Player-IP")) playerIP = null;

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                    if (!Messages.getString("Discord.Player-Login-Staff").isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Login-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%IP%", String.valueOf(playerIP)), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(Messages.getString("Files.Player-Login-Staff").replaceAll("%time%", dateFormat.format(date)).replaceAll("%player%", playerName).replaceAll("%IP%", String.valueOf(playerIP)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.playerLogin(serverName, playerName, playerIP, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerLogin(serverName, playerName, playerIP, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLoginLogFile(), true));
                    out.write(Messages.getString("Files.Player-Login").replaceAll("%time%", dateFormat.format(date)).replaceAll("%player%", playerName).replaceAll("%IP%", String.valueOf(playerIP)) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord Integration
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                if (!Messages.getString("Discord.Player-Login-Staff").isEmpty()) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Login-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%IP%", String.valueOf(playerIP)), false);

                }

            } else {

                if (!Messages.getString("Discord.Player-Login").isEmpty()) {

                    Discord.playerLogin(player, Objects.requireNonNull(Messages.getString("Discord.Player-Login")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%IP%", String.valueOf(playerIP)), false);

                }
            }

            //MySQL Handling
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.playerLogin(serverName, playerName, playerIP, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite Handling
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerLogin(serverName, playerName, playerIP, player.hasPermission("loggerproxy.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
