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

public class Start {

    private final Main main = Main.getInstance();

    public void run(){

        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (main.getConfig().getBoolean("Log-to-Files") && main.getConfig().getBoolean("Log.Server-Start")) {

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getserverStartFile(), true));
                out.write("[" + dateFormat.format(date) + "]" + "\n");
                out.close();

            } catch (IOException e) {

                main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }
        }

        Discord.serverStart("\uD83D\uDFE2 " + dateFormat.format(date), false);

        if (main.getConfig().getBoolean("MySQL.Enable") && main.getConfig().getBoolean("Log.Server-Start") && main.mySQL.isConnected()){

            try {

                MySQLData.serverStart(serverName);

            }catch (Exception e){

                e.printStackTrace();

            }
        }

        if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Server-Start") && main.getSqLite().isConnected()) {

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
