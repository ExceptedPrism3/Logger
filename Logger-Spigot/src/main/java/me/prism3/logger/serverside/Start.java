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

public class Start {

    private final Main main = Main.getInstance();

    public void run() {

        if (this.main.getConfig().getBoolean("Log-Server.Start")) {

            // Log To Files
            if (Data.isLogToFiles) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStartFile(), true));
                    String startMessage = this.main.getMessages().get().getString("Files.Server-Side.Start");
                    if (startMessage == null)
                        startMessage = "Server Started at %time%";
                    out.write(
                            startMessage.replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    Log.warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            String discordStartMsg = this.main.getMessages().get().getString("Discord.Server-Side.Start");
            if (discordStartMsg != null && !discordStartMsg.isEmpty()) {

                this.main.getDiscord()
                        .serverStart(discordStartMsg
                                .replace("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())), false);
            }

            // External
            if (Data.isExternal && this.main.getExternal().isConnected()) {

                try {

                    ExternalData.serverStart(Data.serverName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // SQLite
            if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStart(Data.serverName);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (Data.isWhitelisted && Data.isBlacklisted) {

            Log.warning("Enabling both Whitelist and Blacklist isn't supported. " +
                    "Disable one of them to continue logging Player Commands.");

        }
    }
}
