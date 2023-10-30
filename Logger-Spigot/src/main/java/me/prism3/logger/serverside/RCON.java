package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.DiscordChannels;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.enums.LogCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


public class RCON implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onConnection(final RemoteServerCommandEvent event) {

        final String ip = event.getSender().getServer().getIp();
        final String command = event.getCommand();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()));
        placeholders.put("%IP%", ip);
        placeholders.put("%command%", command);

        // Log To Files
        if (Data.isLogToFiles)
            this.main.getFileHandler().handleFileLog(LogCategory.RCON, "Files.Server-Side.RCON", placeholders);

        // Discord
        if (this.main.getDiscordFile().get().getBoolean("Discord.Enable"))
            this.main.getDiscord().handleDiscordLog("Discord.Server-Side.RCON", placeholders, DiscordChannels.RCON, "RCON", null);

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertRCON(Data.serverName, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertRCON(Data.serverName, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
