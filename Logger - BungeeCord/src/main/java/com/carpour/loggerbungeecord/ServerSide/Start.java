package com.carpour.loggerbungeecord.ServerSide;

import com.carpour.loggerbungeecord.Database.External.ExternalData;
import com.carpour.loggerbungeecord.Database.SQLite.SQLiteData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

import static com.carpour.loggerbungeecord.Utils.Data.*;

public class Start {

    private final Main main = Main.getInstance();

    public void run(){

        if (this.main.getConfig().getBoolean("Log-Server.Start")) {

            // Log To Files Handling
            if (isLogToFiles) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStartLogFile(), true));
                    out.write(Messages.getString("Files.Server-Side.Start").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Messages.getString("Discord.Server-Side.Start").isEmpty()) {

                Discord.serverStart(Messages.getString("Discord.Server-Side.Start").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);

            }

            // External
            if (isExternal && this.main.getExternal().isConnected()) {

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
        }
        if (isWhitelisted && isBlacklisted) {

            this.main.getLogger().warning("Enabling both Whitelist and Blacklist isn't supported. " +
                    "Disable one of them to continue logging Player Commands.");

        }
    }
}
