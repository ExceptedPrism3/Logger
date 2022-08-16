package me.prism3.loggerbungeecord.events;

import com.carpour.loggercore.database.entity.Coordinates;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

public class OnLeave implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onQuit(final PlayerDisconnectEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Leave")) {

            final ProxiedPlayer player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt)) return;

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();

            // This resolves an error showing up if the targeted server is offline whist connecting
            if (player.getServer() == null) return;

            final String playerServerName = player.getServer().getInfo().getName();

            if (player.getServer() == null || playerServerName == null) return;

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                        out.write(this.main.getMessages().getString("Files.Player-Leave-Staff").replace("%server%", playerServerName).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                } else {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getLeaveLogFile(), true));
                        out.write(this.main.getMessages().getString("Files.Player-Leave").replace("%server%", playerServerName).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName) + "\n");
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

                    if (!this.main.getMessages().getString("Discord.Player-Leave-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().getString("Discord.Player-Leave-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", playerServerName), false);

                    }
                } else {

                    if (!this.main.getMessages().getString("Discord.Player-Leave").isEmpty()) {

                        this.main.getDiscord().playerLeave(playerName, playerUUID, this.main.getMessages().getString("Discord.Player-Leave").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%server%", playerServerName), false);

                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertPlayerLeave(Data.serverName, playerName,playerUUID.toString(), new Coordinates(), player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertPlayerLeave(Data.serverName, playerName,playerUUID.toString(),new Coordinates(), player.hasPermission(Data.loggerStaffLog));

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
