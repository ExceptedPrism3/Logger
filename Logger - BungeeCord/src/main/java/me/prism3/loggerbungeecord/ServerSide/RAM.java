package me.prism3.loggerbungeecord.ServerSide;

import me.prism3.loggerbungeecord.Database.External.ExternalData;
import me.prism3.loggerbungeecord.Database.SQLite.SQLiteData;
import me.prism3.loggerbungeecord.Discord.Discord;
import me.prism3.loggerbungeecord.Main;
import me.prism3.loggerbungeecord.Utils.FileHandler;
import me.prism3.loggerbungeecord.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static me.prism3.loggerbungeecord.Utils.Data.*;

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

                        ExternalData.ram(serverName, maxMemory, usedMemory, freeMemory);

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
