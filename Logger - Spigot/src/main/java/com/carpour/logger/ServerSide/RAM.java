package com.carpour.logger.ServerSide;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                        out.write("[" + dateFormat.format(date) + "] The Server has exceeded the set amount of percentage of RAM Usage! Total Memory: " + maxMemory + "| Used Memory " + usedMemory + "| Free Memory " + freeMemory + "\n");
                        out.close();

                    } catch (
                            IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                //Discord
                Discord.RAM("⚠️ The server has **exceeded** the set amount of percentage of RAM Usage! Total Memory: **" + maxMemory + "** | Used Memory **" + usedMemory + "** | Free Memory **" + freeMemory + "**", false);

                //MySQL
                if (main.getConfig().getBoolean("MySQL.Enable")  && main.mySQL.isConnected()) {

                    try {

                        MySQLData.RAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception e) {

                        e.printStackTrace();

                    }
                }

                //SQLite
                if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertRAM(serverName, maxMemory, usedMemory, freeMemory);

                    } catch (Exception exception) {

                        exception.printStackTrace();

                    }
                }
            }
        }
    }
}
