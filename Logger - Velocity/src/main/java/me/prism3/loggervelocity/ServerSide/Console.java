package me.prism3.loggervelocity.ServerSide;

import me.prism3.loggervelocity.Database.External.External;
import me.prism3.loggervelocity.Database.External.ExternalData;
import me.prism3.loggervelocity.Database.SQLite.SQLite;
import me.prism3.loggervelocity.Database.SQLite.SQLiteData;
import me.prism3.loggervelocity.Discord.Discord;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.Utils.FileHandler;
import me.prism3.loggervelocity.Utils.Messages;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.Utils.Data.*;

public class Console {

    @Subscribe
    public void onConsole(final CommandExecuteEvent event){

        final Main main = Main.getInstance();
        final Messages messages = new Messages();

        if (main.getConfig().getBoolean("Log-Server.Console-Commands")) {

            final String command = event.getCommand().replace("\\", "\\\\");

            // Log To Files
            if (isLogToFiles) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getConsoleCommandLogFile(), true));
                    out.write(messages.getString("Files.Server-Side.Console-Commands").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%command%", command) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!messages.getString("Discord.Server-Side.Console-Commands").isEmpty()) {

                Discord.console(messages.getString("Discord.Server-Side.Console-Commands").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%command%", command), false);
            }

            // External
            if (isExternal && External.isConnected()) {

                try {

                    ExternalData.consoleCommands(serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && SQLite.isConnected()) {

                try {

                    SQLiteData.insertConsoleCommands(serverName, command);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
