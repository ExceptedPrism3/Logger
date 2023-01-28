package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnPlayerChat implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String worldName = player.getWorld().getName();
        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final String msg = event.getMessage().replace("\\", "\\\\");

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%message%", msg);

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Player-Chat-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Player-Chat", placeholders, FileHandler.getChatLogFile());
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Chat-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Chat", placeholders, DiscordChannels.PLAYER_CHAT, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerChat(Data.serverName, playerName, playerUUID.toString(), worldName, msg, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerChat(Data.serverName, playerName, playerUUID.toString(), worldName, msg, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
