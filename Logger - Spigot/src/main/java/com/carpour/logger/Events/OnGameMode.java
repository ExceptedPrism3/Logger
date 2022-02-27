package com.carpour.logger.Events;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class OnGameMode implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameMode(final PlayerGameModeChangeEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Game-Mode")) {

            final String gameMode = gameModeConf;
            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt) || gameMode.isEmpty()) return;

            if (event.getNewGameMode() == GameMode.valueOf(gameMode.toUpperCase())) {

                final String playerName = player.getName();
                final World world = player.getWorld();
                final String worldName = world.getName();

                // Log To Files Handling
                if (isLogToFiles) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Game-Mode-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Game-Mode-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%game-mode%", gameMode), false);

                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                            out.write(Objects.requireNonNull(Messages.get().getString("Files.Game-Mode-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%game-mode%", gameMode) + "\n");
                            out.close();

                        } catch (IOException e) {

                            this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (isExternal && this.main.getExternal().isConnected()) {

                            ExternalData.gameMode(serverName, worldName, playerName, gameMode, true);

                        }

                        if (isSqlite && this.main.getSqLite().isConnected()) {

                            SQLiteData.insertGameMode(serverName, worldName, playerName, gameMode, true);

                        }

                        return;

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getGameModeFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Game-Mode")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%game-mode%", gameMode) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord Integration
                if (!player.hasPermission(loggerExemptDiscord)) {

                    if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Game-Mode-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Game-Mode-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%game-mode%", gameMode), false);

                        }
                    } else {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Game-Mode")).isEmpty()) {

                            Discord.gameMode(player, Objects.requireNonNull(Messages.get().getString("Discord.Game-Mode")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%game-mode%", gameMode), false);
                        }
                    }
                }

                // MySQL Handling
                if (isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.gameMode(serverName, worldName, playerName, gameMode, player.hasPermission(loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite Handling
                if (isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertGameMode(serverName, worldName, playerName, gameMode, player.hasPermission(loggerStaffLog));

                    } catch (Exception exception) { exception.printStackTrace(); }
                }
            }
        }
    }
}
