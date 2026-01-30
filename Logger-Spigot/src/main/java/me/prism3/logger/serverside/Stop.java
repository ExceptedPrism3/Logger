package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Data;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.utils.Log;

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
                    String stopMessage = this.main.getMessages().get().getString("Files.Server-Side.Stop");
                    if (stopMessage == null)
                        stopMessage = "Server Stopped at %time%";
                    out.write(stopMessage.replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            String discordStopMsg = this.main.getMessages().get().getString("Discord.Server-Side.Stop");
            if (discordStopMsg != null && !discordStopMsg.isEmpty()) {

                this.main.getDiscord()
                        .serverStop(discordStopMsg
                                .replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())), false);
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.serverStop(Data.serverName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStop(Data.serverName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
