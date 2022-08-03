package me.prism3.logger.events.oncommands;

import me.prism3.logger.Main;
import me.prism3.logger.events.spy.OnCommandSpy;
import me.prism3.logger.utils.BedrockChecker;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
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

public class OnCommand implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCmd(final PlayerCommandPreprocessEvent event) {

        // Commands Spy
        if (this.main.getConfig().getBoolean("Spy-Features.Commands-Spy.Enable")) {

            new OnCommandSpy().onCmdSpy(event);

        }

        // Whitelist Commands
        if (Data.isWhitelisted) {

            new OnCommandWhitelist().onWhitelistedCommand(event);

            return;
        }

        if (!event.isCancelled() && this.main.getConfig().getBoolean("Log-Player.Commands")) {

            final Player player = event.getPlayer();

            if (player.hasPermission(Data.loggerExempt) || (Data.isWhitelisted && Data.isBlacklisted) || BedrockChecker.isBedrock(player.getUniqueId())) return;

            final World world = player.getWorld();
            final String worldName = world.getName();
            final String playerName = player.getName();
            final String command = event.getMessage().replace("\\", "\\\\");
            final List<String> commandParts = Arrays.asList(event.getMessage().split("\\s+"));

            // Blacklisted Commands
            if (Data.isBlacklisted) {

                for (String m : Data.commandsToBlock) {

                    if (commandParts.get(0).equalsIgnoreCase(m)) return;

                }
            }

            if (Data.isLogToFiles) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Commands-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Commands-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%command%", command), false);

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Player-Commands-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%command%", command) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (Data.isExternal  ) {

                        Main.getInstance().getDatabase().insertPlayerCommands(Data.serverName, player, command, true);

                    }

                    if (Data.isSqlite ) {

                        Main.getInstance().getSqLite().insertPlayerCommands(Data.serverName, player, command, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Player-Commands")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%player%", playerName).replace("%command%", command) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!player.hasPermission(Data.loggerExemptDiscord)) {

                if (Data.isStaffEnabled && player.hasPermission(Data.loggerStaffLog)) {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Commands-Staff")).isEmpty()) {

                        this.main.getDiscord().staffChat(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Commands-Staff")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%command%", command), false);

                    }
                } else {

                    if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Commands")).isEmpty()) {

                        this.main.getDiscord().playerCommand(player, Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Player-Commands")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%world%", worldName).replace("%command%", command), false);
                    }
                }
            }

            // Logging to MySQL if logging to MySQL and Command Logging is enabled
            if (Data.isExternal  ) {

                try {

                    Main.getInstance().getDatabase().insertPlayerCommands(Data.serverName, player, command, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }

            // Logging to SQLite if logging to SQLite and Command Logging is enabled
            if (Data.isSqlite ) {

                try {

                    Main.getInstance().getSqLite().insertPlayerCommands(Data.serverName, player, command, player.hasPermission(Data.loggerStaffLog));

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
