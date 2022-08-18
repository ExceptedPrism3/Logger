package me.prism3.loggervelocity.events;

import com.carpour.loggercore.database.data.Coordinates;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnLeave {

    @Subscribe
    public void onQuit(final DisconnectEvent event) {

        final Main main = Main.getInstance();

        final Player player = event.getPlayer();

        if (player.hasPermission(loggerExempt)) return;

        final String playerName = player.getUsername();
        final UUID playerUUID = player.getUniqueId();

        // This resolves an error showing up if the targeted server is offline whist connecting
        if (!player.getCurrentServer().isPresent()) return;

        final String server = player.getCurrentServer().get().getServerInfo().getName();

        // Log To Files
        if (isLogToFiles) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                    out.write(main.getMessages().getString("Files.Player-Leave-Staff").replace("%server%", server).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            } else {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeaveLogFile(), true));
                    out.write(main.getMessages().getString("Files.Player-Leave").replace("%server%", server).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }
        }

        // Discord
        if (!player.hasPermission(loggerExemptDiscord)) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                if (!main.getMessages().getString("Discord.Player-Leave-Staff").isEmpty()) {

                    main.getDiscord().staffChat(playerName, playerUUID, main.getMessages().getString("Discord.Player-Leave-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server), false);

                }
            } else {

                if (!main.getMessages().getString("Discord.Player-Leave").isEmpty()) {

                    main.getDiscord().playerLeave(playerName, playerUUID, main.getMessages().getString("Discord.Player-Leave").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server), false);

                }
            }
        }

        // External
        if (isExternal) {

            try {

                Main.getInstance().getDatabase().insertPlayerLeave(serverName, playerName, playerUUID.toString(), new Coordinates(), player.hasPermission(loggerStaffLog));

            } catch (Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (isSqlite) {

            try {

                Main.getInstance().getSqLite().insertPlayerLeave(serverName, playerName, playerUUID.toString(), new Coordinates(), player.hasPermission(loggerStaffLog));

            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
