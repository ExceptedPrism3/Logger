package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.database.external.ExternalData;
import me.prism3.loggervelocity.database.sqlite.SQLiteData;
import me.prism3.loggervelocity.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnChat{

    @Subscribe
    public void onChat(final PlayerChatEvent event) {

        final Main main = Main.getInstance();
        final Player player = event.getPlayer();

        if (main.getConfig().getBoolean("Log-Player.Chat") && player.getCurrentServer().isPresent()) {

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getUsername();
            final String server = player.getCurrentServer().get().getServerInfo().getName();
            final String message = event.getMessage().replace("\\", "\\\\");

            // Log To Files
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!main.getMessages().getString("Discord.Player-Chat-Staff").isEmpty()) {

                        main.getDiscord().staffChat(player, main.getMessages().getString("Discord.Player-Chat-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%msg%", message), false);

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(main.getMessages().getString("Files.Player-Chat-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%msg%", message) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && main.getExternal().isConnected()) {

                        ExternalData.playerChat(serverName, playerName, message, true);

                    }

                    if (isSqlite && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerChat(serverName, playerName, message, true);

                    }

                    return;

                }

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true));
                    out.write(main.getMessages().getString("Files.Player-Chat").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%msg%", message) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!main.getMessages().getString("Discord.Player-Chat-Staff").isEmpty()) {

                        main.getDiscord().staffChat(player, main.getMessages().getString("Discord.Player-Chat-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%msg%", message), false);

                    }
                } else {

                    if (!main.getMessages().getString("Discord.Player-Chat").isEmpty()) {

                        main.getDiscord().playerChat(player, main.getMessages().getString("Discord.Player-Chat").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%msg%", message), false);

                    }
                }
            }

            // External
            if (isExternal && main.getExternal().isConnected()) {

                try {

                    ExternalData.playerChat(serverName, playerName, message, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerChat(serverName, playerName, message, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
