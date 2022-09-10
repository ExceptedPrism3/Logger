package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class RCON implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onConnection(final RemoteServerCommandEvent event) {

        final String ip = event.getSender().getServer().getIp();
        final String command = event.getCommand();

        // Log To Files
        if (Data.isLogToFiles) {

            try (final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRconFile(), true))) {
                
                out.write(this.main.getMessages().get().getString("Files.Server-Side.RCON").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()).replace("%IP%", ip).replace("%command%", command)) + "\n");

            } catch (final IOException e) {

                Log.warning("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        // Discord
        if (!this.main.getMessages().get().getString("Discord.Server-Side.RCON").isEmpty())
            this.main.getDiscord().rCon(this.main.getMessages().get().getString("Discord.Server-Side.RCON").replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()).replace("%IP%", ip).replace("%command%", command))), false);

        // External
        if (Data.isExternal) {

            try {

                Main.getInstance().getDatabase().insertRCON(Data.serverName, ip, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }

        // SQLite
        if (Data.isSqlite) {

            try {

                Main.getInstance().getSqLite().insertRCON(Data.serverName, ip, command);

            } catch (final Exception e) { e.printStackTrace(); }
        }
    }
}
