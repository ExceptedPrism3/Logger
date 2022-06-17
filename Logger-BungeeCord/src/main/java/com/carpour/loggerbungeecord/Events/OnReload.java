package com.carpour.loggerbungeecord.Events;

import com.carpour.loggerbungeecord.Database.MySQL.MySQLData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.ConfigManager;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnReload implements Listener {

    private final ConfigManager cm = Main.getConfig();

    private final Main main = Main.getInstance();

    @EventHandler
    public void onServerReload(ProxyReloadEvent event){

        CommandSender cS = event.getSender();
        String playerName = event.getSender().getName();
        String serverName = cm.getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (cm.getBoolean("Log.Server-Reload")) {

            if (cS instanceof ProxiedPlayer) {

                ProxiedPlayer player = (ProxiedPlayer) event.getSender();
                String server = player.getServer().getInfo().getName();

                if (player.hasPermission("loggerbungee.exempt")) return;

                //Log To Files Handling
                if (cm.getBoolean("Log-to-Files")) {

                    if (cm.getBoolean("Staff.Enabled") && player.hasPermission("loggerbungee.staff.log")) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Server-Reload-Player-Staff")).replaceAll("%server%", server), false);

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                            out.write(Messages.getString("Files.Server-Reload-Player-Staff-").replaceAll("%time%", dateFormat.format(date)).replaceAll("%server%", server).replaceAll("%player%", playerName) + "\n");
                            out.close();

                        } catch (IOException e) {

                            Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                            MySQLData.serverReload(serverName, playerName, true);

                        }

                        return;

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getReloadLogFile(), true));
                        out.write(Messages.getString("Files.Reload-Player").replaceAll("%time%", dateFormat.format(date)).replaceAll("%server%", server).replaceAll("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                //Discord Integration
                if (cm.getBoolean("Staff.Enabled") && player.hasPermission("loggerbungee.staff.log")) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Server-Reload-Player-Staff")).replaceAll("%server%", server), false);

                } else {

                    Discord.serverReload(player.getName(), Objects.requireNonNull(Messages.getString("Discord.Server-Reload-Player")).replaceAll("%server%", server), false);
                }

                //MySQL Handling
                if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                    try {

                        MySQLData.serverReload(serverName, playerName, player.hasPermission("loggerbungee.staff.log"));

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
                }

            } else {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getReloadLogFile(), true));
                    out.write(Messages.getString("Files.Server-Reload-Console").replaceAll("%time%", dateFormat.format(date)) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                //Discord
                Discord.serverReload(null, Objects.requireNonNull(Messages.getString("Discord.Server-Reload-Console")).replaceAll("%time%", dateFormat.format(date)), false);

                //MySQL Handling
                if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                    try {

                        MySQLData.serverReload(serverName, null, true);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
                }
            }
        }
    }
}
