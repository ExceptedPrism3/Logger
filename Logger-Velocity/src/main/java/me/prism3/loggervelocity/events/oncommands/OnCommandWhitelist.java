package me.prism3.loggervelocity.events.oncommands;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.database.external.ExternalData;
import me.prism3.loggervelocity.database.sqlite.SQLiteData;
import me.prism3.loggervelocity.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnCommandWhitelist {

    @Subscribe
    public void onWhitelistedCommand(final CommandExecuteEvent event) {

        final Main main = Main.getInstance();
        final Player player = (Player) event.getCommandSource();

        if (main.getConfig().getBoolean("Log-Player.Commands") && player.getCurrentServer().isPresent()) {

            final String playerName = player.getUsername();
            final String command = event.getCommand().replace("\\", "\\\\");
            final String server = player.getCurrentServer().get().getServerInfo().getName();
            final List<String> commandParts = Arrays.asList(command.split("\\s+"));

            for (String m : commandsToLog) {

                if (commandParts.contains(m)) {

                    // Log To Files
                    if (isLogToFiles) {

                        if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                            if (!main.getMessages().getString("Discord.Player-Commands-Staff").isEmpty()) {

                                main.getDiscord().staffChat(player, main.getMessages().getString("Discord.Player-Commands-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command), false);

                            }

                            try {

                                final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                                out.write(main.getMessages().getString("Files.Player-Commands-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%command%", command) + "\n");
                                out.close();

                            } catch (IOException e) {

                                main.getLogger().error("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }

                            if (isExternal && main.getExternal().isConnected()) {

                                ExternalData.playerCommands(serverName, playerName, command, true);

                            }

                            if (isSqlite && main.getSqLite().isConnected()) {

                                SQLiteData.insertPlayerCommands(serverName, playerName, command, true);

                            }
                            return;
                        }

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerCommandLogFile(), true));
                            out.write(main.getMessages().getString("Files.Player-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    // Discord
                    if (!player.hasPermission(loggerExemptDiscord)) {

                        if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                            if (!main.getMessages().getString("Discord.Player-Commands-Staff").isEmpty()) {

                                main.getDiscord().staffChat(player, main.getMessages().getString("Discord.Player-Commands-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command), false);

                            }
                        } else {

                            if (!main.getMessages().getString("Discord.Player-Commands").isEmpty()) {

                                main.getDiscord().playerCommands(player, main.getMessages().getString("Discord.Player-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command), false);

                            }
                        }
                    }

                    // External
                    if (isExternal && main.getExternal().isConnected()) {

                        try {

                            ExternalData.playerCommands(serverName, playerName, command, player.hasPermission(loggerStaffLog));

                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    // SQLite
                    if (isSqlite && main.getSqLite().isConnected()) {

                        try {

                            SQLiteData.insertPlayerCommands(serverName, playerName, command, player.hasPermission(loggerStaffLog));

                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }
            }
        }
    }
}
