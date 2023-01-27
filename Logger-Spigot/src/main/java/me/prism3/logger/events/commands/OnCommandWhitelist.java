package me.prism3.logger.events.commands;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
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

public class OnCommandWhitelist implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWhitelistedCommand(final PlayerCommandPreprocessEvent event) {

        final Player player = event.getPlayer();
        final String command = event.getMessage().replace("\\", "\\\\");
        final List<String> commandParts = Arrays.asList(event.getMessage().split("\\s+"));

        for (String m : commandsToLog)
            if (commandParts.get(0).equalsIgnoreCase(m))
                this.commandWhitelistLog(player, command);
    }

    private void commandWhitelistLog(Player player, String command) {

        final String worldName = player.getWorld().getName();
        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%command%", command);

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Player-Commands-Whitelisted-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Player-Commands-Whitelisted", placeholders, FileHandler.getPlayerCommandLogFile());
            }
        }

        // DiscordManager Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("DiscordManager.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("DiscordManager.Player-Commands-Whitelisted-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("DiscordManager.Player-Commands-Whitelisted", placeholders, DiscordChannels.PLAYER_COMMANDS, playerName, playerUUID);
            }
        }

        // External
        if (isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerCommands(serverName, player.getName(), player.getUniqueId().toString(), player.getWorld().getName(), command, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerCommands(serverName, player.getName(), player.getUniqueId().toString(), player.getWorld().getName(), command, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
