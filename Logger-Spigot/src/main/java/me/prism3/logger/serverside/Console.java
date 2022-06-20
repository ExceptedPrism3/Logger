package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Console implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommand(final ServerCommandEvent event) {

        if (event.getSender() instanceof BlockCommandSender) return;

        final String command = event.getCommand().replace("\\", "\\\\");
        final List<String> commandParts = Arrays.asList(event.getCommand().split("\\s+"));

        // Blacklisted Commands
        if (Data.isConsoleCommands) {

            for (String m : Data.consoleCommandsToBlock) {

                if (commandParts.get(0).equalsIgnoreCase(m)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (this.main.getConfig().getBoolean("Log-Server.Console-Commands")) {

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getConsoleLogFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Server-Side.Console-Commands")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.Console-Commands")).isEmpty()) {

                this.main.getDiscord().console(Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.Console-Commands")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command), false);
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.consoleCommands(Data.serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertConsoleCommands(Data.serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
