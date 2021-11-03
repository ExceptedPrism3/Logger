package com.carpour.loggerbungeecord.Events;

import com.carpour.loggerbungeecord.Database.MySQL.MySQLData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.ConfigManager;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnLeave implements Listener {

    private final ConfigManager cm = Main.getConfig();
    private final Main main = Main.getInstance();

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event){

        ProxiedPlayer player = event.getPlayer();
        String playerName = player.getName();
        String server = player.getServer().getInfo().getName();
        String serverName = cm.getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("loggerbungee.exempt")) return;

        if (player.getServer() == null) return;

        if (cm.getBoolean("Log.Player-Leave")) {

            //Log To Files Handling
            if (cm.getBoolean("Log-to-Files")) {

                if (cm.getBoolean("Staff.Enabled") && player.hasPermission("loggerbungee.staff.log")) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Leave-Staff")).replaceAll("%server%", server), false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(Messages.getString("Files.Player-Leave-Staff").replaceAll("%server%", server).replaceAll("%time%", dateFormat.format(date)).replaceAll("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.playerLeave(serverName, playerName, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeaveLogFile(), true));
                    out.write(Messages.getString("Files.Player-Leave").replaceAll("%server%", server).replaceAll("%time%", dateFormat.format(date)).replaceAll("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord Integration
            if (cm.getBoolean("Staff.Enabled") && player.hasPermission("loggerbungee.staff.log")) {

                Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Leave-Staff")).replaceAll("%server%", server), false);

            } else {

                Discord.playerLeave(player, Objects.requireNonNull(Messages.getString("Discord.Player-Leave")).replaceAll("%server%", server), false);
            }

            //MySQL Handling
            if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.playerLeave(serverName, playerName, player.hasPermission("loggerbungee.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
