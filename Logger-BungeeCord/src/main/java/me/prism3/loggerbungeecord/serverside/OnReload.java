package me.prism3.loggerbungeecord.serverside;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.FileHandler;
import me.prism3.loggerbungeecord.utils.Log;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.loggerbungeecord.utils.Data.*;

public class OnReload implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerReload(final ProxyReloadEvent event) {

        final CommandSender cS = event.getSender();

        if (cS instanceof ProxiedPlayer) {

            final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final String server = player.getServer().getInfo().getName();

            // Log To Files
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true))) {

                        out.write(this.main.getMessages().getString("Files.Server-Reload-Player-Staff-").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getReloadLogFile(), true))) {

                        out.write(this.main.getMessages().getString("Files.Server-Reload-Player").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%player%", playerName).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().getString("Discord.Server-Reload-Player-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().getString("Discord.Server-Reload-Player-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%uuid%", playerUUID.toString()), false);
                    }
                } else {

                    if (!this.main.getMessages().getString("Discord.Server-Reload-Player").isEmpty()) {

                        this.main.getDiscord().serverReload(playerName, this.main.getMessages().getString("Discord.Server-Reload-Player").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", server).replace("%uuid%", playerUUID.toString()), false);
                    }
                }
            }

            // External
            if (isExternal) {

                try {

                    Main.getInstance().getDatabase().insertServerReload(serverName, playerName, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertServerReload(serverName, playerName, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

        } else {

            // File Logging
            try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getReloadLogFile(), true))) {

                out.write(this.main.getMessages().getString("Files.Server-Side.Reload-Console").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");

            } catch (final IOException e) {

                Log.severe("An error occurred while logging into the appropriate file.");
                e.printStackTrace();
            }

            // Discord
            if (!this.main.getMessages().getString("Discord.Server-Side.Restart-Console").isEmpty() && this.main.getDiscordFile().getBoolean("Discord.Enable"))
                this.main.getDiscord().serverReload(null, this.main.getMessages().getString("Discord.Server-Side.Restart-Console").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);

            // External
            if (isExternal) {

                try {

                    Main.getInstance().getDatabase().insertServerReload(serverName, null, true);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertServerReload(serverName, null, true);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
