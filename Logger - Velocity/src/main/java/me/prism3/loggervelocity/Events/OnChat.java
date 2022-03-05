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
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.Utils.Data.*;

public class OnChat{

    @Subscribe
    public void onChat(final PlayerChatEvent event){

        final Main main = Main.getInstance();
        final Messages messages = new Messages();
        final Player player = event.getPlayer();

        if (main.getConfig().getBoolean("Log-Player.Chat") && player.getCurrentServer().isPresent()) {

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getUsername();
            final String server = String.valueOf(player.getCurrentServer().get().getServerInfo().getName());
            final String message = event.getMessage().replace("\\", "\\\\");

            // Log To Files
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!messages.getString("Discord.Player-Chat-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Chat-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%msg%", message), false);

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(messages.getString("Files.Player-Chat-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%msg%", message) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && External.isConnected()) {

                        ExternalData.playerChat(serverName, playerName, message, true);

                    }

                    if (isSqlite && SQLite.isConnected()) {

                        SQLiteData.insertPlayerChat(serverName, playerName, message, true);

                    }

                    return;

                }

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true));
                    out.write(messages.getString("Files.Player-Chat").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName).replaceAll("%msg%", message) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!messages.getString("Discord.Player-Chat-Staff").isEmpty()) {

                        Discord.staffChat(player, messages.getString("Discord.Player-Chat-Staff").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%msg%", message), false);

                    }
                } else {

                    if (!messages.getString("Discord.Player-Chat").isEmpty()) {

                        Discord.playerChat(player, messages.getString("Discord.Player-Chat").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%msg%", message), false);

                    }
                }
            }

            // External
            if (isExternal && External.isConnected()) {

                try {

                    ExternalData.playerChat(serverName, playerName, message, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && SQLite.isConnected()) {

                try {

                    SQLiteData.insertPlayerChat(serverName, playerName, message, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
