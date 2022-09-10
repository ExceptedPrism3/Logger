package me.prism3.loggervelocity.serverside;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.utils.FileHandler;
import me.prism3.loggervelocity.utils.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.utils.Data.*;

public class Console {

    @Subscribe
    public void onConsole(final CommandExecuteEvent event) {

        final Main main = Main.getInstance();

        final String command = event.getCommand().replace("\\", "\\\\");

        // Log To Files
        if (isLogToFiles) {

            try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getConsoleCommandLogFile(), true))) {

                out.write(main.getMessages().getString("Files.Server-Side.Console-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command) + "\n");

            } catch (final IOException e) {

                Log.error("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        // Discord
        if (!main.getMessages().getString("Discord.Server-Side.Console-Commands").isEmpty())
            main.getDiscord().console(main.getMessages().getString("Discord.Server-Side.Console-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command), false);

        // External
        if (isExternal) {

            try {

                Main.getInstance().getDatabase().insertConsoleCommand(serverName, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (isSqlite) {

            try {

                Main.getInstance().getSqLite().insertConsoleCommand(serverName, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
