package com.carpour.loggervelocity.ServerSide;

import com.carpour.loggervelocity.Database.External.External;
import com.carpour.loggervelocity.Database.External.ExternalData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.FileHandler;
import com.carpour.loggervelocity.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static com.carpour.loggervelocity.Utils.Data.*;

public class Start {

    public void run(){

        final Main main = Main.getInstance();
        final Messages messages = new Messages();

        if (main.getConfig().getBoolean("Log-Server.Start")) {

            // Log To Files Handling
            if (isLogToFiles) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStartLogFile(), true));
                    out.write(messages.getString("Files.Server-Side.Start").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!messages.getString("Discord.Server-Side.Start").isEmpty()) {

                Discord.serverStart(messages.getString("Discord.Server-Side.Start").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);

            }

            // External
            if (isExternal && External.isConnected()) {

                try {

                    ExternalData.serverStart(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (isSqlite && SQLite.isConnected()) {

                try {

                    SQLiteData.insertServerStart(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }

        if (isWhitelisted && isBlacklisted) {

            main.getLogger().error("Enabling both Whitelist and Blacklist isn't supported. " +
                    "Please disable one of them to continue logging Player Commands");

        }
    }
}
