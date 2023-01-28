package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.prism3.logger.utils.Data.*;

public class OnBucketFill implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucket(final PlayerBucketFillEvent event) {

        if (event.isCancelled() || event.getItemStack() == null)
            return;

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final String worldName = player.getWorld().getName();
        final String bucket = event.getItemStack().getType().name().replace("_", " ");
        final int x = event.getBlockClicked().getX();
        final int y = event.getBlockClicked().getY();
        final int z = event.getBlockClicked().getZ();

        final Coordinates coordinates = new Coordinates(x, y, z, worldName);

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%x%", String.valueOf(x));
        placeholders.put("%y%", String.valueOf(y));
        placeholders.put("%z%", String.valueOf(z));
        placeholders.put("%bucket%", bucket);

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Bucket-Fill-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Bucket-Fill", placeholders, FileHandler.getBucketFillFile());
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Bucket-Fill-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Bucket-Fill", placeholders, DiscordChannels.BUCKET_FILL, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueBucketFill(Data.serverName, playerName, playerUUID.toString(), bucket, coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queueBucketFill(Data.serverName, playerName, playerUUID.toString(), bucket, coordinates, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
