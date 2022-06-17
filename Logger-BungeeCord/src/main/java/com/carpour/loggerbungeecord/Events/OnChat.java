package com.carpour.loggerbungeecord.Events;

import org.carour.loggercore.database.mysql.MySQLData;
import com.carpour.logger.Discord.Discord;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.ConfigManager;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnChat implements Listener {

    private final ConfigManager cm = Main.getConfig();

    private final Main main = Main.getInstance();

    @EventHandler
    public void onPlayerChat(ChatEvent event){

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String server =  ((ProxiedPlayer) event.getSender()).getServer().getInfo().getName();
        String message = event.getMessage();
        String serverName = cm.getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("loggerbungee.exempt")) return;

        if (!event.isCancelled() && cm.getBoolean("Log.Player-Chat")) {

            //Log To Files Handling
            if (cm.getBoolean("Log-to-Files")) {

                if (cm.getBoolean("Staff.Enabled") && player.hasPermission("loggerbungee.staff.log")) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Chat-Staff")).replaceAll("%server%", server).replaceAll("%msg%", message), false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(Messages.getString("Files.Player-Chat-Staff").replaceAll("%time%", dateFormat.format(date)).replaceAll("%server%", server).replaceAll("%player%", player.getName()).replaceAll("%msg%", message) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.playerChat(serverName, player.getName(), message, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true));
                    out.write(Messages.getString("Files.Player-Chat").replaceAll("%time%", dateFormat.format(date)).replaceAll("%server%", server).replaceAll("%player%", player.getName()).replaceAll("%msg%", message) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord Integration
            if (cm.getBoolean("Staff.Enabled") && player.hasPermission("loggerbungee.staff.log")) {

                Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Chat-Staff")).replaceAll("%server%", server).replaceAll("%msg%", message), false);

            } else {

                Discord.playerChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Chat")).replaceAll("%server%", server).replaceAll("%msg%", message), false);
            }

            //MySQL Handling
            if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.playerChat(serverName, player.getName(), message, player.hasPermission("loggerbungee.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
