package me.prism3.loggervelocity.Events;

import me.prism3.loggervelocity.Database.External.External;
import me.prism3.loggervelocity.Database.External.ExternalData;
import me.prism3.loggervelocity.Database.SQLite.SQLite;
import me.prism3.loggervelocity.Database.SQLite.SQLiteData;
import me.prism3.loggervelocity.Discord.Discord;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.Utils.FileHandler;
import me.prism3.loggervelocity.Utils.Messages;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.Utils.Data.*;

public class OnLeave {

    @Subscribe
    public void onQuit(final DisconnectEvent event) {

        final Main main = Main.getInstance();
        final Messages messages = new Messages();

        if (main.getConfig().getBoolean("Log-Player.Leave")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getUsername();

            // This resolves an error showing up if the targeted server is offline whist connecting
            if (!player.getCurrentServer().isPresent()) return;

            final String server = player.getCurrentServer().get().getServerInfo().getName();

            // Log To Files
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!messages.getString("Discord.Player-Leave-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Leave-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server), false);

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(messages.getString("Files.Player-Leave-Staff").replaceAll("%server%", server).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && External.isConnected()) {

                        ExternalData.playerLeave(serverName, playerName, true);

                    }

                    if (isSqlite && SQLite.isConnected()) {

                        SQLiteData.insertPlayerLeave(serverName, playerName, true);

                    }

                    return;
                }

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeaveLogFile(), true));
                    out.write(messages.getString("Files.Player-Leave").replaceAll("%server%", server).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!messages.getString("Discord.Player-Leave-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Leave-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server), false);

                    }
                } else {

                    if (!messages.getString("Discord.Player-Leave").isEmpty()) {

                        Discord.playerLeave(player, messages.getString("Discord.Player-Leave").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server), false);

                    }
                }
            }

            // External
            if (isExternal && External.isConnected()) {

                try {

                    ExternalData.playerLeave(serverName, playerName, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && SQLite.isConnected()) {

                try {

                    SQLiteData.insertPlayerLeave(serverName, playerName, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
