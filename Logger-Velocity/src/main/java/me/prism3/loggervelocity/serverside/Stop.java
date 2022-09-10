package me.prism3.loggervelocity.serverside;

import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.utils.FileHandler;
import me.prism3.loggervelocity.utils.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.utils.Data.*;

public class Stop {

    public void run() {

        final Main main = Main.getInstance();

        if (main.getConfig().getBoolean("Log-Server.Stop")) {

            // Log to Files
            if (isLogToFiles) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStopLogFile(), true))) {

                    out.write(main.getMessages().getString("Files.Server-Side.Stop").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");

                } catch (final IOException e) {

                    Log.error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!main.getMessages().getString("Discord.Server-Side.Stop").isEmpty())
                main.getDiscord().serverStop(main.getMessages().getString("Discord.Server-Side.Stop").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);

            // External
            if (isExternal) {

                try {

                    Main.getInstance().getDatabase().insertServerStop(serverName);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertServerStop(serverName);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
