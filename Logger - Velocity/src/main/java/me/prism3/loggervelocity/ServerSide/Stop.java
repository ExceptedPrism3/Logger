package me.prism3.loggervelocity.ServerSide;

import me.prism3.loggervelocity.Database.External.External;
import me.prism3.loggervelocity.Database.External.ExternalData;
import me.prism3.loggervelocity.Database.SQLite.SQLite;
import me.prism3.loggervelocity.Database.SQLite.SQLiteData;
import me.prism3.loggervelocity.Discord.Discord;
import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.Utils.FileHandler;
import me.prism3.loggervelocity.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.Utils.Data.*;

public class Stop {

    public void run() {

        final Main main = Main.getInstance();
        final Messages messages = new Messages();

        if (main.getConfig().getBoolean("Log-Server.Stop")) {

            // Log to Files
            if (isLogToFiles) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStopLogFile(), true));
                    out.write(messages.getString("Files.Server-Side.Stop").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!messages.getString("Discord.Server-Side.Stop").isEmpty()) {

                Discord.serverStop(messages.getString("Discord.Server-Side.Stop").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);

            }

            // External
            if (isExternal && External.isConnected()) {

                try {

                    ExternalData.serverStop(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && SQLite.isConnected()) {

                try {

                    SQLiteData.insertServerStop(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
