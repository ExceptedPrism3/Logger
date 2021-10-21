package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class onPlayerDeath implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        double x = Math.floor(player.getLocation().getX());
        double y = Math.floor(player.getLocation().getY());
        double z = Math.floor(player.getLocation().getZ());
        String cause = Objects.requireNonNull(event.getEntity().getLastDamageCause()).getCause().name();
        String killer = "";
        String itemInUse = "";
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (!(event.getEntity().getKiller() == null)){

            if (event.getEntity().getLastDamageCause().getEntity() instanceof Player) {
                cause = "a Player";
            }

            killer = " named " + event.getEntity().getKiller().getName();
            itemInUse = " using " + Objects.requireNonNull(Objects.requireNonNull(event.getEntity().getKiller()).getItemInHand().getType().name());

        }

        if (main.getConfig().getBoolean("Log.Player-Death")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, "☠️ **|** \uD83D\uDC6E\u200D♂️ [" + worldName + "]" + " X = **" + x + "** Y = **" + y + "** Z = **" + z + "** | caused by **" + cause + "**" + "**" + killer + "**" + "**" + itemInUse + "**", false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has died at X = " + x + " Y = " + y + " Z = " + z + " | caused by " + cause + killer +  itemInUse + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.playerDeath(serverName, worldName, playerName, x, y, z, cause, killer, itemInUse, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerDeath(serverName, player, cause, killer, itemInUse, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getPlayerDeathLogFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has died at X = " + x + " Y = " + y + " Z = " + z + " | caused by " + cause + killer +  itemInUse + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                Discord.staffChat(player, "☠️ **|** \uD83D\uDC6E\u200D♂️ [" + worldName + "]" + " X = " + x + " Y = " + y + " Z = " + z + "** | caused by **" + cause + "**" + "**" + killer + "**" + "**" + itemInUse + "**", false);

            } else {

                Discord.playerDeath(player, "☠️ [" + worldName + "]" + " X = **" + x + "** Y = **" + y + "** Z = **" + z + "** | caused by **" + cause + "**" + "**" + killer + "**" + "**" + itemInUse + "**", false);

            }

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && (main.mySQL.isConnected())) {

                try {

                    MySQLData.playerDeath(serverName, worldName, playerName, x, y, z, cause, killer, itemInUse, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerDeath(serverName, player, cause, killer, itemInUse, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        }
    }
}
