package com.carpour.logger.Events;

import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Utils.Messages;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class OnAFK implements Listener {

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

        if (player.hasPermission("logger.exempt")) return;

        if (!afk) {

            if (main.getConfig().getBoolean("Log.AFK")) {

                //Log To Files Handling
                if (main.getConfig().getBoolean("Log-to-Files")) {

                    if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.AFK-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                            out.write(Objects.requireNonNull(Messages.get().getString("Files.AFK-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                            out.close();

                        } catch (IOException event) {

                            main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            event.printStackTrace();

                        }

                        if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                            MySQLData.afk(serverName, worldName, playerName, x, y, z, true);

                        }

                        if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                            SQLiteData.insertAFK(serverName, player, true);

                        }

                        return;

                    }

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getAfkFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.AFK")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)) + "\n");
                        out.close();

                    } catch (IOException event) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        event.printStackTrace();

                    }
                }

                //Discord
                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.AFK-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                } else {

                    Discord.afk(player, Objects.requireNonNull(Messages.get().getString("Discord.AFK")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)), false);

                }

                //MySQL
                if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                    try {

                        MySQLData.afk(serverName, worldName, playerName, x, y, z, player.hasPermission("logger.staff.log"));

                    } catch (Exception event) {

                        event.printStackTrace();

                    }
                }

                //SQLite
                if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                    try {

                        SQLiteData.insertAFK(serverName, player, player.hasPermission("logger.staff.log"));

                    } catch (Exception exception) {

                        exception.printStackTrace();

                    }
                }
            }
        }
    }
}
