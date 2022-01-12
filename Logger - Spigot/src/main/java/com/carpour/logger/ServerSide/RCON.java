package com.carpour.logger.ServerSide;

import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class RCON implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onConnection(RemoteServerCommandEvent event) {

        String ip = event.getSender().getServer().getIp();
        String command = event.getCommand();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (main.getConfig().getBoolean("Log-Server.RCON")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getRconFile(), true));
                    out.write(Objects.requireNonNull(Messages.get().getString("Files.Server-Side.RCON")).replaceAll("%time%", dateFormat.format(date).replaceAll("%IP%", ip).replaceAll("%command%", command)) + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            if (!Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RCON")).isEmpty()) {

                Discord.rcon(Objects.requireNonNull(Messages.get().getString("Discord.Server-Side.RCON")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%time%", dateFormat.format(date).replaceAll("%IP%", ip).replaceAll("%command%", command)), false);
            }

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.rcon(serverName, ip, command);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertRcon(serverName, ip, command);

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
