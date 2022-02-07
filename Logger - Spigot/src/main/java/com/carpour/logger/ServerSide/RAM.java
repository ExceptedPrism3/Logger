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
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class RAM implements Runnable {

    private final Main main = Main.getInstance();

    long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
    long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
    long usedMemory = maxMemory - freeMemory;
    double percentUsed = (double) usedMemory * 100.0D / (double) maxMemory;
    String serverName = main.getConfig().getString("Server-Name");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void run() {

        if (main.getConfig().getInt("RAM.Percent") <= 0 || main.getConfig().getInt("RAM.Percent") >= 100) {

            return;

        }

        if (main.getConfig().getInt("RAM.Percent") <= percentUsed) {

            if (main.getConfig().getBoolean("Log-Server.RAM")) {

                // Log To Files Handling
                if (main.getConfig().getBoolean("Log-to-Files")) {

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRAMLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.RAM")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RAM")).isEmpty()) {

                    Discord.RAM(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RAM")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)), false);
                }

                // MySQL
                if (main.getConfig().getBoolean("Database.Enable")  && main.external.isConnected()) {

                    try {

                        ExternalData.RAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertRAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception exception) { exception.printStackTrace(); }
                }
            }
        }
    }
}
