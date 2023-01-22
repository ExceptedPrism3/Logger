package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnGameMode implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameMode(final PlayerGameModeChangeEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || gameModeConf.isEmpty() || BedrockChecker.isBedrock(player.getUniqueId()))
            return;

        if (event.getNewGameMode() == GameMode.valueOf(gameModeConf.toUpperCase())) {

            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final String worldName = player.getWorld().getName();

            final Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
            placeholders.put("%world%", worldName);
            placeholders.put("%uuid%", playerUUID.toString());
            placeholders.put("%player%", playerName);
            placeholders.put("%game_mode%", gameModeConf);

            // Log To Files
            if (Data.isLogToFiles) {
                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                    FileHandler.handleFileLog("Files.Game-Mode-Staff", placeholders, FileHandler.getStaffFile());
                } else {
                    FileHandler.handleFileLog("Files.Game-Mode", placeholders, FileHandler.getGameModeFile());
                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    this.main.getDiscord().handleDiscordLog("Discord.Game-Mode-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
                } else {

                    this.main.getDiscord().handleDiscordLog("Discord.Game-Mode", placeholders, DiscordChannels.GAME_MODE, playerName, playerUUID);
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueGameMode(Data.serverName, playerName, playerUUID.toString(), gameModeConf, worldName, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // External
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueGameMode(Data.serverName, playerName, playerUUID.toString(), gameModeConf, worldName, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
