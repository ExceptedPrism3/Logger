package com.carpour.logger.ServerSide;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static com.carpour.logger.Utils.Data.*;

public class Console implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommand(final ServerCommandEvent event) {

        if (this.main.getConfig().getBoolean("Log-Server.Console-Commands")) {

            final String command = event.getCommand().replace("\\", "\\\\");
            final List<String> commandParts = Arrays.asList(event.getCommand().split("\\s+"));

            // Blacklisted Commands
            if (isConsoleCommands) {

                for (String m : consoleCommandsToBlock) {

                    if (commandParts.get(0).equalsIgnoreCase(m)) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            // Log To Files Handling
            if (isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getConsoleLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.Console-Commands")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%command%", command) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Console-Commands")).isEmpty()) {

                Discord.console(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Console-Commands")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%command%", command), false);
            }

            // MySQL
            if (isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.consoleCommands(serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertConsoleCommands(serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
