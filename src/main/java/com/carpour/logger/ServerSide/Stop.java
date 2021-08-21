package com.carpour.logger.ServerSide;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.database.MySQL.MySQLData;
import com.carpour.logger.Utils.FileHandler;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stop {

    private final Main main = Main.getInstance();

    public void run(){

        if (main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Server-Stop"))) {

            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

            Discord.serverStop("\uD83D\uDD34 " + dateFormat.format(date), false, Color.red);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStopFile(), true));
                out.write("[" + dateFormat.format(date) + "]" + "\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }

        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Server-Stop")) && (main.mySQL.isConnected())){

            String serverName = main.getConfig().getString("Server-Name");

            try {

                MySQLData.serverStop(serverName);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }
}
