package me.prism3.logger.events.commands;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.time.ZonedDateTime;
import java.util.*;

import static me.prism3.logger.utils.Data.*;

public class OnCommand implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCmd(final PlayerCommandPreprocessEvent event) {

        if (event.isCancelled())
            return;

        if (isWhitelisted && isBlacklisted) return;

        // Whitelist Commands
        if (isWhitelisted) {

            new OnCommandWhitelist().onWhitelistedCommand(event);
            return;
        }

        final Player player = event.getPlayer();

        if (player.hasPermission(loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final List<String> commandParts = Arrays.asList(event.getMessage().split("\\s+"));

        // Blacklisted Commands
        if (isBlacklisted)
            for (String m : commandsToBlock)
                if (commandParts.get(0).equalsIgnoreCase(m)) return;

        final String worldName = player.getWorld().getName();
        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final String command = event.getMessage().replace("\\", "\\\\");

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%command%", command);

        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Player-Commands-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Player-Commands", placeholders, FileHandler.getPlayerCommandLogFile());
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Commands-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Commands", placeholders, DiscordChannels.PLAYER_COMMANDS, playerName, playerUUID);
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
