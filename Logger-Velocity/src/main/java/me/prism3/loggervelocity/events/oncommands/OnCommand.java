package me.prism3.loggervelocity.events.oncommands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.serverside.Console;
import me.prism3.loggervelocity.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnCommand {

    @Subscribe
    public void onCmd(final CommandExecuteEvent event) {

        final Main main = Main.getInstance();

        final CommandSource commandSource = event.getCommandSource();

        if (commandSource instanceof Player) {

            final Player player = (Player) commandSource;

            if (main.getConfig().getBoolean("Log-Player.Commands") && player.getCurrentServer().isPresent()) {

                if (isWhitelisted && isBlacklisted) return;

                if (player.hasPermission(loggerExempt)) return;

                final String playerName = player.getUsername();
                final UUID playerUUID = player.getUniqueId();
                final String command = event.getCommand().replace("\\", "\\\\");
                final String server = player.getCurrentServer().get().getServerInfo().getName();
                final List<String> commandParts = Arrays.asList(command.split("\\s+"));

                if (isBlacklisted)
                    for (String list : commandsToBlock)
                        if (commandParts.contains(list)) return;

                // Whitelist Commands
                if (isWhitelisted) {

                    new OnCommandWhitelist().onWhitelistedCommand(event);
                    return;
                }

                // Log To Files
                if (isLogToFiles) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                            out.write(main.getMessages().getString("Files.Player-Commands-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    } else {

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerCommandLogFile(), true));
                            out.write(main.getMessages().getString("Files.Player-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getLogger().error("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }
                }

                // Discord
                if (!player.hasPermission(loggerExemptDiscord)) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!main.getMessages().getString("Discord.Player-Commands-Staff").isEmpty()) {

                            main.getDiscord().staffChat(playerName, playerUUID, main.getMessages().getString("Discord.Player-Commands-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command), false);

                        }
                    } else {

                        if (!main.getMessages().getString("Discord.Player-Commands").isEmpty()) {

                            main.getDiscord().playerCommands(playerName, playerUUID, main.getMessages().getString("Discord.Player-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command), false);

                        }
                    }
                }

                // External
                if (isExternal) {

                    try {

                        Main.getInstance().getDatabase().insertPlayerCommands(serverName, playerName, command, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite) {

                    try {

                        Main.getInstance().getSqLite().insertPlayerCommands(serverName, playerName, command, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        } else {

            new Console().onConsole(event);

        }
    }
}
