package me.prism3.loggerbungeecord.events.oncommands;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OnCommandWhitelist implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWhitelistedCommand(final ChatEvent event) {

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final String server = player.getServer().getInfo().getName();
        final String command = event.getMessage().replace("\\", "\\\\");
        final List<String> commandParts = Arrays.asList(command.split("\\s+"));

        for (String list : Data.commandsToLog) {

            if (commandParts.get(0).equalsIgnoreCase(list)) {

                // Log To Files
                if (Data.isLogToFiles) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true))) {

                            out.write(this.main.getMessages().getString("Files.Player-Commands-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", player.getName()).replace("%command%", command).replace("%uuid%", playerUUID.toString()) + "\n");

                        } catch (final IOException e) {

                            Log.severe("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();
                        }
                    } else {

                        try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true))) {

                            out.write(this.main.getMessages().getString("Files.Player-Commands").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", player.getName()).replace("%command%", command).replace("%uuid%", playerUUID.toString()) + "\n");

                        } catch (final IOException e) {

                            Log.severe("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();
                        }
                    }
                }

                // Discord Integration
                if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!this.main.getMessages().getString("Discord.Player-Commands-Staff").isEmpty()) {

                            this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().getString("Discord.Player-Commands-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command).replace("%uuid%", playerUUID.toString()), false);
                        }
                    } else {

                        if (!this.main.getMessages().getString("Discord.Player-Commands").isEmpty()) {

                            this.main.getDiscord().playerCommands(playerName, playerUUID, this.main.getMessages().getString("Discord.Player-Commands").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command).replace("%uuid%", playerUUID.toString()), false);
                        }
                    }
                }

                // External
                if (Data.isExternal) {

                    try {

//                        Main.getInstance().getDatabase().insertPlayerCommands(Data.serverName, playerName ,playerUUID.toString(), null, command, player.hasPermission(Data.loggerStaffLog));

                    } catch (final Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite) {

                    try {

//                        Main.getInstance().getSqLite().insertPlayerCommands(Data.serverName, playerName, playerUUID.toString(), null, command, player.hasPermission(Data.loggerStaffLog));

                    } catch (final Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
