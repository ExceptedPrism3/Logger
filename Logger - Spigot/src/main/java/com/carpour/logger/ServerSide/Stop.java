package com.carpour.logger.ServerSide;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.SQLite.SQLiteData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stop {

    private final Main main = Main.getInstance();

    public void run() {

        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (main.getConfig().getBoolean("Log.Server-Stop")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getserverStopFile(), true));
                    out.write("[" + dateFormat.format(date) + "]" + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            Discord.serverStop("\uD83D\uDD34 **" + dateFormat.format(date) + "**", false);

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.serverStop(serverName);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStop(serverName);

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
