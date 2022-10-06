package me.prism3.logger.events;

import me.prism3.logger.Main;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import me.prism3.loggercore.database.data.Coordinates;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static me.prism3.logger.utils.Data.loggerStaffLog;

public class OnSign implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSign(final SignChangeEvent event) {

        if (!event.isCancelled()) {

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

            // Log To Files
            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Player-Sign-Text-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines.get(0).replace("\\", "\\\\")).replace("%line2%", lines.get(1).replace("\\", "\\\\")).replace("%line3%", lines.get(2).replace("\\", "\\\\")).replace("%line4%", lines.get(3).replace("\\", "\\\\")).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                } else {

                    try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getSignLogFile(), true))) {

                        out.write(this.main.getMessages().get().getString("Files.Player-Sign-Text").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines.get(0).replace("\\", "\\\\")).replace("%line2%", lines.get(1).replace("\\", "\\\\")).replace("%line3%", lines.get(2).replace("\\", "\\\\")).replace("%line4%", lines.get(3).replace("\\", "\\\\")).replace("%uuid%", playerUUID.toString()) + "\n");

                    } catch (final IOException e) {

                        Log.warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();
                    }
                }
            }

            // Discord Integration
            if (!player.hasPermission(Data.loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

                if (Data.isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!this.main.getMessages().get().getString("Discord.Player-Sign-Text-Staff").isEmpty()) {

                        this.main.getDiscord().staffChat(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Sign-Text-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines.get(0).replace("\\", "\\\\")).replace("%line2%", lines.get(1).replace("\\", "\\\\")).replace("%line3%", lines.get(2).replace("\\", "\\\\")).replace("%line4%", lines.get(3).replace("\\", "\\\\")).replace("%uuid%", playerUUID.toString()), false);
                    }
                } else {

                    if (!this.main.getMessages().get().getString("Discord.Player-Sign-Text").isEmpty()) {

                        this.main.getDiscord().playerSignText(playerName, playerUUID, this.main.getMessages().get().getString("Discord.Player-Sign-Text-Staff").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%x%", String.valueOf(x)).replace("%y%", String.valueOf(y)).replace("%z%", String.valueOf(z)).replace("%line1%", lines.get(0).replace("\\", "\\\\")).replace("%line2%", lines.get(1).replace("\\", "\\\\")).replace("%line3%", lines.get(2).replace("\\", "\\\\")).replace("%line4%", lines.get(3).replace("\\", "\\\\")).replace("%uuid%", playerUUID.toString()), false);
                    }
                }
            }

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertPlayerSignText(Data.serverName, playerName, playerUUID.toString(), coordinates, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertPlayerSignText(Data.serverName, playerName, playerUUID.toString(), coordinates, "[" + lines.get(0) + "] " + "[" + lines.get(1) + "] " + "[" + lines.get(2) + "] " + "[" + lines.get(3) + "]", player.hasPermission(loggerStaffLog));

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
