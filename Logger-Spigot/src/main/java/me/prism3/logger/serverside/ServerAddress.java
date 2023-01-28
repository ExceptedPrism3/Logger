package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class ServerAddress implements Listener {

    private final Main main = Main.getInstance();//todo velocity

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerAddress(final PlayerLoginEvent event) {

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final UUID playerUUID = player.getUniqueId();
        final String playerName = player.getName();
        final String address = event.getHostname();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%address%", address);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Server-Side.Server-Address-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Server-Side.Server-Address", placeholders, FileHandler.getServerAddressFile());
            }
        }

        // Discord
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Server-Side.Server-Address-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Server-Side.Server-Address", placeholders, DiscordChannels.SERVER_ADDRESS, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

              Main.getInstance().getDatabase().getDatabaseQueue().queueServerAddress(Data.serverName, playerName, playerUUID.toString(), address);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueServerAddress(Data.serverName, playerName, playerUUID.toString(), address);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
