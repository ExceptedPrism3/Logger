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
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class OnGameMode implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameMode(PlayerGameModeChangeEvent event) {

        Player player = event.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        String worldName = world.getName();
        String gameMode = Objects.requireNonNull(main.getConfig().getString("Game-Mode"));
        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (player.hasPermission("logger.exempt") || gameMode.isEmpty()) return;

        if (!event.isCancelled() && main.getConfig().getBoolean("Log-Player.Game-Mode")) {

            if (event.getNewGameMode() == GameMode.valueOf(gameMode.toUpperCase())) {

                // Log To Files Handling
                if (main.getConfig().getBoolean("Log-to-Files")) {

                    if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                        if (!Objects.requireNonNull(Messages.get().getString("Discord.Game-Mode-Staff")).isEmpty()) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Game-Mode-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%game-mode%", gameMode), false);

                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                            out.write(Objects.requireNonNull(Messages.get().getString("Files.Game-Mode-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%game-mode%", gameMode) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                            ExternalData.gameMode(serverName, worldName, playerName, gameMode, true);

                        }

                        if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                            SQLiteData.insertGameMode(serverName, worldName, playerName, gameMode, true);

                        }

                        return;

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getGameModeFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Game-Mode")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%game-mode%", gameMode) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord Integration
                if (!player.hasPermission("logger.exempt.discord")) {

                    if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

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
                if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                    try {

                        ExternalData.gameMode(serverName, worldName, playerName, gameMode, player.hasPermission("logger.staff.log"));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite Handling
                if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertGameMode(serverName, worldName, playerName, gameMode, player.hasPermission("logger.staff.log"));

                    } catch (Exception exception) { exception.printStackTrace(); }
                }
            }
        }
    }
}
