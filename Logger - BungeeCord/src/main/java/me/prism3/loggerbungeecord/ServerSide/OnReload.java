package me.prism3.loggerbungeecord.ServerSide;

import me.prism3.loggerbungeecord.Database.External.ExternalData;
import me.prism3.loggerbungeecord.Database.SQLite.SQLiteData;
import me.prism3.loggerbungeecord.Discord.Discord;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.Utils.FileHandler;
import me.prism3.loggerbungeecord.Utils.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static me.prism3.loggerbungeecord.Utils.Data.*;

public class OnReload implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onServerReload(final ProxyReloadEvent event){

        if (this.main.getConfig().getBoolean("Log-Server.Reload")) {

            final CommandSender cS = event.getSender();

            if (cS instanceof ProxiedPlayer) {

                final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

                if (player.hasPermission(loggerExempt)) return;

                final String playerName = player.getName();
                final String server = player.getServer().getInfo().getName();

                // Log To Files
                if (isLogToFiles) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!Messages.getString("Discord.Server-Reload-Player-Staff").isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Server-Reload-Player-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server), false);

                        }

                        try {

                            final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                            out.write(Messages.getString("Files.Server-Reload-Player-Staff-").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName) + "\n");
                            out.close();

                        } catch (IOException e) {

                            Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (isExternal && this.main.getExternal().isConnected()) {

                            ExternalData.serverReload(serverName, playerName, true);

                        }

                        if (isSqlite && this.main.getSqLite().isConnected()) {

                            SQLiteData.insertServerReload(serverName, playerName, true);

                        }

                        return;

                    }

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getReloadLogFile(), true));
                        out.write(Messages.getString("Files.Server-Reload-Player").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server).replaceAll("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord Integration
                if (!player.hasPermission(loggerExemptDiscord)) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!Messages.getString("Discord.Server-Reload-Player-Staff").isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.getString("Discord.Server-Reload-Player-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server), false);

                        }
                    } else {

                        if (!Messages.getString("Discord.Server-Reload-Player").isEmpty()) {

                            Discord.serverReload(playerName, Objects.requireNonNull(Messages.getString("Discord.Server-Reload-Player")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%server%", server), false);

                        }
                    }
                }

                // External
                if (isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.serverReload(serverName, playerName, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertServerReload(serverName, playerName, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

            } else {

                // File Logging
                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getReloadLogFile(), true));
                    out.write(Messages.getString("Files.Server-Side.Reload-Console").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                // Discord
                if (!Messages.getString("Discord.Server-Side.Restart-Console").isEmpty()) {

                    Discord.serverReload(null, Objects.requireNonNull(Messages.getString("Discord.Server-Side.Restart-Console")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);

                }

                // External
                if (isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.serverReload(serverName, null, true);

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertServerReload(serverName, null, true);

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
