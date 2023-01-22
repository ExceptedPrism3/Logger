package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.discord.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class Stop {

    private final Main main = Main.getInstance();

    public void run() {

        if (!this.main.getConfig().getBoolean("Log-Server.Stop"))
            return;

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));

        // Log To Files
        if (Data.isLogToFiles)
            FileHandler.handleFileLog("Files.Server-Side.Stop", placeholders, FileHandler.getServerStopFile());

        // Discord
        if (this.main.getDiscordFile().getBoolean("Discord.Enable"))
            this.main.getDiscord().handleDiscordLog("Discord.Server-Side.Stop", placeholders, DiscordChannels.SERVER_STOP, "Server Stop", null);

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
