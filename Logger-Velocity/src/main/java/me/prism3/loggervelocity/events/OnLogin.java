package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.utils.FileHandler;
import me.prism3.loggervelocity.utils.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnLogin {

    @Subscribe
    public void onJoin(final PostLoginEvent event) {

        final Main main = Main.getInstance();

        final Player player = event.getPlayer();

        if (player.hasPermission(loggerExempt)) return;

        final String playerName = player.getUsername();
        final UUID playerUUID = player.getUniqueId();
        InetSocketAddress playerIP = player.getRemoteAddress();
        if (!isPlayerIP) playerIP = null;

        // Log To Files
        if (isLogToFiles) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true))) {

                    out.write(main.getMessages().getString("Files.Player-Login-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%IP%", String.valueOf(playerIP)) + "\n");

                } catch (final IOException e) {

                    Log.error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLoginLogFile(), true))) {

                    out.write(main.getMessages().getString("Files.Player-Login").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%IP%", String.valueOf(playerIP)) + "\n");

                } catch (final IOException e) {

                    Log.error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }
        }

        // Discord
        if (!player.hasPermission(loggerExemptDiscord)) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                if (!main.getMessages().getString("Discord.Player-Login-Staff").isEmpty()) {

                    main.getDiscord().staffChat(playerName, playerUUID, main.getMessages().getString("Discord.Player-Login-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%IP%", String.valueOf(playerIP)), false);

                }
            } else {

                if (!main.getMessages().getString("Discord.Player-Login").isEmpty()) {

                    main.getDiscord().playerLogin(playerName, playerUUID, main.getMessages().getString("Discord.Player-Login").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%IP%", String.valueOf(playerIP)), false);

                }
            }
        }

        // External
        if (isExternal) {

            try {

                Main.getInstance().getDatabase().insertPlayerLogin(serverName, playerName, playerUUID.toString(), playerIP, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (isSqlite) {

            try {

                Main.getInstance().getSqLite().insertPlayerLogin(serverName, playerName, playerUUID.toString(), playerIP, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
