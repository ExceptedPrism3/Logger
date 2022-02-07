package com.carpour.logger.ServerSide;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Stop {

    private final Main main = Main.getInstance();

    public void run() {

        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (main.getConfig().getBoolean("Log-Server.Stop")) {

            // Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getserverStopFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.Stop")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Stop")).isEmpty()) {

                Discord.serverStop(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Stop")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);
            }

            // MySQL
            if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                try {

                    ExternalData.serverStop(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStop(serverName);

                } catch (Exception exception) { exception.printStackTrace(); }
            }
        }
    }
}
