package me.prism3.logger.events.commands;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnCommand implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCmd(final PlayerCommandPreprocessEvent event) {

        if (isWhitelisted && isBlacklisted) return;

        // Whitelist Commands
        if (isWhitelisted) {

            new OnCommandWhitelist().onWhitelistedCommand(event);
            return;
        }

        if (!event.isCancelled()) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final String worldName = player.getWorld().getName();
            final UUID playerUUID = player.getUniqueId();
            final String playerName = player.getName();
            final String command = event.getMessage().replace("\\", "\\\\");
            final List<String> commandParts = Arrays.asList(event.getMessage().split("\\s+"));

            // Blacklisted Commands
            if (isBlacklisted)
                for (String m : commandsToBlock)
                    if (commandParts.get(0).equalsIgnoreCase(m)) return;

            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {
                        
                        out.write(this.main.getMessages().get().getString("Files.Player-Commands-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%command%", command).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true))) {
                        
                        out.write(this.main.getMessages().get().getString("Files.Player-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%command%", command).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Player-Commands-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Commands-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%command%", command).replace("%uuid%", playerUUID.toString()), false);
                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Player-Commands").isEmpty()) {

                        this.main.getDiscord().playerCommand(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%command%", command).replace("%uuid%", playerUUID.toString()), false);
                    }
                }
            }

            // External
            if (isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerCommands(serverName, playerName, playerUUID.toString(), worldName, command, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerCommands(serverName, playerName, playerUUID.toString(), worldName, command, player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
