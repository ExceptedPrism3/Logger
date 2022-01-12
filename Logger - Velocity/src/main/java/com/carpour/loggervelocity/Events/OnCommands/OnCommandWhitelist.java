package com.carpour.loggervelocity.Events.OnCommands;

import com.carpour.loggervelocity.Database.MySQL.MySQL;
import com.carpour.loggervelocity.Database.MySQL.MySQLData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.FileHandler;
import com.carpour.loggervelocity.Utils.Messages;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OnCommandWhitelist {

    @Subscribe
    public void onWhitelistedCommand(CommandExecuteEvent event) {

        Main main = Main.getInstance();
        Messages messages = new Messages();

        Player player = (Player) event.getCommandSource();
        String playerName = player.getUsername();
        String command = event.getCommand();
        String server = player.getCurrentServer().get().getServerInfo().getName();
        List<String> commandParts = Arrays.asList(command.split("\\s+"));
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (main.getConfig().getBoolean("Log-Player.Commands")) {

            for (String m : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                if (commandParts.contains(m)) {

                    //Log To Files Handling
                    if (main.getConfig().getBoolean("Log-to-Files")) {

                        if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                            if (!messages.getString("Discord.Player-Commands-Staff").isEmpty()) {

                                Discord.staffChat(player, messages.getString("Discord.Player-Commands-Staff").replaceAll("%time%", dateFormat.format(date)).replaceAll("%server%", server).replaceAll("%command%", command), false);

                            }

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                                out.write(messages.getString("Files.Player-Commands-Staff").replaceAll("%time%", dateFormat.format(date)).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                                out.close();

                            } catch (IOException e) {

                                main.getLogger().error("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }

                            if (main.getConfig().getBoolean("MySQL.Enable") && MySQL.isConnected()) {

                                MySQLData.playerCommands(serverName, playerName, command, true);

                            }

                            if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                                SQLiteData.insertPlayerCommands(serverName, playerName, command, true);

                            }
                            return;
                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerCommandLogFile(), true));
                            out.write(messages.getString("Files.Player-Commands").replaceAll("%time%", dateFormat.format(date)).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    //Discord Integration
                    if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                        if (!messages.getString("Discord.Player-Commands-Staff").isEmpty()) {

                            Discord.staffChat(player, messages.getString("Discord.Player-Commands-Staff").replaceAll("%time%", dateFormat.format(date)).replaceAll("%server%", server).replaceAll("%command%", command), false);

                        }

                    } else {

                        if (!messages.getString("Discord.Player-Commands").isEmpty()) {

                            Discord.playerCommands(player, messages.getString("Discord.Player-Commands").replaceAll("%time%", dateFormat.format(date)).replaceAll("%server%", server).replaceAll("%command%", command), false);

                        }
                    }


                    //MySQL Handling
                    if (main.getConfig().getBoolean("MySQL.Enable") && MySQL.isConnected()) {

                        try {

                            MySQLData.playerCommands(serverName, playerName, command, player.hasPermission("loggerproxy.staff.log"));

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }

                    //SQLite Handling
                    if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                        try {

                            SQLiteData.insertPlayerCommands(serverName, playerName, command, player.hasPermission("loggerproxy.staff.log"));

                        } catch (Exception exception) {

                            exception.printStackTrace();

                        }
                    }
                }
            }
        }
    }
}
