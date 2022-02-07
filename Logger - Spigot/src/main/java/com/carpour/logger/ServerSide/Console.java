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
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Console implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommand(ServerCommandEvent event) {

        String command = event.getCommand();
        List<String> commandParts = Arrays.asList(event.getCommand().split("\\s+"));
        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (main.getConfig().getBoolean("Log-Server.Console-Commands")) {

            // Blacklisted Commands
            if (main.getConfig().getBoolean("Console-Commands.Blacklist-Commands")) {

                for (String m : main.getConfig().getStringList("Console-Commands.Commands-to-Block")) {

                    if (commandParts.get(0).equalsIgnoreCase(m)) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            // Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getConsoleLogFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.Console-Commands")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%command%", command) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Console-Commands")).isEmpty()) {

                Discord.console(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Console-Commands")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%command%", command), false);
            }

            // MySQL
            if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                try {

                    ExternalData.consoleCommands(serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertConsoleCommands(serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
