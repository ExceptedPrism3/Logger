package com.carpour.logger.Events.PluginDependent;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import net.ess3.api.events.AfkStatusChangeEvent;
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


public class OnAFK implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void afk(final AfkStatusChangeEvent e) {

        if (!e.isCancelled() && this.main.getConfig().getBoolean("Log-Extras.Essentials-AFK")
                && !e.getAffected().isAfk()) {

            final Player player = e.getAffected().getBase();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final int x = player.getLocation().getBlockX();
            final int y = player.getLocation().getBlockY();
            final int z = player.getLocation().getBlockZ();
            final String worldName = player.getWorld().getName();

            // Log To Files Handling
            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.AFK-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException event) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        event.printStackTrace();

                    }

                    if (isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.afk(serverName, worldName, playerName, x, y, z, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertAFK(serverName, player, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAfkFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Extras.AFK")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                    out.close();

                } catch (IOException event) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    event.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK")).isEmpty()) {

                        Discord.afk(player, Objects.requireNonNull(Messages.get().getString("Discord.Extras.AFK")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);
                    }
                }
            }

            // MySQL
            if (isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.afk(serverName, worldName, playerName, x, y, z, player.hasPermission(loggerStaffLog));

                } catch (Exception event) { event.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertAFK(serverName, player, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
