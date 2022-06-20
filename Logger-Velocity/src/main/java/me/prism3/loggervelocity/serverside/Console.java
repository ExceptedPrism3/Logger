package me.prism3.loggervelocity.serverside;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.database.external.ExternalData;
import me.prism3.loggervelocity.database.sqlite.SQLiteData;
import me.prism3.loggervelocity.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.utils.Data.*;

public class Console {

    @Subscribe
    public void onConsole(final CommandExecuteEvent event) {

        final Main main = Main.getInstance();

        if (main.getConfig().getBoolean("Log-Server.Console-Commands")) {

            final String command = event.getCommand().replace("\\", "\\\\");

            // Log To Files
            if (isLogToFiles) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getConsoleCommandLogFile(), true));
                    out.write(main.getMessages().getString("Files.Server-Side.Console-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!main.getMessages().getString("Discord.Server-Side.Console-Commands").isEmpty()) {

                main.getDiscord().console(main.getMessages().getString("Discord.Server-Side.Console-Commands").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%command%", command), false);
            }

            // External
            if (isExternal && main.getExternal().isConnected()) {

                try {

                    ExternalData.consoleCommands(serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertConsoleCommands(serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
