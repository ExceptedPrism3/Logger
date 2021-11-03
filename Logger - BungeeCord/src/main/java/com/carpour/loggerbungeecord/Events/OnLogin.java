package com.carpour.loggerbungeecord.Events;

import com.carpour.loggerbungeecord.Database.MySQL.MySQLData;
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

    private final ConfigManager cm = Main.getConfig();
    private final Main main = Main.getInstance();

    @EventHandler
    public void onCmd(PostLoginEvent event){

        ProxiedPlayer player = event.getPlayer();
        String playerName = player.getName();
        InetSocketAddress playerIP = (InetSocketAddress) event.getPlayer().getSocketAddress();
        String serverName = cm.getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("loggerbungee.exempt")) return;

        if (cm.getBoolean("Log.Player-Login")) {

            if (!cm.getBoolean("Player-Login.Player-IP")) playerIP = null;

            //Log To Files Handling
            if (cm.getBoolean("Log-to-Files")) {

                if (cm.getBoolean("Staff.Enabled") && player.hasPermission("loggerbungee.staff.log")) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Login-Staff")).replaceAll("%IP%", String.valueOf(playerIP)), false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(Messages.getString("Files.Player-Login-Staff").replaceAll("%time%", dateFormat.format(date)).replaceAll("%player%", playerName).replaceAll("%IP%", String.valueOf(playerIP)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.playerLogin(serverName, playerName, playerIP, true);

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
            if (cm.getBoolean("Staff.Enabled") && player.hasPermission("loggerbungee.staff.log")) {

                Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Login-Staff")).replaceAll("%IP%", String.valueOf(playerIP)), false);

            } else {

                Discord.playerLogin(player, Objects.requireNonNull(Messages.getString("Discord.Player-Login")).replaceAll("%IP%", String.valueOf(playerIP)), false);
            }

            //MySQL Handling
            if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.playerLogin(serverName, playerName, playerIP, player.hasPermission("loggerbungee.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
