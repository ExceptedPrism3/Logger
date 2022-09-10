package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
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

public class Console implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsoleCommand(final ServerCommandEvent event) {

        if (event.getSender() instanceof BlockCommandSender) return;

        final String command = event.getCommand().replace("\\", "\\\\");
        final List<String> commandParts = Arrays.asList(event.getCommand().split("\\s+"));

        // Blacklisted Commands
        if (Data.isConsoleCommands && Data.consoleCommandsToBlock.stream().anyMatch(commandParts.get(0)::equalsIgnoreCase)) {
            event.setCancelled(true);
            return;
        }

        // Log To Files
        if (Data.isLogToFiles) {

            try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getConsoleLogFile(), true))) {

                out.write(this.main.getMessages().get().getString("Files.Server-Side.Console-Commands").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command) + "\n");

            } catch (final IOException e) {

                Log.warning("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        // Discord
        if (!this.main.getMessages().get().getString("Discord.Server-Side.Console-Commands").isEmpty())
            this.main.getDiscord().console(this.main.getMessages().get().getString("Discord.Server-Side.Console-Commands").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command), false);

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertConsoleCommand(Data.serverName, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertConsoleCommand(Data.serverName, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
