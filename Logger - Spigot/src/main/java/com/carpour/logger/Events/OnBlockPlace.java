package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.Database.MySQL.MySQLData;
import com.carpour.logger.Database.SQLite.SQLiteData;
import com.carpour.logger.Utils.Messages;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OnBlockPlace implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        String playerName = player.getName();
        World world = player.getWorld();
        String worldName = world.getName();
        int x = event.getBlock().getLocation().getBlockX();
        int y = event.getBlock().getLocation().getBlockY();
        int z = event.getBlock().getLocation().getBlockZ();
        Material blockType = event.getBlock().getType();
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (player.hasPermission("logger.exempt")) return;

        if (!event.isCancelled() && main.getConfig().getBoolean("Log.Block-Place")) {

            //Log To Files Handling
            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Block-Place-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%block%", String.valueOf(blockType)), false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write(Objects.requireNonNull(Messages.get().getString("Files.Block-Place-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%block%", String.valueOf(blockType)) + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {


                        MySQLData.blockPlace(serverName, worldName, playerName, blockType.toString(), x, y, z, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        SQLiteData.insertBlockPlace(serverName, player, blockType, true);
                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getBlockPlaceLogFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has placed " + blockType + " at X= " + x + " Y= " + y + " Z= " + z + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }

            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Block-Place-Staff")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%block%", String.valueOf(blockType)), false);

            } else {

                Discord.blockPlace(player, Objects.requireNonNull(Messages.get().getString("Discord.Block-Place")).replaceAll("%world%", worldName).replaceAll("%x%", String.valueOf(x)).replaceAll("%y%", String.valueOf(y)).replaceAll("%z%", String.valueOf(z)).replaceAll("%block%", String.valueOf(blockType)), false);

            }

            //MySQL
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                try {

                    MySQLData.blockPlace(serverName, worldName, playerName, blockType.toString(), x, y, z, player.hasPermission("logger.staff.log"));

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //SQLite
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertBlockPlace(serverName, player, blockType, player.hasPermission("logger.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
