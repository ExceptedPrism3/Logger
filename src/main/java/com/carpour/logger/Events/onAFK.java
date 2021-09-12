package com.carpour.logger.Events;

import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class onAFK implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void afk(AfkStatusChangeEvent e) {

        boolean afk = e.getAffected().isAfk();

        Player player = e.getAffected().getBase();
        String playerName = player.getName();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        String worldName = player.getWorld().getName();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (!afk) {

            if (player.hasPermission("logger.exempt")) return;

            if (main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.AFK"))) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")){

                    Discord.afk(player, "\uD83D\uDCA4 **|** \uD83D\uDC6E\u200D♂ [" + worldName + "]" + " X = " + x + " Y = " + y + " Z = " + z, false, Color.CYAN);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> went AFK at X = " + x + " Y = " + y + " Z = " + z + "\n");
                        out.close();

                        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.AFK")) && (main.mySQL.isConnected())) {

                            MySQLData.afk(serverName, worldName, playerName, x, y, z, true);

                        }

                    } catch (IOException event) {

                        System.out.println("An error occurred while logging into the appropriate file.");
                        event.printStackTrace();

                    }

                    return;

                }

                Discord.afk(player, "\uD83D\uDCA4 [" + worldName + "]" + " X = " + x + " Y = " + y + " Z = " + z, false, Color.RED);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAfkFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has went AFK at X = " + x + " Y = " + y + " Z = " + z + "\n");
                    out.close();

                } catch (IOException event) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    event.printStackTrace();

                }

            }

            if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.AFK")) && (main.mySQL.isConnected())){

                try {

                    MySQLData.afk(serverName, worldName, playerName, x, y, z, false);

                }catch (Exception event) {

                    event.printStackTrace();

                }
            }
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.AFK") && main.getSqLite().isConnected()) {
                try {
                    SQLiteData.insertAFK(serverName, player, player.hasPermission("logger.staff.log"));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
