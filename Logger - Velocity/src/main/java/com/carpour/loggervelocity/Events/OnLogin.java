package com.carpour.loggervelocity.Events;

import com.carpour.loggervelocity.Database.External.External;
import com.carpour.loggervelocity.Database.External.ExternalData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.FileHandler;
import com.carpour.loggervelocity.Utils.Messages;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.ZonedDateTime;

import static com.carpour.loggervelocity.Utils.Data.*;

public class OnLogin {

    @Subscribe
    public void onJoin(final PostLoginEvent event) {

        final Main main = Main.getInstance();
        final Messages messages = new Messages();

        if (main.getConfig().getBoolean("Log-Player.Login")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getUsername();
            InetSocketAddress playerIP = player.getRemoteAddress();

            if (!isPlayerIP) playerIP = null;

            // Log To Files
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!messages.getString("Discord.Player-Login-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Login-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%IP%", String.valueOf(playerIP)), false);

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(messages.getString("Files.Player-Login-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%IP%", String.valueOf(playerIP)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && External.isConnected()) {

                        ExternalData.playerLogin(serverName, playerName, playerIP, true);

                    }

                    if (isSqlite && SQLite.isConnected()) {

                        SQLiteData.insertPlayerLogin(serverName, playerName, playerIP, true);

                    }

                    return;

                }

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLoginLogFile(), true));
                    out.write(messages.getString("Files.Player-Login").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%IP%", String.valueOf(playerIP)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!messages.getString("Discord.Player-Login-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Login-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%IP%", String.valueOf(playerIP)), false);

                    }
                } else {

                    if (!messages.getString("Discord.Player-Login").isEmpty()) {

                        Discord.playerLogin(player, messages.getString("Discord.Player-Login").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%IP%", String.valueOf(playerIP)), false);

                    }
                }
            }

            // External
            if (isExternal && External.isConnected()) {

                try {

                    ExternalData.playerLogin(serverName, playerName, playerIP, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && SQLite.isConnected()) {

                try {

                    SQLiteData.insertPlayerLogin(serverName, playerName, playerIP, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
