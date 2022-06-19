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
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Stop")).replaceAll("%time%", dateFormat.format(date)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            main.getDiscord().sendServerStop(Objects.requireNonNull(Messages.get().getString("Discord.Server-Stop")).replaceAll("%time%", dateFormat.format(date)), false);

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.getMySQL().isConnected()) {

                try {

                    main.getMySQLData().serverStop(serverName);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    main.getSqLiteData().insertServerStop(serverName);

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
