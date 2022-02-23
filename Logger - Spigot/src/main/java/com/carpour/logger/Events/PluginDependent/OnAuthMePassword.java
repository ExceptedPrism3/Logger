package com.carpour.logger.Events.PluginDependent;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import fr.xephi.authme.events.FailedLoginEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class OnAuthMePassword implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void password(final FailedLoginEvent e) {

        if (this.main.getConfig().getBoolean("Log-Extras.AuthMe-Wrong-Password")) {

            final Player player = e.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final String worldName = player.getWorld().getName();

            // Log To Files Handling
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.Wrong-Password-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.Wrong-Password-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName), false);
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.Wrong-Password-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName) + "\n");
                        out.close();

                    } catch (IOException event) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        event.printStackTrace();

                    }

                    if (isExternal && this.main.external.isConnected()) {

                        ExternalData.wrongPassword(serverName, worldName, playerName, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertWrongPassword(serverName, player, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getWrongPasswordFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.Wrong-Password")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName) + "\n");
                    out.close();

                } catch (IOException event) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    event.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.Wrong-Password-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.Wrong-Password-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.Wrong-Password")).isEmpty()) {

                        Discord.wrongPassword(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.Wrong-Password")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName), false);
                    }
                }
            }

            // MySQL
            if (isExternal && this.main.external.isConnected()) {

                try {

                    ExternalData.wrongPassword(serverName, worldName, playerName, player.hasPermission(loggerStaffLog));

                } catch (Exception event) { event.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertWrongPassword(serverName, player, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
