package com.carpour.logger.serverside;

import com.carpour.logger.Main;
import com.carpour.logger.utils.FileHandler;
import com.carpour.logger.utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class RAM implements Runnable {

    private final Main main = Main.getInstance();

    long maxMemory = Runtime.getRuntime().maxMemory() / 1048576L;
    long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
    long usedMemory = maxMemory - freeMemory;
    double percentUsed = (double) usedMemory * 100.0D / (double) maxMemory;
    String serverName = main.getConfig().getString("Server-Name");
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public void run() {

        if (main.getConfig().getInt("RAM.Percent") <= 0 || main.getConfig().getInt("RAM.Percent") >= 100) {

            return;

        }

        if (main.getConfig().getInt("RAM.Percent") <= percentUsed) {

            if (main.getConfig().getBoolean("Log.RAM")) {

                //Log To Files Handling
                if (main.getConfig().getBoolean("Log-to-Files")) {

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRAMLogFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.RAM")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)) + "\n");
                        out.close();

                    } catch (
                            IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                //Discord
                main.getDiscord().sendRAM(Objects.requireNonNull(Messages.get().getString("Discord.RAM")).replaceAll("%max%", String.valueOf(maxMemory)).replaceAll("%used%", String.valueOf(usedMemory)).replaceAll("%free%", String.valueOf(freeMemory)), false);

                //MySQL
                if (main.getConfig().getBoolean("MySQL.Enable")  && main.getMySQL().isConnected()) {

                    try {

                        main.getMySQLData().RAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
                }

                //SQLite
                if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                    try {

                        main.getSqLiteData().insertRAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception exception) {

                        exception.printStackTrace();

                    }
                }
            }
        }
    }
}
