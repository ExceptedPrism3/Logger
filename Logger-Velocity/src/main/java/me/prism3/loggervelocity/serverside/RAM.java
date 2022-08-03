package me.prism3.loggervelocity.serverside;

import me.prism3.loggervelocity.Main;
import me.prism3.loggervelocity.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggervelocity.utils.Data.*;

public class RAM implements Runnable {

    final Main main = Main.getInstance();

    public void run() {

        if (main.getConfig().getBoolean("Log-Server.RAM")) {

            if (ramPercent <= 0 || ramPercent >= 100) return;

            final long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
            final long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
            final long usedMemory = maxMemory - freeMemory;
            final double percentUsed = usedMemory * 100.0D / maxMemory;

            if (ramPercent <= percentUsed) {

                // Log To Files
                if (isLogToFiles) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRamLogFile(), true));
                        out.write(main.getMessages().getString("Files.Server-Side.RAM").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%max%", String.valueOf(maxMemory)).replace("%used%", String.valueOf(usedMemory)).replace("%free%", String.valueOf(freeMemory)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getLogger().error("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!main.getMessages().getString("Discord.Server-Side.RAM").isEmpty()) {

                    main.getDiscord().ram(main.getMessages().getString("Discord.Server-Side.RAM").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%max%", String.valueOf(maxMemory)).replace("%used%", String.valueOf(usedMemory)).replace("%free%", String.valueOf(freeMemory)), false);

                }

                // External
                if (isExternal ) {

                    try {

                        Main.getInstance().getDatabase().insertRam(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite ) {

                    try {

                        Main.getInstance().getSqLite().insertRAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
