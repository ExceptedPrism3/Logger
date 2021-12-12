package com.carpour.logger.ServerSide;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import org.carour.loggercore.database.mysql.MySQLData;
import com.carpour.logger.Utils.FileHandler;
import org.carour.loggercore.database.sqlite.SQLiteData;
import com.carpour.logger.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Start {

    private final Main main = Main.getInstance();

    public void run(){

        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (main.getConfig().getBoolean("Log.Server-Start")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getserverStartFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Start")).replaceAll("%time%", dateFormat.format(date)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            main.getDiscord().sendServerStart(Objects.requireNonNull(Messages.get().getString("Discord.Server-Start")).replaceAll("%time%", dateFormat.format(date)), false);

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.serverStart(serverName);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertServerStart(serverName);

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }

            if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")
                    && main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {

                main.getLogger().warning("Enabling both Whitelist and Blacklist isn't supported. " +
                        "Please disable one of them to continue logging Player Commands");

            }
        }
    }
}
