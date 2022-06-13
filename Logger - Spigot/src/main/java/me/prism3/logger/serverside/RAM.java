package me.prism3.logger.serverside;

import me.prism3.logger.Main;
import me.prism3.logger.utils.FileHandler;
import me.prism3.logger.database.external.ExternalData;
import me.prism3.logger.database.sqlite.global.SQLiteData;
import me.prism3.logger.utils.Messages;
import me.prism3.logger.utils.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

public class RAM implements Runnable {

    private final Main main = Main.getInstance();

    public void run() {

        if (this.main.getConfig().getBoolean("Log-Server.RAM")) {

            if (Data.ramPercent <= 0 || Data.ramPercent >= 100) return;

            final long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
            final long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
            final long usedMemory = maxMemory - freeMemory;
            final double percentUsed = usedMemory * 100.0D / maxMemory;

            if (Data.ramPercent <= percentUsed) {

                // Log To Files
                if (Data.isLogToFiles) {

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRAMLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.RAM")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        this.main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                // Discord
                if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RAM")).isEmpty()) {

                    this.main.getDiscord().ram(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RAM")).replaceAll("%time%", Data.dateTimeFormatter.format(ZonedDateTime.now())).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)), false);
                }

                // External
                if (Data.isExternal && this.main.getExternal().isConnected()) {

                    try {

                        ExternalData.ram(Data.serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }

                // SQLite
                if (Data.isSqlite && this.main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertRAM(Data.serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }
}
