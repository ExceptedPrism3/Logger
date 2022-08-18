package me.prism3.loggerbungeecord.events;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.loggerbungeecord.utils.Data.loggerExempt;

public class OnServerSwitch implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onServerSwitch(final ServerSwitchEvent event) {

        // If the player just joined, and DIDN'T switched from server.
        if (event.getFrom() == null) return;

        final ProxiedPlayer player = event.getPlayer();

        if (player.hasPermission(loggerExempt)) return;

        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final String from = player.getServer().getInfo().getName();
        final String destination = player.getServer().getInfo().getName();

        // We need to use a timer to get the new server, otherwise it will return the previous server!
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {



                // I only did discord for now

        if (Data.isLogToFiles) {

            if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffLogFile(), true));
                    out.write(this.main.getMessages().getString("Files.Server-Switch-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%from%", from).replace("%destination%", destination) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            } else {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerSwitchLogFile(), true));
                    out.write(this.main.getMessages().getString("Files.Server-Switch").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%from%", from).replace("%destination%", destination) + "\n");
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

                if (!this.main.getMessages().getString("Discord.Server-Switch-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().getString("Discord.Server-Switch-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%from%", from).replace("%destination%", destination), false);

                }
            } else {

                if (!this.main.getMessages().getString("Discord.Server-Switch").isEmpty()) {

                    this.main.getDiscord().serverSwitch(playerName, playerUUID, this.main.getMessages().getString("Discord.Server-Switch").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%player%", playerName).replace("%from%", from).replace("%destination%", destination), false);
                }
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertServerSwitch(Data.serverName, playerUUID.toString(), playerName, from, destination, player.hasPermission(Data.loggerStaffLog));

            } catch (Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertServerSwitch(Data.serverName, playerUUID.toString(), playerName, from, destination, player.hasPermission(Data.loggerStaffLog));

            } catch (Exception e) { e.printStackTrace(); }
        }
//        }, 0, 500); // 0,5s
    }
}


