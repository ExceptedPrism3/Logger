package me.prism3.loggervelocity.serverside;

import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.utils.Data.*;

public class Start {

    public void run() {

        final Main main = Main.getInstance();

        if (main.getConfig().getBoolean("Log-Server.Start")) {

            // Log to Files
            if (isLogToFiles) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStartLogFile(), true));
                    out.write(main.getMessages().getString("Files.Server-Side.Start").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!main.getMessages().getString("Discord.Server-Side.Start").isEmpty())
                main.getDiscord().serverStart(main.getMessages().getString("Discord.Server-Side.Start").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);

            // External
            if (isExternal) {

                try {

                    Main.getInstance().getDatabase().insertServerStart(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertServerStart(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }

        if (isWhitelisted && isBlacklisted) {

            main.getLogger().error("Enabling both Whitelist and Blacklist isn't supported. " +
                    "Please disable one of them to continue logging Player Commands");

        }
    }
}
