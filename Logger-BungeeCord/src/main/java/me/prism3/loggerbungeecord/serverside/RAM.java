package me.prism3.loggerbungeecord.serverside;

import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.utils.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static me.prism3.loggerbungeecord.utils.Data.*;

public class RAM implements Runnable {

    private final Main main = Main.getInstance();

    public void run() {

        if (this.main.getConfig().getBoolean("Log-Server.RAM")) {

            if (ramPercent <= 0 || ramPercent >= 100) return;

            long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
            long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
            long usedMemory = maxMemory - freeMemory;
            double percentUsed = usedMemory * 100.0D / maxMemory;

            if (ramPercent <= percentUsed) {

                // Log To Files Handling
                if (isLogToFiles) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRamLogFile(), true));
                        out.write(this.main.getMessages().getString("Files.Server-Side.RAM").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%max%", String.valueOf(maxMemory)).replace("%used%", String.valueOf(usedMemory)).replace("%free%", String.valueOf(freeMemory)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!this.main.getMessages().getString("Discord.Server-Side.RAM").isEmpty())
                    this.main.getDiscord().ram(this.main.getMessages().getString("Discord.Server-Side.RAM").replace("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replace("%max%", String.valueOf(maxMemory)).replace("%used%", String.valueOf(usedMemory)).replace("%free%", String.valueOf(freeMemory)), false);

                // External
                if (isExternal) {

                    try {

                        Main.getInstance().getDatabase().insertRam(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite) {

                    try {

                        Main.getInstance().getSqLite().insertRAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
