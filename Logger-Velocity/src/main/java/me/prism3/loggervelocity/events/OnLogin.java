package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.database.external.ExternalData;
import me.prism3.loggervelocity.database.sqlite.SQLiteData;
import me.prism3.loggervelocity.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnLogin {

    @Subscribe
    public void onJoin(final PostLoginEvent event) {

        final Main main = Main.getInstance();

        if (main.getConfig().getBoolean("Log-Player.Login")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getUsername();
            InetSocketAddress playerIP = player.getRemoteAddress();

            if (!isPlayerIP) playerIP = null;

            // Log To Files
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!main.getMessages().getString("Discord.Player-Login-Staff").isEmpty()) {

                        main.getDiscord().staffChat(player, main.getMessages().getString("Discord.Player-Login-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%IP%", String.valueOf(playerIP)), false);

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(main.getMessages().getString("Files.Player-Login-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%IP%", String.valueOf(playerIP)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && main.getExternal().isConnected()) {

                        ExternalData.playerLogin(serverName, playerName, playerIP, true);

                    }

                    if (isSqlite && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerLogin(serverName, playerName, playerIP, true);

                    }

                    return;

                }

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLoginLogFile(), true));
                    out.write(main.getMessages().getString("Files.Player-Login").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%IP%", String.valueOf(playerIP)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!main.getMessages().getString("Discord.Player-Login-Staff").isEmpty()) {

                        main.getDiscord().staffChat(player, main.getMessages().getString("Discord.Player-Login-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%IP%", String.valueOf(playerIP)), false);

                    }
                } else {

                    if (!main.getMessages().getString("Discord.Player-Login").isEmpty()) {

                        main.getDiscord().playerLogin(player, main.getMessages().getString("Discord.Player-Login").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%IP%", String.valueOf(playerIP)), false);

                    }
                }
            }

            // External
            if (isExternal && main.getExternal().isConnected()) {

                try {

                    ExternalData.playerLogin(serverName, playerName, playerIP, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerLogin(serverName, playerName, playerIP, player.hasPermission(loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
