package me.prism3.loggerbungeecord.serverside;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.Data;
import me.prism3.loggerbungeecord.utils.FileHandler;
import me.prism3.loggerbungeecord.utils.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class Stop {

    private final Main main = Main.getInstance();

    public void run() {

        if (this.main.getConfig().getBoolean("Log-Server.Stop")) {

            // Log To Files
            if (Data.isLogToFiles) {

                try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStopLogFile(), true))) {

                    out.write(this.main.getMessages().getString("Files.Server-Side.Stop").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())) + "\n");

                } catch (final IOException e) {

                    Log.severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!this.main.getMessages().getString("Discord.Server-Side.Stop").isEmpty())
                this.main.getDiscord().serverStop(this.main.getMessages().getString("Discord.Server-Side.Stop").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())), false);

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().insertServerStop(Data.serverName);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getSqLite().insertServerStop(Data.serverName);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
