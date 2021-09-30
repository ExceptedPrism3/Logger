package com.carpour.logger.Events.onCommands;

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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class onCommand implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCmd(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        String message = event.getMessage();
        List<String> messageParts = Arrays.asList(event.getMessage().split("\\s+"));
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")
                && main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) return;

        //Stop Adding Message to Log if the Player has the correct Permissions
        if (player.hasPermission("logger.exempt")) return;


        //Blacklisted Commands
        if (main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {

            for (String m : main.getConfig().getStringList("Player-Commands.Commands-to-Block")) {

                if (messageParts.get(0).equalsIgnoreCase(m)) return;

            }
        }


        //Commands Spy
        if (main.getConfig().getBoolean("Player-Commands.Commands-Spy.Enable")) {

            onCommandSpy commandSpy = new onCommandSpy();
            commandSpy.onCmdSpy(event);

        }


        //Whitelist Commands
        if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")){

            onCommandWhitelist whitelist = new onCommandWhitelist();
            whitelist.onWhitelistedCommand(event);

            return;
        }

        //Logging to File if logging to File and Command Logging is enabled
        if (!event.isCancelled() && main.getConfig().getBoolean("Log.Player-Commands")) {

            if (main.getConfig().getBoolean("Log-to-Files")) {

                if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                    Discord.staffChat(player, "\uD83D\uDC7E **|** \uD83D\uDC6E\u200D♂️ " + message, false);

                    try {

                        BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                        out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has executed => " + message + "\n");
                        out.close();

                    } catch (IOException e) {

                        main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                        e.printStackTrace();

                    }

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Commands")) && (main.mySQL.isConnected())) {

                        MySQLData.playerCommands(serverName, worldName, playerName, message, true);

                    }

                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Player-Commands") && main.getSqLite().isConnected()) {

                        SQLiteData.insertPlayerCommands(serverName, player, message, true);

                    }

                    return;

                }

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has executed => " + message + "\n");
                    out.close();

                } catch (IOException e) {

                    main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }
            }



            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                Discord.staffChat(player, "\uD83D\uDC7E **|** \uD83D\uDC6E\u200D♂️ " + message, false);

            } else {

                Discord.playerCommand(player, "\uD83D\uDC7E " + message, false);

            }


            //Logging to MySQL if logging to MySQL and Command Logging is enabled
            if (main.getConfig().getBoolean("MySQL.Enable") && main.getConfig().getBoolean("Log.Player-Commands") && main.mySQL.isConnected()) {

                try {

                    MySQLData.playerCommands(serverName, worldName, playerName, message, player.hasPermission("logger.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }


            //Logging to SQLite if logging to SQLite and Command Logging is enabled
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Player-Commands") && main.getSqLite().isConnected()) {

                try {

                    SQLiteData.insertPlayerCommands(serverName, player, message, player.hasPermission("logger.staff.log"));

                } catch (Exception exception) {

                    exception.printStackTrace();

                }
            }
        }
    }
}
