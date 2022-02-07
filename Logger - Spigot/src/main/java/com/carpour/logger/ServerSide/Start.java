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

public class Start {

    private final Main main = Main.getInstance();

    public void run(){

        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (main.getConfig().getBoolean("Log-Server.Start")) {

            // Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getserverStartFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.Start")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Start")).isEmpty()) {

                Discord.serverStart(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Start")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);
            }

            // MySQL
            if (main.getConfig().getBoolean("Database.Enable") && main.external.isConnected()) {

                try {

                    ExternalData.serverStart(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStart(serverName);

                } catch (Exception exception) { exception.printStackTrace(); }
            }

            if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")
                    && main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {

                main.getLogger().warning("Enabling both Whitelist and Blacklist isn't supported. " +
                        "Please disable one of them to continue logging Player Commands");

            }
        }
    }
}
