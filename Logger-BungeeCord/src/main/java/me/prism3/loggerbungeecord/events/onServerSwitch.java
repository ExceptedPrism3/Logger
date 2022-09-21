package me.prism3.loggerbungeecord.events;

import me.prism3.loggerbungeecord.utils.Data;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class onServerSwitch implements Listener {

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        Player player = event.getPlayer();

        // If the player just joined, and DIDN'T switched from server.
        if (event.getFrom() == null) {
            return;
        }

        // We need to use a timer to get the new server, otherwise it will return the previous server!
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Server from = event.getPlayer().getServer().getInfo().getName();
                Server destination = event.getPlayer().getServer().getInfo().getName();

                // I only did discord for now

                // Discord Integration
                if (!player.hasPermission(Data.loggerExemptDiscord)) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!this.main.getMessages().getString("Discord.Player-ServerSwitch-Staff").isEmpty()) {

                            this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().getString("Discord.Player-ServerSwitch-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%from%", from).replace("%destination%", destination), false);

                        } else {
                        }

                        if (!this.main.getMessages().getString("Discord.Player-ServerSwitch").isEmpty()) {

                            this.main.getDiscord().playerChat(player, Objects.requireNonNull(this.main.getMessages().getString("Discord.Player-ServerSwitch")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%from%", from).replace("%destination%", destination), false);
                        }
                    }
                }

            }
        }, 0, 500); // 0,5s
    }
}


