package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnPlayerLevel implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelChange(final PlayerLevelChangeEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final int logAbove = Data.abovePlayerLevel;
        final double playerLevel = event.getNewLevel();

        if (playerLevel == logAbove) {

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Player-Level-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%level%", String.valueOf(logAbove)).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerLevelFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Player-Level").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%player%", playerName).replace("%level%", String.valueOf(logAbove)).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Player-Level-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Level-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%level%", String.valueOf(logAbove)).replace("%uuid%", playerUUID.toString()), false);
                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Player-Level").isEmpty()) {

                        this.main.getDiscord().playerLevel(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Level").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%level%", String.valueOf(logAbove)).replace("%uuid%", playerUUID.toString()), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertLevelChange(Data.serverName, playerName, playerUUID.toString(), player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertLevelChange(Data.serverName, playerName, playerUUID.toString(), player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
