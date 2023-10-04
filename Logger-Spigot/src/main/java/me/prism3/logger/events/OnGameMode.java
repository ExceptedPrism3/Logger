package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
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

public class OnGameMode implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameMode(final PlayerGameModeChangeEvent event) {

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Game-Mode")) {

            final String gameMode = Data.gameModeConf;
            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || gameMode.isEmpty() || BedrockChecker.isBedrock(player.getUniqueId())) return;

            if (event.getNewGameMode() == GameMode.valueOf(gameMode.toUpperCase())) {

                final String playerName = player.getName();
                final World world = player.getWorld();
                final String worldName = world.getName();

                // Log To Files
                if (Data.isLogToFiles) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Game-Mode-Staff")).isEmpty()) {

                            this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Game-Mode-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%game-mode%", gameMode), false);

                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                            out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Game-Mode-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%game-mode%", gameMode) + "\n");
                            out.close();

                        } catch (IOException e) {

                            Log.warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (Data.isExternal && this.main.getExternal().isConnected()) {

                            ExternalData.gameMode(Data.serverName, player, gameMode, true);

                        }

                        if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                            SQLiteData.insertGameMode(Data.serverName, player, gameMode, true);

                        }

                        return;

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getGameModeFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Game-Mode")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%game-mode%", gameMode) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord Integration
                if (!player.hasPermission(Data.loggerExemptDiscord)) {

                    if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                        if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Game-Mode-Staff")).isEmpty()) {

                            this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Game-Mode-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%game-mode%", gameMode), false);

                        }
                    } else {

                        if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Game-Mode")).isEmpty()) {

                            this.main.getDiscord().gameMode(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Game-Mode")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%game-mode%", gameMode), false);
                        }
                    }
                }

                // External
                if (Data.isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.gameMode(Data.serverName, player, gameMode, player.hasPermission(Data.loggerStaffLog));

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // External
                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertGameMode(Data.serverName, player, gameMode, player.hasPermission(Data.loggerStaffLog));

                    } catch (Exception exception) { exception.printStackTrace(); }
                }
            }
        }
    }
}
