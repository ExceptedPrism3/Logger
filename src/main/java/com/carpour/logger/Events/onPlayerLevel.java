package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onPlayerLevel implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelChange(PlayerLevelChangeEvent event){

        Player player = event.getPlayer();
        String playerName = player.getName();
        int logAbove = main.getConfig().getInt("Player-Level.Log-Above");
        double playerLevel= event.getNewLevel();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Level"))) {

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")){

                if (playerLevel == logAbove) {

                    Discord.staffChat(player, "⬆️ **|** \uD83D\uDC6E\u200D♂ Has arrived to the set amount of Level which is **" + logAbove + "**", false, Color.GREEN);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                        out.write("[" + dateFormat.format(date) + "] The Level of the Staff <" + playerName + "> has arrived to the set amount which is " + logAbove + "\n");
                        out.close();

                        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Level")) && (main.mySQL.isConnected())) {

                            MySQLData.levelChange(serverName, playerName,true);

                        }

                    } catch (IOException e) {

                        System.out.println("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }
                }

                return;

            }

            if (playerLevel == logAbove) {

                Discord.playerLevel(player, "⬆️ Has arrived to the set amount of Level which is **" + logAbove + "**", false, Color.GREEN);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerLevelFile(), true));
                    out.write("[" + dateFormat.format(date) + "] The Level of the Player <" + playerName + "> has arrived to the set amount which is " + logAbove + "\n");
                    out.close();

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

        }

        if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Level")) && (main.mySQL.isConnected())){

            if (playerLevel == logAbove) {

                try {

                    MySQLData.levelChange(serverName, playerName, false);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
        if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Player-Level") && main.getSqLite().isConnected()) {
            if (playerLevel == logAbove) {
                try {
                    SQLiteData.insertLevelChange(serverName, player, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
