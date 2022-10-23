package me.prism3.logger.events.commands;

import me.prism3.logger.Main;
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

        // Log To Files
        if (isLogToFiles) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Player-Commands-Whitelisted-Staff").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", player.getWorld().getName()).replace("%player%", player.getName()).replace("%command%", command).replace("%uuid%", player.getUniqueId().toString()) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            } else {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true))) {

                    out.write(this.main.getMessages().get().getString("Files.Player-Commands-Whitelisted").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", player.getWorld().getName()).replace("%player%", player.getName()).replace("%command%", command).replace("%uuid%", player.getUniqueId().toString()) + "\n");

                } catch (final IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();
                }
            }
        }

        // Discord Integration
        if (!player.hasPermission(loggerExemptDiscord) && this.main.getDiscordFile().getBoolean("Discord.Enable")) {

            if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                if (!this.main.getMessages().get().getString("Discord.Player-Commands-Whitelisted-Staff").isEmpty()) {

                    this.main.getDiscord().staffChat(player.getName(), player.getUniqueId(), this.main.getMessages().get().getString("Discord.Player-Commands-Staff-Whitelisted").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", player.getWorld().getName()).replace("%command%", command).replace("%uuid%", player.getUniqueId().toString()), false);
                }
            } else {

                if (!this.main.getMessages().get().getString("Discord.Player-Commands-Whitelisted").isEmpty()) {

                    this.main.getDiscord().playerCommand(player.getName(), player.getUniqueId(), this.main.getMessages().get().getString("Discord.Player-Commands-Whitelisted").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", player.getWorld().getName()).replace("%command%", command).replace("%uuid%", player.getUniqueId().toString()), false);
                }
            }
        }

        // External
        if (isExternal) {

            try {

                Main.getInstance().getQueueManager().queuePlayerCommands(serverName, player.getName(), player.getUniqueId().toString(), player.getWorld().getName(), command, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (isSqlite) {

            try {

                Main.getInstance().getQueueManager().queuePlayerCommands(serverName, player.getName(), player.getUniqueId().toString(), player.getWorld().getName(), command, player.hasPermission(loggerStaffLog));

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
