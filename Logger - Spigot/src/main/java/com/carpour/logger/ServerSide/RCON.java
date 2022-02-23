package com.carpour.logger.ServerSide;

import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class RCON implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onConnection(final RemoteServerCommandEvent event) {

        if (this.main.getConfig().getBoolean("Log-Server.RCON")) {

            final String ip = event.getSender().getServer().getIp();
            final String command = event.getCommand();

            // Log To Files Handling
            if (isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRconFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.RCON")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now()).replaceAll("%IP%", ip).replaceAll("%command%", command)) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RCON")).isEmpty()) {

                Discord.rcon(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RCON")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now()).replaceAll("%IP%", ip).replaceAll("%command%", command)), false);
            }

            // MySQL
            if (isExternal && this.main.external.isConnected()) {

                try {

                    ExternalData.rcon(serverName, ip, command);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertRcon(serverName, ip, command);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
