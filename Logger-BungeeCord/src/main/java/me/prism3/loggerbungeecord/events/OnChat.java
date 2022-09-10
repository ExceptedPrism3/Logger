package me.prism3.loggerbungeecord.events;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

public class OnChat implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onPlayerChat(final ChatEvent event) {

        if (!event.isCommand() && !event.isCancelled()) {

            final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

            if (player.hasPermission(Data.loggerExempt)) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final String server = player.getServer().getInfo().getName();
            final String message = event.getMessage().replace("\\", "\\\\");

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true))) {

                        out.write(this.main.getMessages().getString("Files.Player-Chat-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", player.getName()).replace("%msg%", message) + "\n");

                    } catch (final IOException e) {

                        Log.severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true))) {

                        out.write(this.main.getMessages().getString("Files.Player-Chat").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", player.getName()).replace("%msg%", message) + "\n");

                    } catch (final IOException e) {

                        Log.severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().getString("Discord.Player-Chat-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().getString("Discord.Player-Chat-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%msg%", message), false);

                    }
                } else {

                    if (!this.main.getMessages().getString("Discord.Player-Chat").isEmpty()) {

                        this.main.getDiscord().playerChat(playerName, playerUUID, this.main.getMessages().getString("Discord.Player-Chat").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%msg%", message), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertPlayerChat(Data.serverName, playerName, playerUUID.toString(), null, message, player.hasPermission(Data.loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertPlayerChat(Data.serverName, playerName, playerUUID.toString(), null, message, player.hasPermission(Data.loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
