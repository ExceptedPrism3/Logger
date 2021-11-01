package com.carpour.loggerbungeecord.ServerSide;

import com.carpour.loggerbungeecord.Database.MySQL.MySQLData;
import com.carpour.loggerbungeecord.Discord.Discord;
import com.carpour.loggerbungeecord.Main;
import com.carpour.loggerbungeecord.Utils.ConfigManager;
import com.carpour.loggerbungeecord.Utils.FileHandler;
import com.carpour.loggerbungeecord.Utils.Messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stop {

    private final ConfigManager cm = Main.getConfig();

    private final Main main = Main.getInstance();

    public void run() {

        String serverName = cm.getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (cm.getBoolean("Log.Server-Stop")) {

            //Log To Files Handling
            if (cm.getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getServerStopLogFile(), true));
                    out.write(Messages.getString("Files.Server-Start").replaceAll("%time%", dateFormat.format(date)) + "\n");
                    out.close();

                } catch (IOException e) {

                    Main.getInstance().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            Discord.serverStop(Messages.getString("Discord.Server-Stop").replaceAll("%time%", dateFormat.format(date)), false);

            //MySQL
            if (cm.getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.serverStop(serverName);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
