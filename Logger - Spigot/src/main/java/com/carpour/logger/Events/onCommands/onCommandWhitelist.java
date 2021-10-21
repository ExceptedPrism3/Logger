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

public class onCommandWhitelist implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWhitelistedCommand(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        String message = event.getMessage();
        List<String> messageParts = Arrays.asList(event.getMessage().split("\\s+"));
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        //Logging to File if logging to File and Command Logging is enabled
        if (!event.isCancelled() && main.getConfig().getBoolean("Log.Player-Commands")) {

            if (main.getConfig().getBoolean("Log-to-Files")) {

                for (String m : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                    if (messageParts.get(0).equalsIgnoreCase(m)) {

                        if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                            Discord.staffChat(player, "\uD83D\uDC7E **|** \uD83D\uDC6E\u200D♂️ " + message, false);

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has executed a Whitelisted Command => " + message + "\n");
                                out.close();

                            } catch (IOException e) {

                                main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }

                            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                                MySQLData.playerCommands(serverName, worldName, playerName, message, true);

                            }

                            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                                SQLiteData.insertPlayerCommands(serverName, player, message, true);

                            }
                            return;
                        }

                        Discord.playerCommand(player, "\uD83D\uDC7E " + message, false);

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                            out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has executed a Whitelisted Command => " + message + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                            MySQLData.playerCommands(serverName, worldName, playerName, message, player.hasPermission("logger.staff.log"));

                        }

                        if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                            SQLiteData.insertPlayerCommands(serverName, player, message, player.hasPermission("logger.staff.log"));

                        }
                    }
                }return;
            }

            //Discord
            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                for (String command : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                    List<String> commandParts = Arrays.asList(command.split("\\s+"));
                    if (messageParts.containsAll(commandParts)) {

                        Discord.staffChat(player, "\uD83D\uDC7E **|** \uD83D\uDC6E\u200D♂️ " + message, false);

                    }
                }

            } else {

                for (String command : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                    List<String> commandParts = Arrays.asList(command.split("\\s+"));

                    if (messageParts.containsAll(commandParts)) {

                        Discord.playerCommand(player, "\uD83D\uDC7E " + message, false);

                    }
                }
            }

            //Logging to MySQL if logging to MySQL and Command Logging is enabled
            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {
                for (String command : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                    List<String> commandParts = Arrays.asList(command.split("\\s+"));

                    if (messageParts.containsAll(commandParts)) {

                        try {

                            MySQLData.playerCommands(serverName, worldName, playerName, message, player.hasPermission("logger.staff.log"));

                        } catch (Exception exception) {

                            exception.printStackTrace();

                        }
                    }
                }
            }

            //Logging to SQLite if logging to SQLite and Command Logging is enabled
            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                for (String command : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                    List<String> commandParts = Arrays.asList(command.split("\\s+"));

                    if (messageParts.containsAll(commandParts)) {

                        try {

                            SQLiteData.insertPlayerCommands(serverName, player, message, player.hasPermission("logger.staff.log"));

                        } catch (Exception exception) {

                            exception.printStackTrace();

                        }
                    }
                }
            }
        }
    }
}
