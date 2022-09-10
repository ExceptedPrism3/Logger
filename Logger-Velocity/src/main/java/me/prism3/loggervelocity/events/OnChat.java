package me.prism3.loggervelocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.utils.FileHandler;
import me.prism3.loggervelocity.utils.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.loggervelocity.utils.Data.*;

public class OnChat {

    @Subscribe
    public void onChat(final PlayerChatEvent event) {

        final Main main = Main.getInstance();

        if (event.getPlayer().getCurrentServer().isPresent()) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getUsername();
            final UUID playerUUID = player.getUniqueId();
            final String server = player.getCurrentServer().get().getServerInfo().getName();
            final String message = event.getMessage().replace("\\", "\\\\");

            // Log To Files
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true))) {

                        out.write(main.getMessages().getString("Files.Player-Chat-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%msg%", message) + "\n");

                    } catch (final IOException e) {

                        Log.error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true))) {

                        out.write(main.getMessages().getString("Files.Player-Chat").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%msg%", message) + "\n");

                    } catch (final IOException e) {

                        Log.error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!main.getMessages().getString("Discord.Player-Chat-Staff").isEmpty()) {

                        main.getDiscord().staffChat(playerName, playerUUID, main.getMessages().getString("Discord.Player-Chat-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%msg%", message), false);

                    }
                } else {

                    if (!main.getMessages().getString("Discord.Player-Chat").isEmpty()) {

                        main.getDiscord().playerChat(playerName, playerUUID, main.getMessages().getString("Discord.Player-Chat").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%msg%", message), false);

                    }
                }
            }

            // External
            if (isExternal) {

                try {

                    Main.getInstance().getDatabase().insertPlayerChat(serverName, playerName, playerUUID.toString(), null, message, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertPlayerChat(serverName, playerName, playerUUID.toString(), null, message, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
