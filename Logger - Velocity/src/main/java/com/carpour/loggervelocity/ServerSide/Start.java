package com.carpour.loggervelocity.ServerSide;

import com.carpour.loggervelocity.Database.MySQL.MySQL;
import com.carpour.loggervelocity.Database.MySQL.MySQLData;
import com.carpour.loggervelocity.Database.SQLite.SQLite;
import com.carpour.loggervelocity.Database.SQLite.SQLiteData;
import com.carpour.loggervelocity.Discord.Discord;
import com.carpour.loggervelocity.Main;
import com.carpour.loggervelocity.Utils.FileHandler;
import com.carpour.loggervelocity.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Start {

    public void run(){

        final Main main = Main.getInstance();
        final Messages messages = new Messages();

        final String serverName = main.getConfig().getString("Server-Name");
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (main.getConfig().getBoolean("Log-Server.Start")) {

            // Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStartLogFile(), true));
                    out.write(messages.getString("Files.Server-Side.Start").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getLogger().error("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            // Discord
            if (!messages.getString("Discord.Server-Side.Start").isEmpty()) {

                Discord.serverStart(messages.getString("Discord.Server-Side.Start").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);

            }

            // MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && MySQL.isConnected()) {

                try {

                    MySQLData.serverStart(serverName);

                } catch (Exception e) { e.printStackTrace(); }
            }

            // SQLite Handling
            if (main.getConfig().getBoolean("SQLite.Enable") && SQLite.isConnected()) {

                try {

                    SQLiteData.insertServerStart(serverName);

                } catch (Exception exception) { exception.printStackTrace(); }
            }

            if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")
                    && main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {

                main.getLogger().error("Enabling both Whitelist and Blacklist isn't supported. " +
                        "Please disable one of them to continue logging Player Commands");

            }
        }
    }
}
