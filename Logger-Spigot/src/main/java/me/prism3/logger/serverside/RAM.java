package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class RAM implements Runnable {

    private final Main main = Main.getInstance();

    public void run() {

        if (Data.ramPercent <= 0 || Data.ramPercent >= 100) return;

        final long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
        final long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
        final long usedMemory = maxMemory - freeMemory;
        final double percentUsed = usedMemory * 100.0D / maxMemory;

        if (Data.ramPercent <= percentUsed) {

            final Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
            placeholders.put("%max%", String.valueOf(maxMemory));
            placeholders.put("%used%", String.valueOf(usedMemory));
            placeholders.put("%free%", String.valueOf(freeMemory));

            // Log To Files
            if (Data.isLogToFiles)
                FileHandler.handleFileLog("Files.Server-Side.RAM", placeholders, FileHandler.getRAMLogFile());

            // Discord
            if (this.main.getDiscordFile().get().getBoolean("Discord.Enable"))
                this.main.getDiscord().handleDiscordLog("Discord.Server-Side.RAM", placeholders, DiscordChannels.RAM, "RAM", null);

            // External
            if (Data.isExternal) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueRam(Data.serverName, maxMemory, usedMemory, freeMemory);

                } catch (final Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite) {

                try {

                    Main.getInstance().getDatabase().getDatabaseQueue().queueRam(Data.serverName, maxMemory, usedMemory, freeMemory);

                } catch (final Exception e) { e.printStackTrace(); }
            }
        }
    }
}
