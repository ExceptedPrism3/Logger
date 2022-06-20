package me.prism3.loggerbungeecord.events.oncommands;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.database.external.ExternalData;
import me.prism3.loggerbungeecord.database.sqlite.SQLiteData;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OnCommandWhitelist implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onWhitelistedCommand(final ChatEvent event) {

        if (event.isCommand()) {

            final ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            final String playerName = player.getName();
            final String server = player.getServer().getInfo().getName();
            final String command = event.getMessage().replace("\\", "\\\\");
            final List<String> commandParts = Arrays.asList(command.split("\\s+"));

            for (String list : Data.commandsToLog) {

                if (commandParts.get(0).equalsIgnoreCase(list)) {

                    // Log To Files
                    if (Data.isLogToFiles) {

                        if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                            if (!this.main.getMessages().getString("Discord.Player-Commands-Staff").isEmpty()) {

                                this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().getString("Discord.Player-Commands-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command), false);

                            }

                            try {

                                final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                                out.write(this.main.getMessages().getString("Files.Player-Commands-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", player.getName()).replace("%command%", command) + "\n");
                                out.close();

                            } catch (IOException e) {

                                Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }

                            if (Data.isExternal && this.main.getExternal().isConnected()) {


                                ExternalData.playerCommands(Data.serverName, playerName, command, true);

                            }

                            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                                SQLiteData.insertPlayerCommands(Data.serverName, playerName, command, true);

                            }

                            return;

                        }

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                            out.write(this.main.getMessages().getString("Files.Player-Commands").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", player.getName()).replace("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    // Discord Integration
                    if (!player.hasPermission(Data.loggerExemptDiscord)) {

                        if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                            if (!this.main.getMessages().getString("Discord.Player-Commands-Staff").isEmpty()) {

                                this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().getString("Discord.Player-Commands-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command), false);

                            }
                        } else {

                            if (!this.main.getMessages().getString("Discord.Player-Commands").isEmpty()) {

                                this.main.getDiscord().playerCommands(player, Objects.requireNonNull(this.main.getMessages().getString("Discord.Player-Commands")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%command%", command), false);

                            }
                        }
                    }

                    // External
                    if (Data.isExternal && this.main.getExternal().isConnected()) {

                        try {

                            ExternalData.playerCommands(Data.serverName, playerName, command, player.hasPermission(Data.loggerStaffLog));

                        } catch (Exception e) { e.printStackTrace(); }
                    }

                    // SQLite
                    if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                        try {

                            SQLiteData.insertPlayerCommands(Data.serverName, playerName, command, player.hasPermission(Data.loggerStaffLog));

                        } catch (Exception e) { e.printStackTrace(); }
                    }
                }
            }
        }
    }
}
