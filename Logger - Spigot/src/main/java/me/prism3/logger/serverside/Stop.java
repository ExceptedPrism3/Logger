package me.prism3.logger.serverside;

import me.prism3.logger.discord.Discord;
import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Messages;
import me.prism3.logger.utils.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Stop {

    private final Main main = Main.getInstance();

    public void run() {

        if (this.main.getConfig().getBoolean("Log-Server.Stop")) {

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getserverStopFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.Stop")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Stop")).isEmpty()) {

                Discord.serverStop(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.Stop")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())), false);
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.serverStop(Data.serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStop(Data.serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }
}
