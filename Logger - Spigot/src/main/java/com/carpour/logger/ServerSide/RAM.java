package com.carpour.logger.ServerSide;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.External.ExternalData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.carpour.logger.Utils.Data.*;

public class RAM implements Runnable {

    private final Main main = Main.getInstance();

    public void run() {

        if (this.main.getConfig().getBoolean("Log-Server.RAM")) {

            if (ramPercent <= 0 || ramPercent >= 100) return;

            final long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
            final long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
            final long usedMemory = maxMemory - freeMemory;
            final double percentUsed = (double) usedMemory * 100.0D / (double) maxMemory;

            if (ramPercent <= percentUsed) {

                // Log To Files Handling
                if (isLogToFiles) {

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRAMLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.RAM")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RAM")).isEmpty()) {

                    Discord.RAM(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RAM")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)), false);
                }

                // MySQL
                if (isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.RAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertRAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception exception) { exception.printStackTrace(); }
                }
            }
        }
    }
}
