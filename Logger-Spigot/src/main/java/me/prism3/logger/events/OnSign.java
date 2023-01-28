package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.time.ZonedDateTime;
import java.util.*;

import static me.prism3.logger.utils.Data.*;

public class OnSign implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSign(final SignChangeEvent event) {

        if (event.isCancelled())
            return;

        final Player player = event.getPlayer();

        if (player.hasPermission(Data.loggerExempt) || BedrockChecker.isBedrock(player.getUniqueId())) return;

        final String playerName = player.getName();
        final UUID playerUUID = player.getUniqueId();
        final World world = player.getWorld();
        final List<String> lines = Arrays.asList(event.getLines());
        final String worldName = world.getName();
        final int x = player.getLocation().getBlockX();
        final int y = player.getLocation().getBlockY();
        final int z = player.getLocation().getBlockZ();

        final Coordinates coordinates = new Coordinates(x, y, z, worldName);

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%world%", worldName);
        placeholders.put("%uuid%", playerUUID.toString());
        placeholders.put("%player%", playerName);
        placeholders.put("%x%", String.valueOf(x));
        placeholders.put("%y%", String.valueOf(y));
        placeholders.put("%z%", String.valueOf(z));
        placeholders.put("%line1%", lines.get(0).replace("\\", "\\\\"));
        placeholders.put("%line2%", lines.get(1).replace("\\", "\\\\"));
        placeholders.put("%line3%", lines.get(2).replace("\\", "\\\\"));
        placeholders.put("%line4%", lines.get(3).replace("\\", "\\\\"));

        // Log To Files
        if (Data.isLogToFiles) {
            if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {
                FileHandler.handleFileLog("Files.Player-Sign-Text-Staff", placeholders, FileHandler.getStaffFile());
            } else {
                FileHandler.handleFileLog("Files.Player-Sign-Text", placeholders, FileHandler.getSignLogFile());
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().get().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Sign-Text-Staff", placeholders, DiscordChannels.STAFF, playerName, playerUUID);
            } else {

                this.main.getDiscord().handleDiscordLog("Discord.Player-Sign-Text", placeholders, DiscordChannels.PLAYER_SIGN_TEXT, playerName, playerUUID);
            }
        }

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerSignText(Data.serverName, playerName, playerUUID.toString(), coordinates, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getDatabase().getDatabaseQueue().queuePlayerSignText(Data.serverName, playerName, playerUUID.toString(), coordinates, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
