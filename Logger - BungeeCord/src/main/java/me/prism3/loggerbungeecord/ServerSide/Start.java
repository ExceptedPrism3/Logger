package me.prism3.loggerbungeecord.ServerSide;

import me.prism3.loggerbungeecord.Database.External.ExternalData;
import me.prism3.loggerbungeecord.Database.SQLite.SQLiteData;
import me.prism3.loggerbungeecord.Discord.Discord;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.Utils.FileHandler;
import me.prism3.loggerbungeecord.Utils.Messages;
import me.prism3.loggerbungeecord.Utils.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class Start {

    private final Main main = Main.getInstance();

    public void run(){

        if (this.main.getConfig().getBoolean("Log-Server.Start")) {

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStartLogFile(), true));
                    out.write(Messages.getString("Files.Server-Side.Start").replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Messages.getString("Discord.Server-Side.Start").isEmpty()) {

                Discord.serverStart(Messages.getString("Discord.Server-Side.Start").replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())), false);

            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.serverStart(Data.serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStart(Data.serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
        if (Data.isWhitelisted && Data.isBlacklisted) {

            this.main.getLogger().warning("Enabling both Whitelist and Blacklist isn't supported. " +
                    "Disable one of them to continue logging Player Commands.");

        }
    }
}
