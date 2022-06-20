package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
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

public class OnLeave {

    @Subscribe
    public void onQuit(final DisconnectEvent event) {

        final Main main = Main.getInstance();

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

                    if (!main.getMessages().getString("Discord.Player-Leave-Staff").isEmpty()) {

                        main.getDiscord().staffChat(player, main.getMessages().getString("Discord.Player-Leave-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server), false);

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(main.getMessages().getString("Files.Player-Leave-Staff").replace("%server%", server).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && main.getExternal().isConnected()) {

                        ExternalData.playerLeave(serverName, playerName, true);

                    }

                    if (isSqlite && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerLeave(serverName, playerName, true);

                    }

                    return;
                }

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeaveLogFile(), true));
                    out.write(main.getMessages().getString("Files.Player-Leave").replace("%server%", server).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!main.getMessages().getString("Discord.Player-Leave-Staff").isEmpty()) {

                        main.getDiscord().staffChat(player, main.getMessages().getString("Discord.Player-Leave-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server), false);

                    }
                } else {

                    if (!main.getMessages().getString("Discord.Player-Leave").isEmpty()) {

                        main.getDiscord().playerLeave(player, main.getMessages().getString("Discord.Player-Leave").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server), false);

                    }
                }
            }

            // External
            if (isExternal && main.getExternal().isConnected()) {

                try {

                    ExternalData.playerLeave(serverName, playerName, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerLeave(serverName, playerName, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
