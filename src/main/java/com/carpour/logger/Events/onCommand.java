package com.carpour.logger.Events;

import com.carpour.logger.Discord.Discord;
import com.carpour.logger.Main;
import com.carpour.logger.Utils.FileHandler;
import com.carpour.logger.database.MySQL.MySQLData;
import com.carpour.logger.database.SQLite.SQLiteData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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


        //Stop Adding Message to Log if the Player has the correct Permissions
        if (player.hasPermission("logger.exempt")){ return; }


        //Logging to File if logging to File and Command Logging is enabled
        if (!event.isCancelled() && main.getConfig().getBoolean("Log-to-Files") && (main.getConfig().getBoolean("Log.Player-Commands"))){

            if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")){

                for (String m : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                    if (messageParts.get(0).equalsIgnoreCase(m)) {

                        if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")) {

                            try {

                                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has executed => " + message + "\n");
                                out.close();

                                if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Commands")) && (main.mySQL.isConnected())) {

                                    MySQLData.playerCommands(serverName, worldName, playerName, message, true);

                                }

                            } catch (IOException e) {

                                System.out.println("An error occurred while logging into the appropriate file.");
                                e.printStackTrace();

                            }
                        }

                        Discord.playerCommand(player, "\uD83D\uDC7E " + message, false, Color.red);

                        try {

                            BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                            out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has executed => " + message + "\n");
                            out.close();

                        } catch (IOException e) {

                            System.out.println("An error occurred while logging into the appropriate file.");
                            e.printStackTrace();

                        }

                        return;
                    }
                }

                return;

            }


            if (main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {

                for (String m : main.getConfig().getStringList("Player-Commands.Commands-to-Block")) {

                    if (messageParts.get(0).equalsIgnoreCase(m)) {

                        return;
                    }
                }
            }

            if (main.getConfig().getBoolean("Staff.Enabled") && player.hasPermission("logger.staff.log")){

                Discord.staffChat(player, "\uD83D\uDC7E **|** \uD83D\uDC6E\u200D♂️ " +  message, false, Color.red);

                try {

                    BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getStaffFile(), true));
                    out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Staff <" + playerName + "> has executed => " + message + "\n");
                    out.close();

                    if (main.getConfig().getBoolean("MySQL.Enable") && (main.getConfig().getBoolean("Log.Player-Commands")) && (main.mySQL.isConnected())) {


                        MySQLData.playerCommands(serverName, worldName, playerName, message, true);

                    }

                } catch (IOException e) {

                    System.out.println("An error occurred while logging into the appropriate file.");
                    e.printStackTrace();

                }

                return;

            }

            Discord.playerCommand(player, "\uD83D\uDC7E " +  message, false, Color.red);

            try {

                BufferedWriter out = new BufferedWriter(new FileWriter(FileHandler.getCommandLogFile(), true));
                out.write("[" + dateFormat.format(date) + "] " + "[" + worldName + "] The Player <" + playerName + "> has executed => " + message + "\n");
                out.close();

            } catch (IOException e) {

                System.out.println("An error occurred while logging into the appropriate file.");
                e.printStackTrace();

            }

        }

        //Logging to MySQL if logging to MySQL and Command Logging is enabled
        if ((main.getConfig().getBoolean("MySQL.Enable")) && (main.getConfig().getBoolean("Log.Player-Commands")) && (main.mySQL.isConnected())){

            //Command Whitelist
            if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")) {

                if (main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {
                    return;
                }

                for (String command : main.getConfig().getStringList("Player-Commands.Commands-to-Log")) {

                    List<String> commandParts = Arrays.asList(command.split("\\s+"));
                    if (messageParts.containsAll(commandParts)) {

                        try {
                            MySQLData.playerCommands(serverName, worldName, playerName, message, false);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
            //Command Blacklist
            if (main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {

                if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")) {
                    return;
                }

                for (String command : main.getConfig().getStringList("Player-Commands.Commands-to-Block")) {
                    List<String> commandParts = Arrays.asList(command.split("\\s+"));

                    if (messageParts.containsAll(commandParts)) {
                        return;
                    }
                }


                try {
                    MySQLData.playerCommands(serverName, worldName, playerName, message, false);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        //Logging to SQLite if logging to SQLite and Command Logging is enabled
        if (main.getConfig().getBoolean("SQLite.Enable") && main.getConfig().getBoolean("Log.Player-Commands") && main.getSqLite().isConnected()) {
            if (!(player.hasPermission("logger.exempt") || player.isOp())) {
                //Command Whitelist
                if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")) {

                    if (main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {
                        return;
                    }

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
                //Command Blacklist
                if (main.getConfig().getBoolean("Player-Commands.Blacklist-Commands")) {

                    if (main.getConfig().getBoolean("Player-Commands.Whitelist-Commands")) {
                        return;
                    }

                    for (String command : main.getConfig().getStringList("Player-Commands.Commands-to-Block")) {
                        List<String> commandParts = Arrays.asList(command.split("\\s+"));

                        if (messageParts.containsAll(commandParts)) {
                            return;
                        }
                    }

                    try {
                        SQLiteData.insertPlayerCommands(serverName, player, message, player.hasPermission("logger.staff.log"));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }

        if (main.getConfig().getBoolean("Log.Player-Commands") && main.getConfig().getBoolean("Player-Commands.Commands-Spy.Enable")) {

            List<String> blackListCommands = main.getConfig().getStringList("Player-Commands.Blacklist-Commands");
            if (!(event.getPlayer().hasPermission("logger.exempt") || player.isOp())) {

                for (String command : blackListCommands) {

                    if (event.getMessage().split(" ")[0].replaceFirst("/", "").equalsIgnoreCase(command)) return;

                }

                for (Player players : Bukkit.getOnlinePlayers()) {

                    if (players.hasPermission("logger.cmdspy")) {

                        players.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(main.getConfig().getString("Player-Commands.Commands-Spy.Message")).replace("%player%", event.getPlayer().getName()).replace("%cmd%", event.getMessage())));

                    }
                }
            }
        }
    }
}
