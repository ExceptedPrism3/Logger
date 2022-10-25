package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnGameMode implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameMode(final PlayerGameModeChangeEvent event) {

        if (!event.isCancelled()) {

            final String gameMode = Data.gameModeConf;
            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || gameMode.isEmpty() || BedrockChecker.isBedrock(player.getUniqueId())) return;

            if (event.getNewGameMode() == GameMode.valueOf(gameMode.toUpperCase())) {

                final String playerName = player.getName();
                final UUID playerUUID = player.getUniqueId();
                final String worldName = player.getWorld().getName();

                // Log To Files
                if (Data.isLogToFiles) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                            out.write(this.main.getMessages().get().getString("Files.Game-Mode-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%game-mode%", gameMode).replace("%uuid%", playerUUID.toString()) + "\n");

                        } catch (final IOException e) {

                            Log.warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();
                        }
                    } else {

                        try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getGameModeFile(), true))) {

                            out.write(this.main.getMessages().get().getString("Files.Game-Mode").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%game-mode%", gameMode).replace("%uuid%", playerUUID.toString()) + "\n");

                        } catch (final IOException e) {

                            Log.warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();
                        }
                    }
                }

                // Discord Integration
                if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!this.main.getMessages().get().getString("Discord.Game-Mode-Staff").isEmpty()) {

                            this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Game-Mode-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%game-mode%", gameMode).replace("%uuid%", playerUUID.toString()), false);
                        }
                    } else {

                        if (!this.main.getMessages().get().getString("Discord.Game-Mode").isEmpty()) {

                            this.main.getDiscord().gameMode(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Game-Mode").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%game-mode%", gameMode).replace("%uuid%", playerUUID.toString()), false);
                        }
                    }
                }

                // External
                if (Data.isExternal) {

                    try {

                        Main.getInstance().getQueueManager().queueGameMode(Data.serverName, playerName, playerUUID.toString(), gameMode, worldName, player.hasPermission(loggerStaffLog));

                    } catch (final Exception e) { e.printStackTrace(); }
                }

                // External
                if (Data.isSqlite) {

                    try {

                        Main.getInstance().getQueueManager().queueGameMode(Data.serverName, playerName, playerUUID.toString(), gameMode, worldName, player.hasPermission(loggerStaffLog));

                    } catch (final Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
