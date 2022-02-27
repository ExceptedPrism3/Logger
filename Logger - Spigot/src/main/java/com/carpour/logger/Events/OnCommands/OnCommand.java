package com.carpour.logger.Events.OnCommands;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Events.Spy.OnCommandSpy;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import org.bukkit.World;
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
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class OnCommand implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCmd(final PlayerCommandPreprocessEvent event) {

        // Commands Spy
        if (this.main.getConfig().getBoolean("Spy-Features.Commands-Spy.Enable")) {

            new OnCommandSpy().onCmdSpy(event);

        }

        // Whitelist Commands
        if (isWhitelisted) {

            new OnCommandWhitelist().onWhitelistedCommand(event);

            return;
        }

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Commands")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(loggerExempt) || (isWhitelisted && isBlacklisted)) return;

            final World world = player.getWorld();
            final String worldName = world.getName();
            final String playerName = player.getName();
            final String command = event.getMessage().replace("\\", "\\\\");
            final List<String> commandParts = Arrays.asList(event.getMessage().split("\\s+"));

            // Blacklisted Commands
            if (isBlacklisted) {

                for (String m : commandsToBlock) {

                    if (commandParts.get(0).equalsIgnoreCase(m)) return;

                }
            }

            if (isLogToFiles) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Commands-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Commands-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%command%", command), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Commands-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (isExternal && this.main.getExternal().isConnected()) {

                        ExternalData.playerCommands(serverName, worldName, playerName, command, true);

                    }

                    if (isSqlite && this.main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerCommands(serverName, player, command, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Commands")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(loggerExemptDiscord)) {

                if (isStaffEnabled && player.hasPermission(loggerStaffLog)) {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Commands-Staff")).isEmpty()) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Commands-Staff")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%command%", command), false);

                    }
                } else {

                    if (!Objects.requireNonNull(Messages.get().getString("Discord.Player-Commands")).isEmpty()) {

                        Discord.playerCommand(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Commands")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%world%", worldName).replaceAll("%command%", command), false);
                    }
                }
            }

            // Logging to MySQL if logging to MySQL and Command Logging is enabled
            if (isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.playerCommands(serverName, worldName, playerName, command, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }

            // Logging to SQLite if logging to SQLite and Command Logging is enabled
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerCommands(serverName, player, command, player.hasPermission(loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
