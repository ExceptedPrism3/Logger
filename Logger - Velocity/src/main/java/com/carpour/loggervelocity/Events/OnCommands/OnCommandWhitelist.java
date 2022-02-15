package com.carpour.loggervelocity.Events.OnCommands;

import com.carpour.loggervelocity.Database.External.External;
import com.carpour.loggervelocity.Database.External.ExternalData;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class OnCommandWhitelist {

    @Subscribe
    public void onWhitelistedCommand(CommandExecuteEvent event) {

        final Main main = Main.getInstance();
        final Messages messages = new Messages();

        Player player = (Player) event.getCommandSource();
        String playerName = player.getUsername();
        String command = event.getCommand().replace("\\", "\\\\");
        String server = player.getCurrentServer().get().getServerInfo().getName();
        List<String> commandParts = Arrays.asList(command.split("\\s+"));
        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (main.getConfig().getBoolean("Log-Player.Commands")) {

            for (String m : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                if (commandParts.contains(m)) {

                    // Log To Files Handling
                    if (main.getConfig().getBoolean("Log-to-Files")) {

                        if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                            if (!messages.getString("Discord.Player-Commands-Staff").isEmpty()) {

                                Discord.staffChat(player, messages.getString("Discord.Player-Commands-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%command%", command), false);

                            }

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                                out.write(messages.getString("Files.Player-Commands-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                                out.close();

                            } catch (IOException e) {

                                main.getLogger().error("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }

                            if (main.getConfig().getBoolean("MySQL.Enable") && External.isConnected()) {

                                ExternalData.playerCommands(serverName, playerName, command, true);

                            }

                            if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                                SQLiteData.insertPlayerCommands(serverName, playerName, command, true);

                            }
                            return;
                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerCommandLogFile(), true));
                            out.write(messages.getString("Files.Player-Commands").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    // Discord Integration
                    if (!player.hasPermission("logger.exempt.discord")) {

                        if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("loggerproxy.staff.log")) {

                            if (!messages.getString("Discord.Player-Commands-Staff").isEmpty()) {

                                Discord.staffChat(player, messages.getString("Discord.Player-Commands-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%command%", command), false);

                            }

                        } else {

                            if (!messages.getString("Discord.Player-Commands").isEmpty()) {

                                Discord.playerCommands(player, messages.getString("Discord.Player-Commands").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%command%", command), false);

                            }
                        }
                    }

                    // MySQL Handling
                    if (main.getConfig().getBoolean("MySQL.Enable") && External.isConnected()) {

                        try {

                            ExternalData.playerCommands(serverName, playerName, command, player.hasPermission("loggerproxy.staff.log"));

                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    // SQLite Handling
                    if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                        try {

                            SQLiteData.insertPlayerCommands(serverName, playerName, command, player.hasPermission("loggerproxy.staff.log"));

                        } catch (Exception exception) { exception.printStackTrace(); }
                    }
                }
            }
        }
    }
}
