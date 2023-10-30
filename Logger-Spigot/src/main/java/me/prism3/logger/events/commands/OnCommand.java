package me.prism3.logger.events.commands;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;
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

        final Player player = event.getPlayer();
        final String command = event.getMessage().replace("\\", "\\\\");
        final List<String> commandParts = Arrays.asList(event.getMessage().split("\\s+"));
        final String worldName = player.getWorld().getName();
        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final String fileKey = isStaffEnabled && player.hasPermission(loggerStaffLog)
                ? "Files.Player-Commands-Whitelisted-Staff"
                : "Files.Player-Commands-Whitelisted";

        final String discordKey = isStaffEnabled && player.hasPermission(loggerStaffLog)
                ? "Discord.Player-Commands-Whitelisted-Staff"
                : "Discord.Player-Commands-Whitelisted";

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%command%", command);

        // Whitelisted Commands
        if (isWhitelisted) {

            for (String m : commandsToLog) {

                if (commandParts.get(0).equalsIgnoreCase(m)) {
                    this.handleCommandLog(fileKey, discordKey, placeholders, player);
                    return;
                }
            }
        }

        // Check for exempt players or Bedrock clients
        if (player.hasPermission(loggerExempt) || BedrockChecker.isBedrock(playerUUID))
            return;

        // Blacklisted Commands
        if (isBlacklisted && commandsToBlock.contains(commandParts.get(0).toLowerCase()))
            return;

        // Blacklisted Commands
        if (isBlacklisted) {
            for (String m : commandsToBlock) {
                if (commandParts.get(0).equalsIgnoreCase(m))
                    return;
            }
        }

        this.handleCommandLog(fileKey, discordKey, placeholders, player);

        if (isExternal || isSqlite) {
            try {
                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerCommands(serverName, playerName, playerUUID.toString(), worldName, command, player.hasPermission(loggerStaffLog));
            } catch (final Exception e) { e.printStackTrace(); }
        }
    }

    private void handleCommandLog(String fileKey, String discordKey, Map<String, String> placeholders, Player player) {

        if (Data.isLogToFiles)
            main.getFileHandler().handleFileLog(LogCategory.PLAYER_COMMANDS, fileKey, placeholders);

        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable"))
            this.main.getDiscord().handleDiscordLog(discordKey, placeholders, DiscordChannels.PLAYER_COMMANDS, player.getName(), player.getUniqueId());
    }
}

