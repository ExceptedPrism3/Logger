package com.carpour.logger.Events.onCommands;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import org.carour.loggercore.database.mysql.MySQLData;
import org.carour.loggercore.database.sqlite.SQLiteData;
import com.carpour.logger.Utils.Messages;
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
import java.util.Objects;

public class OnCommandWhitelist implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWhitelistedCommand(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();
        String playerName = player.getName();
        String command = event.getMessage();
        List<String> commandParts = Arrays.asList(event.getMessage().split("\\s+"));
        String serverName = main.getConfig().getString("Server-Name");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


        if (!event.isCancelled() && main.getConfig().getBoolean("Log.Player-Commands")) {

            for (String m : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                if (commandParts.get(0).equalsIgnoreCase(m)) {

                    //Log to Files Handling
                    if (main.getConfig().getBoolean("Log-to-Files")) {

                        if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                            Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Commands-Whitelisted-Staff")).replaceAll("%world%", worldName).replaceAll("%command%", command), false);

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getstaffFile(), true));
                                out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Commands-Whitelisted-Staff")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                                out.close();

                            } catch (IOException e) {

                                main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }

                            if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                                MySQLData.playerCommands(serverName, worldName, playerName, command, true);

                            }

                            if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                                SQLiteData.insertPlayerCommands(serverName, player, command, true);

                            }
                            return;
                        }

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                            out.write(Objects.requireNonNull(Messages.get().getString("Files.Player-Commands-Whitelisted")).replaceAll("%time%", dateFormat.format(date)).replaceAll("%world%", worldName).replaceAll("%player%", playerName).replaceAll("%command%", command) + "\n");
                            out.close();

                        } catch (IOException e) {

                            main.getServer().getLogger().warning("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }
                    }

                    //Discord
                    if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                        Discord.staffChat(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Commands-Whitelisted-Staff")).replaceAll("%world%", worldName).replaceAll("%command%", command), false);

                    } else {

                        Discord.playerCommand(player, Objects.requireNonNull(Messages.get().getString("Discord.Player-Commands-Whitelisted")).replaceAll("%world%", worldName).replaceAll("%command%", command), false);

                    }

                    //Logging to MySQL if logging to MySQL and Command Logging is enabled
                    if (main.getConfig().getBoolean("MySQL.Enable") && main.mySQL.isConnected()) {

                        try {

                            MySQLData.playerCommands(serverName, worldName, playerName, command, player.hasPermission("logger.staff.log"));

                        } catch (Exception exception) {

                            exception.printStackTrace();

                        }
                    }

                    //Logging to SQLite if logging to SQLite and Command Logging is enabled
                    if (main.getConfig().getBoolean("SQLite.Enable") && main.getSqLite().isConnected()) {

                        try {

                            SQLiteData.insertPlayerCommands(serverName, player, command, player.hasPermission("logger.staff.log"));

                        } catch (Exception exception) {

                            exception.printStackTrace();

                        }
                    }
                }
            }
        }
    }
}
