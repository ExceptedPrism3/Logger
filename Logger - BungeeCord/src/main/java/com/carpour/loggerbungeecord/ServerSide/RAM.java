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
import java.util.Objects;

import static com.carpour.loggerbungeecord.Utils.Data.*;

public class RAM implements Runnable{

    private final Main main = Main.getInstance();

    public void run() {

        if (this.main.getConfig().getBoolean("Log-Server.RAM")) {

            if (ramPercent <= 0 || ramPercent >= 100) return;

            long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
            long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
            long usedMemory = maxMemory - freeMemory;
            double percentUsed = (double) usedMemory * 100.0D / (double) maxMemory;

            if (ramPercent <= percentUsed) {

                //Log To Files Handling
                if (isLogToFiles) {

                    try {

                        final BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRAMLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.getString("Files.Server-Side.RAM")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        Main.getInstance().getLogger().severe("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!Messages.getString("Discord.Server-Side.RAM").isEmpty()) {

                    Discord.ram(Objects.requireNonNull(Messages.getString("Discord.Server-Side.RAM")).replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)), false);

                }

                // External
                if (isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.RAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertRAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
