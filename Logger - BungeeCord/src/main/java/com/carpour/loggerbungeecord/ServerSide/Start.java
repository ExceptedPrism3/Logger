package com.carpour.loggerbungeecord.ServerSide;

import com.carpour.loggerbungeecord.Database.MySQL.MySQLData;
import com.carpour.loggerbungeecord.Database.SQLite.SQLiteData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Start {

    private final Main main = Main.getInstance();

    public void run(){

        String serverName = main.getConfig().getString("Server-Name");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (main.getConfig().getBoolean("Log-Server.Start")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStartLogFile(), true));
                    out.write(Messages.getString("Files.Server-Side.Start").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            if (!Messages.getString("Discord.Server-Side.Start").isEmpty()) {

                Discord.serverStart(Messages.getString("Discord.Server-Side.Start").replaceAll("%time%", dateTimeFormatter.format(ZonedDateTime.now())), false);

            }

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.serverStart(serverName);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite Handling
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
