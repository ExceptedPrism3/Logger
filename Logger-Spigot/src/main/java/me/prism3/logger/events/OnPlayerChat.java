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
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnPlayerChat implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {

        if (!event.isCancelled()) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String worldName = player.getWorld().getName();
            final String playerName = player.getName();
            final UUID playerUUID = player.getUniqueId();
            final String msg = event.getMessage().replace("\\", "\\\\");

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Player-Chat-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%message%", msg) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getChatLogFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Player-Chat").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%player%", playerName).replace("%message%", msg) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Player-Chat-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Chat-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%message%", msg), false);
                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Player-Chat").isEmpty()) {

                        this.main.getDiscord().playerChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Chat").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%uuid%", playerUUID.toString()).replace("%world%", worldName).replace("%message%", msg), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getQueueManager().queuePlayerChat(Data.serverName, playerName, playerUUID.toString(), worldName, msg, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getQueueManager().queuePlayerChat(Data.serverName, playerName, playerUUID.toString(), worldName, msg, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
