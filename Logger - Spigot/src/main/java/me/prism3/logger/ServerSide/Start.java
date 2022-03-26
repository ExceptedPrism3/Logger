package me.prism3.logger.ServerSide;

import me.prism3.logger.Discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.Database.External.ExternalData;
import me.prism3.logger.Utils.FileHandler;
import me.prism3.logger.Database.SQLite.Global.SQLiteData;
import me.prism3.logger.Utils.Messages;
import me.prism3.logger.Utils.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Start {

    private final Main main = Main.getInstance();

    public void run(){

        if (this.main.getConfig().getBoolean("Log-Server.Start")) {

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getserverStartFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.Start")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Start")).isEmpty()) {

                Discord.serverStart(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Start")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())), false);
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

        if (Messages.getOldMsg()) {

            this.main.getLogger().warning("Old Message file has been found. Consider deleting it!");

        }
    }
}
