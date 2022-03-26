package me.prism3.logger.Events;

import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.Utils.FileHandler;
import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Database.SQLite.Global.SQLiteData;
import me.prism3.logger.Utils.Messages;
import me.prism3.logger.Utils.Data;
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

public class OnPlayerLevel implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelChange(final PlayerLevelChangeEvent event) {

        if (this.main.getConfig().getBoolean("Log-Player.Level")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt)) return;

            final String playerName = player.getName();
            final int logAbove = Data.abovePlayerLevel;
            final double playerLevel = event.getNewLevel();

            if (playerLevel == logAbove) {

                // Log To Files
                if (Data.isLogToFiles) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%level%", String.valueOf(logAbove)), false);
                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                            out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Level-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%level%", String.valueOf(logAbove)) + "\n");
                            out.close();

                        } catch (IOException e) {

                            this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (Data.isExternal && this.main.getExternal().isConnected()) {

                            ExternalData.levelChange(Data.serverName, playerName, true);

                        }

                        if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                            SQLiteData.insertLevelChange(Data.serverName, player, true);

                        }

                        return;
                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerLevelFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Level")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%player%", playerName).replaceAll("%level%", String.valueOf(logAbove)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!player.hasPermission(Data.loggerExemptDiscord)) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Level-Staff")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%level%", String.valueOf(logAbove)), false);

                        }
                    } else {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Level")).isEmpty()) {

                            Discord.playerLevel(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Level")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%level%", String.valueOf(logAbove)), false);
                        }
                    }
                }

                // External
                if (Data.isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.levelChange(Data.serverName, playerName, player.hasPermission(Data.loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertLevelChange(Data.serverName, player, player.hasPermission(Data.loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
