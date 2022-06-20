package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;

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

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStopFile(), true));
                    out.write(Objects.requireNonNull(this.main.getMessages().get().getString("Files.Server-Side.Stop")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.Stop")).isEmpty()) {

                this.main.getDiscord().serverStop(Objects.requireNonNull(this.main.getMessages().get().getString("Discord.Server-Side.Stop")).replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())), false);
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
