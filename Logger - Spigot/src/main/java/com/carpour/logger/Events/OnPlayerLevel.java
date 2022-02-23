package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class OnPlayerLevel implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelChange(final PlayerLevelChangeEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Level")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt)) return;

            final String playerName = player.getName();
            final int logAbove = abovePlayerLevel;
            final double playerLevel = event.getNewLevel();

            if (playerLevel == logAbove) {

                // Log To Files Handling
                if (isLogToFiles) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%level%", String.valueOf(logAbove)), false);
                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                            out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Level-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%level%", String.valueOf(logAbove)) + "\n");
                            out.close();

                        } catch (IOException e) {

                            this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (isExternal && this.main.external.isConnected()) {

                            ExternalData.levelChange(serverName, playerName, true);

                        }

                        if (isSqlite && this.main.getSqLite().isConnected()) {

                            SQLiteData.insertLevelChange(serverName, player, true);

                        }

                        return;
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerLevelFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Level")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%level%", String.valueOf(logAbove)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!player.hasPermission(loggerExemptDiscord)) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%level%", String.valueOf(logAbove)), false);

                        }
                    } else {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Level")).isEmpty()) {

                            Discord.playerLevel(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Level")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%level%", String.valueOf(logAbove)), false);
                        }
                    }
                }

                // MySQL
                if (isExternal && this.main.external.isConnected()) {

                    try {

                        ExternalData.levelChange(serverName, playerName, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertLevelChange(serverName, player, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
