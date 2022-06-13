package me.prism3.logger.serverside;

import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.Main;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Messages;
import me.prism3.logger.utils.Data;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class RCON implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onConnection(final RemoteServerCommandEvent event) {

        if (this.main.getConfig().getBoolean("Log-Server.RCON")) {

            final String ip = event.getSender().getServer().getIp();
            final String command = event.getCommand();

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRconFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.RCON")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()).replaceAll("%IP%", ip).replaceAll("%command%", command)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RCON")).isEmpty()) {

                this.main.getDiscord().rCon(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RCON")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now()).replaceAll("%IP%", ip).replaceAll("%command%", command)), false);
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.rCon(Data.serverName, ip, command);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertRcon(Data.serverName, ip, command);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
