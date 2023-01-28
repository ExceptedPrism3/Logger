package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class Start {

    private final Main main = Main.getInstance();

    public void run() {

        if (Data.isWhitelisted && Data.isBlacklisted)
            Log.warning("Enabling both Whitelist and Blacklist isn't supported. " +
                    "Disable one of them to continue logging Player Commands.");

        if (!this.main.getConfig().getBoolean("Log-Server.Start"))
            return;

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));

        // Log To Files
        if (Data.isLogToFiles)
            FileHandler.handleFileLog("Files.Server-Side.Start", placeholders, FileHandler.getServerStartFile());

        // Discord
        if (this.main.getDiscordFile().get().getBoolean("Discord.Enable"))
            this.main.getDiscord().handleDiscordLog("Discord.Server-Side.Start", placeholders, DiscordChannels.SERVER_START, "Server Start", null);

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertServerStart(Data.serverName);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertServerStart(Data.serverName);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
