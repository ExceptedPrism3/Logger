package me.prism3.loggervelocity.Events.OnCommands;

import me.prism3.loggervelocity.Database.External.External;
import me.prism3.loggervelocity.Database.External.ExternalData;
import me.prism3.loggervelocity.Database.SQLite.SQLite;
import me.prism3.loggervelocity.Database.SQLite.SQLiteData;
import me.prism3.loggervelocity.Discord.Discord;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.ServerSide.Console;
import me.prism3.loggervelocity.Utils.FileHandler;
import me.prism3.loggervelocity.Utils.Messages;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static me.prism3.loggervelocity.Utils.Data.*;

public class OnCommand {

    @Subscribe
    public void onCmd(final CommandExecuteEvent event) {

        final Main main = Main.getInstance();
        final Messages messages = new Messages();

        final CommandSource commandSource = event.getCommandSource();

        if (commandSource instanceof Player){

            final Player player = (Player) commandSource;

            if (main.getConfig().getBoolean("Log-Player.Commands") && player.getCurrentServer().isPresent()) {

                if (isWhitelisted && isBlacklisted) return;

                if (player.hasPermission(loggerExempt)) return;

                final String playerName = player.getUsername();
                final String command = event.getCommand().replace("\\", "\\\\");
                final String server = player.getCurrentServer().get().getServerInfo().getName();
                final List<String> commandParts = Arrays.asList(command.split("\\s+"));

                if (isBlacklisted) {

                    for (String list : commandsToBlock) {

                        if (commandParts.contains(list)) return;

                    }
                }

                // Whitelist Commands
                if (isWhitelisted) {

                    new OnCommandWhitelist().onWhitelistedCommand(event);

                    return;
                }

                // Log To Files
                if (isLogToFiles) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!messages.getString("Discord.Player-Commands-Staff").isEmpty()) {

                            Discord.staffChat(player, messages.getString("Discord.Player-Commands-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%command%", command), false);

                        }

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                            out.write(messages.getString("Files.Player-Commands-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (isExternal && External.isConnected()) {

                            ExternalData.playerCommands(serverName, playerName, command, true);

                        }

                        if (isSqlite && SQLite.isConnected()) {

                            SQLiteData.insertPlayerCommands(serverName, playerName, command, true);

                        }

                        return;

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerCommandLogFile(), true));
                        out.write(messages.getString("Files.Player-Commands").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!player.hasPermission(loggerExemptDiscord)) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!messages.getString("Discord.Player-Commands-Staff").isEmpty()) {

                            Discord.staffChat(player, messages.getString("Discord.Player-Commands-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%command%", command), false);

                        }
                    } else {

                        if (!messages.getString("Discord.Player-Commands").isEmpty()) {

                            Discord.playerCommands(player, messages.getString("Discord.Player-Commands").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%command%", command), false);

                        }
                    }
                }

                // External
                if (isExternal && External.isConnected()) {

                    try {

                        ExternalData.playerCommands(serverName, playerName, command, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite && SQLite.isConnected()) {

                    try {

                        SQLiteData.insertPlayerCommands(serverName, playerName, command, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }else{

            new Console().onConsole(event);

        }
    }
}
