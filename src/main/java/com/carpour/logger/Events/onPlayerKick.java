package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.SQLite.SQLiteData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class onPlayerKick implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTabCompletion(PlayerKickEvent event){

        Player player = event.getPlayer();
        String reason = event.getReason();
        String worldName = player.getWorld().getName();
        String playerName = player.getName();
        double x = Math.floor(player.getLocation().getX());
        double y = Math.floor(player.getLocation().getY());
        double z = Math.floor(player.getLocation().getZ());
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (!event.isCancelled() && main.getConfig().getBoolean("Log.Player-Kick")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, "\uD83E\uDD7E **|** \uD83D\uDC6E\u200D♂ [" + worldName + "]" + " X = **" + x + "** Y = **" + y + "** Z = **" + z + "** **Reason** " + reason, false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> got kicked at X = " + x + " Y = " + y + " Z = " + z + " for " + reason + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.getConfig().getBoolean("Log.Player-Kick") && main.mySQL.isConnected()) {

                        MySQLData.playerKick(serverName, worldName, playerName, x, y, z, reason, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Player-Kick") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerKick(serverName, player, reason, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerKickLogFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> got kicked at X = " + x + " Y = " + y + " Z = " + z + " for " + reason + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                Discord.staffChat(player, "\uD83E\uDD7E **|** \uD83D\uDC6E\u200D♂ [" + worldName + "]" + " X = **" + x + "** Y = **" + y + "** Z = **" + z + "** **Reason** " + reason, false);

            } else {

                Discord.playerKick(player, "\uD83E\uDD7E [" + worldName + "]" + " X = **" + x + "** Y = **" + y + "** Z = **" + z + "** **Reason** " + reason, false);

            }

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Kick")) && (main.mySQL.isConnected())) {

                try {

                    MySQLData.playerKick(serverName, worldName, playerName, x, y, z, reason, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Player-Kick") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerKick(serverName, player, reason, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
