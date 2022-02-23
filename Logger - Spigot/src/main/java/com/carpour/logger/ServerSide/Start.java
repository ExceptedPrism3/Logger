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
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class Start {

    private final Main main = Main.getInstance();

    public void run(){

        if (this.main.getConfig().getBoolean("Log-Server.Start")) {

            // Log To Files Handling
            if (isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getserverStartFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.Start")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Start")).isEmpty()) {

                Discord.serverStart(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Start")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);
            }

            // MySQL
            if (isExternal && this.main.external.isConnected()) {

                try {

                    ExternalData.serverStart(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStart(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            if (isWhitelisted && isBlacklisted) {

                this.main.getLogger().warning("Enabling both Whitelist and Blacklist isn't supported. " +
                        "Disable one of them to continue logging Player Commands.");

            }
        }
    }
}
