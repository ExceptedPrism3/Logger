package me.prism3.loggerbungeecord.events;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.ZonedDateTime;
import java.util.UUID;

public class OnLogin implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onLogging(final PostLoginEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Login")) {

            final ProxiedPlayer player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt)) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            InetSocketAddress playerIP = (InetSocketAddress) event.getPlayer().getSocketAddress();

            if (!Data.isPlayerIP) playerIP = null;

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(this.main.getMessages().getString("Files.Player-Login-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%IP%", String.valueOf(playerIP)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLoginLogFile(), true));
                        out.write(this.main.getMessages().getString("Files.Player-Login").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%IP%", String.valueOf(playerIP)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().getString("Discord.Player-Login-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().getString("Discord.Player-Login-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%IP%", String.valueOf(playerIP)), false);

                    }
                } else {

                    if (!this.main.getMessages().getString("Discord.Player-Login").isEmpty()) {

                        this.main.getDiscord().playerLogin(playerName, playerUUID, this.main.getMessages().getString("Discord.Player-Login").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%IP%", String.valueOf(playerIP)), false);

                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {//TODO check

                    Main.getInstance().getDatabase().insertPlayerLogin(Data.serverName, playerName, playerUUID.toString(), playerIP, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertPlayerLogin(Data.serverName, playerName, playerUUID.toString(), playerIP, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
