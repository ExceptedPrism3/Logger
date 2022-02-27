package com.carpour.loggerbungeecord.Events;

import com.carpour.loggerbungeecord.Database.External.ExternalData;
import com.carpour.loggerbungeecord.Database.SQLite.SQLiteData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.loggerbungeecord.Utils.Data.*;

public class OnLeave implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onQuit(final PlayerDisconnectEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Leave")) {

            final ProxiedPlayer player = event.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            String playerName = player.getName();

            // This resolves an error showing up if the targeted server is offline whist connecting
            if (player.getServer() == null) return;

            final String playerServerName = player.getServer().getInfo().getName();

            if (player.getServer() == null || playerServerName == null) return;

            // Log To Files Handling
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Messages.getString("Discord.Player-Leave-Staff").isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Leave-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", playerServerName), false);

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(Messages.getString("Files.Player-Leave-Staff").replaceAll("%server%", playerServerName).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.playerLeave(serverName, playerName, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerLeave(serverName, playerName, true);

                    }

                    return;

                }

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeaveLogFile(), true));
                    out.write(Messages.getString("Files.Player-Leave").replaceAll("%server%", playerServerName).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Messages.getString("Discord.Player-Leave-Staff").isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Player-Leave-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", playerServerName), false);

                    }
                } else {

                    if (!Messages.getString("Discord.Player-Leave").isEmpty()) {

                        Discord.playerLeave(player, Objects.requireNonNull(Messages.getString("Discord.Player-Leave")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", playerServerName), false);

                    }
                }
            }

            // External Handling
            if (isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.playerLeave(serverName, playerName, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite Handling
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerLeave(serverName, playerName, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
